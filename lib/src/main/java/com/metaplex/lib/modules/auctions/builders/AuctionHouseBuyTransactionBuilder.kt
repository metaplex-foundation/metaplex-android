/*
 * AuctionHouseBuyTransactionBuilder
 * Metaplex
 * 
 * Created by Funkatronics on 10/21/2022
 */

package com.metaplex.lib.modules.auctions.builders

import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.experimental.jen.auctionhouse.AuctionHouseInstructions
import com.metaplex.lib.modules.auctions.models.*
import com.metaplex.lib.shared.builders.TransactionBuilder
import com.solana.core.*
import com.solana.programs.AssociatedTokenProgram
import com.solana.programs.SystemProgram
import com.solana.programs.TokenProgram
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuctionHouseBuyTransactionBuilder(
    val auctionHouse: AuctionHouse, val purchase: Purchase, val printReceipt: Boolean = true,
    connection: Connection, dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TransactionBuilder(purchase.buyer, connection, dispatcher) {

    var creators = listOf(purchase.seller)

    fun addCreators(creators: List<PublicKey>): AuctionHouseBuyTransactionBuilder {
        this.creators = creators
        return this
    }

    override suspend fun build(): Result<Transaction> = withContext(dispatcher) {
        Result.success(Transaction().apply {
            purchase.apply {

                val programAsSigner = AuctionHouse.programAsSignerPda()

                val saleInstruction = auctioneerAuthority?.let {
                    AuctionHouseInstructions.auctioneerExecuteSale(
                        buyer = buyer, seller = seller,
                        tokenAccount = assetTokenAccount,
                        tokenMint = mintAccount,
                        metadata = assetMetadata,
                        treasuryMint = auctionHouse.treasuryMint,
                        escrowPaymentAccount = escrowPayment.address,
                        sellerPaymentReceiptAccount = sellerPaymentReceiptAccount,
                        buyerReceiptTokenAccount = buyerReceiptTokenAccount,
                        authority = auctionHouse.authority,
                        auctioneerAuthority = auctioneerAuthority,
                        auctionHouse = auctionHouse.address,
                        auctionHouseFeeAccount = auctionHouse.auctionHouseFeeAccount,
                        auctionHouseTreasury = auctionHouse.auctionHouseTreasury,
                        buyerTradeState = buyerTradeState,
                        sellerTradeState = sellerTradeState,
                        freeTradeState = freeTradeState.address,
                        ahAuctioneerPda = auctionHouse.auctioneerPda(auctioneerAuthority),
                        tokenProgram = TokenProgram.PROGRAM_ID,
                        systemProgram = SystemProgram.PROGRAM_ID,
                        ataProgram = AssociatedTokenProgram.SPL_ASSOCIATED_TOKEN_ACCOUNT_PROGRAM_ID,
                        programAsSigner = programAsSigner.address,
                        rent = Sysvar.SYSVAR_RENT_PUBKEY,
                        freeTradeStateBump = freeTradeState.nonce.toUByte(),
                        escrowPaymentBump = escrowPayment.nonce.toUByte(),
                        programAsSignerBump = programAsSigner.nonce.toUByte(),
                        buyerPrice = price.toULong(),
                        tokenSize = tokens.toULong()
                    )
                } ?: AuctionHouseInstructions.executeSale(
                    buyer = buyer, seller = seller,
                    tokenAccount = assetTokenAccount,
                    tokenMint = mintAccount,
                    metadata = assetMetadata,
                    treasuryMint = auctionHouse.treasuryMint,
                    escrowPaymentAccount = escrowPayment.address,
                    sellerPaymentReceiptAccount = sellerPaymentReceiptAccount,
                    buyerReceiptTokenAccount = buyerReceiptTokenAccount,
                    authority = auctionHouse.authority,
                    auctionHouse = auctionHouse.address,
                    auctionHouseFeeAccount = auctionHouse.auctionHouseFeeAccount,
                    auctionHouseTreasury = auctionHouse.auctionHouseTreasury,
                    buyerTradeState = buyerTradeState,
                    sellerTradeState = sellerTradeState,
                    freeTradeState = freeTradeState.address,
                    tokenProgram = TokenProgram.PROGRAM_ID,
                    systemProgram = SystemProgram.PROGRAM_ID,
                    ataProgram = AssociatedTokenProgram.SPL_ASSOCIATED_TOKEN_ACCOUNT_PROGRAM_ID,
                    programAsSigner = programAsSigner.address,
                    rent = Sysvar.SYSVAR_RENT_PUBKEY,
                    freeTradeStateBump = freeTradeState.nonce.toUByte(),
                    escrowPaymentBump = escrowPayment.nonce.toUByte(),
                    programAsSignerBump = programAsSigner.nonce.toUByte(),
                    buyerPrice = price.toULong(),
                    tokenSize = tokens.toULong()
                )

                val additionalAccounts = mutableListOf<AccountMeta>()

                creators.forEach { address ->
                    additionalAccounts.add(AccountMeta(address, false, true))

                    if (!auctionHouse.isNative)
                        additionalAccounts.add(AccountMeta(PublicKey.associatedTokenAddress(address, mintAccount).address, false, true))
                }

                addInstruction(TransactionInstruction(saleInstruction.programId, saleInstruction.keys + additionalAccounts, saleInstruction.data))

                if (printReceipt) {

                    val listingReceipt = AuctionHouse.listingReceiptPda(sellerTradeState)
                    val bidReceipt = AuctionHouse.bidReceiptPda(buyerTradeState)
                    val purchaseReceipt = AuctionHouse.purchaseReceiptPda(sellerTradeState, buyerTradeState)

                    addInstruction(
                        AuctionHouseInstructions.printPurchaseReceipt(
                            purchaseReceipt = purchaseReceipt.address,
                            listingReceipt = listingReceipt.address,
                            bidReceipt = bidReceipt.address,
                            bookkeeper = bookkeeper,
                            systemProgram = SystemProgram.PROGRAM_ID,
                            rent = Sysvar.SYSVAR_RENT_PUBKEY,
                            instruction = Sysvar.SYSVAR_INSTRUCTIONS_PUBKEY,
                            purchaseReceiptBump = purchaseReceipt.nonce.toUByte()
                        )
                    )
                }
            }
        })
    }
}