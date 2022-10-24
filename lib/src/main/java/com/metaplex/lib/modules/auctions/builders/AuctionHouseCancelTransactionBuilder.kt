/*
 * AuctionHouseCancelTransactionBuilder
 * Metaplex
 * 
 * Created by Funkatronics on 10/21/2022
 */

package com.metaplex.lib.modules.auctions.builders

import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.experimental.jen.auctionhouse.AuctionHouseInstructions
import com.metaplex.lib.modules.auctions.models.*
import com.metaplex.lib.shared.builders.TransactionBuilder
import com.solana.core.PublicKey
import com.solana.core.Sysvar
import com.solana.core.Transaction
import com.solana.programs.SystemProgram
import com.solana.programs.TokenProgram
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuctionHouseCancelTransactionBuilder(
    val auctionHouse: AuctionHouse, val wallet: PublicKey, val mint: PublicKey,
    val tradeState: PublicKey, val price: Long, val tokenSize: Long,
    val purchaseReceipt: PublicKey? = null, val authority: PublicKey? = null,
    val mode: Mode = Mode.LISTING,
    connection: Connection, dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TransactionBuilder(wallet, connection, dispatcher) {

    enum class Mode {
        LISTING, BID
    }

    constructor(auctionHouse: AuctionHouse, listing: Listing, mint: PublicKey,
                auctioneerAuthority: PublicKey? = null, connection: Connection,
                dispatcher: CoroutineDispatcher = Dispatchers.IO) : this(auctionHouse,
        listing.seller, mint, listing.sellerTradeState.address, listing.price, listing.tokens,
        listing.receiptAddress.address, auctioneerAuthority, Mode.LISTING, connection, dispatcher
    )

    constructor(auctionHouse: AuctionHouse, bid: Bid, mint: PublicKey,
                auctioneerAuthority: PublicKey? = null, connection: Connection,
                dispatcher: CoroutineDispatcher = Dispatchers.IO) : this(auctionHouse,
        bid.buyer, mint, bid.buyerTradeState.address, bid.price, bid.tokens,
        bid.receiptAddress.address, auctioneerAuthority, Mode.BID, connection, dispatcher
    )

    override suspend fun build(): Result<Transaction> = withContext(dispatcher) {

        val tokenAccount = PublicKey.associatedTokenAddress(wallet, mint).address

        Result.success(Transaction().apply {
            addInstruction(
                authority?.let {
                    AuctionHouseInstructions.auctioneerCancel(
                        wallet = wallet,
                        tokenAccount = tokenAccount,
                        tokenMint = mint,
                        authority = auctionHouse.authority,
                        auctioneerAuthority = authority,
                        auctionHouse = auctionHouse.address,
                        auctionHouseFeeAccount = auctionHouse.feeAccountPda().address,
                        tradeState = tradeState,
                        ahAuctioneerPda = auctionHouse.auctioneerPda(authority),
                        tokenProgram = TokenProgram.PROGRAM_ID,
                        buyerPrice = price.toULong(),
                        tokenSize = tokenSize.toULong()
                    )
                } ?: AuctionHouseInstructions.cancel(
                    wallet = wallet,
                    tokenAccount = tokenAccount,
                    tokenMint = mint,
                    authority = auctionHouse.authority,
                    auctionHouse = auctionHouse.address,
                    auctionHouseFeeAccount = auctionHouse.feeAccountPda().address,
                    tokenProgram = TokenProgram.PROGRAM_ID,
                    tradeState = tradeState,
                    buyerPrice = price.toULong(),
                    tokenSize = tokenSize.toULong()
                )
            )

            purchaseReceipt?.let {
                addInstruction( when (mode) {
                    Mode.LISTING -> AuctionHouseInstructions.cancelListingReceipt(
                        receipt = purchaseReceipt,
                        instruction = Sysvar.SYSVAR_INSTRUCTIONS_PUBKEY,
                        systemProgram = SystemProgram.PROGRAM_ID
                    )
                    Mode.BID -> AuctionHouseInstructions.cancelBidReceipt(
                        receipt = purchaseReceipt,
                        instruction = Sysvar.SYSVAR_INSTRUCTIONS_PUBKEY,
                        systemProgram = SystemProgram.PROGRAM_ID
                    )
                })
            }
        })
    }
}