/*
 * Cancel
 * Metaplex
 * 
 * Created by Funkatronics on 8/26/2022
 */

package com.metaplex.lib.modules.auctions.models

import com.metaplex.lib.experimental.jen.auctionhouse.AuctionHouseInstructions
import com.metaplex.lib.modules.auctions.SYSVAR_INSTRUCTIONS_PUBKEY
import com.metaplex.lib.modules.auctions.associatedTokenAddress
import com.solana.core.PublicKey
import com.solana.core.Transaction
import com.solana.programs.SystemProgram
import com.solana.programs.TokenProgram

fun buildAuctionCancelInstruction(auctionHouse: AuctionHouse, wallet: PublicKey, mint: PublicKey,
                                  tradeState: PublicKey, price: Long, tokenSize: Long,
                                  purchaseReceipt: PublicKey? = null, authority: PublicKey? = null)
: Transaction {

    val tokenAccount = PublicKey.associatedTokenAddress(wallet, mint).address

    return Transaction().apply {
        addInstruction(
            authority?.let {
                AuctionHouseInstructions.auctioneerCancel(
                    wallet = wallet,
                    tokenAccount = tokenAccount,
                    tokenMint = mint,
                    authority = auctionHouse.authority,
                    auctioneerAuthority = authority,
                    auctionHouse = auctionHouse.address,
                    auctionHouseFeeAccount = auctionHouse.auctionHouseFeeAccount,
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
                auctionHouseFeeAccount = auctionHouse.auctionHouseFeeAccount,
                tokenProgram = TokenProgram.PROGRAM_ID,
                tradeState = tradeState,
                buyerPrice = price.toULong(),
                tokenSize = tokenSize.toULong()
            )
        )

        purchaseReceipt?.let {
            addInstruction(
                AuctionHouseInstructions.cancelBidReceipt(
                    receipt = purchaseReceipt,
                    instruction = PublicKey(SYSVAR_INSTRUCTIONS_PUBKEY),
                    systemProgram = SystemProgram.PROGRAM_ID
                )
            )
        }
    }
}