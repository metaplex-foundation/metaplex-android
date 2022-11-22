/*
 * Bid
 * Metaplex
 * 
 * Created by Funkatronics on 8/16/2022
 */

package com.metaplex.lib.modules.auctions.models

import com.metaplex.lib.experimental.jen.auctionhouse.AuctionHouseInstructions
import com.metaplex.lib.programs.token_metadata.accounts.MetadataAccount
import com.solana.core.PublicKey
import com.solana.core.Sysvar
import com.solana.core.Transaction
import com.solana.programs.SystemProgram
import com.solana.programs.TokenProgram

data class Bid(
    val auctionHouse: AuctionHouse,
    val mintAccount: PublicKey, // Required for checking Metadata
    val buyer: PublicKey, // Default: identity
    val authority: PublicKey = auctionHouse.authority, // Default: auctionHouse.authority
    val auctioneerAuthority: PublicKey? = null, // Use Auctioneer ix when provided
    val tokenAccount: PublicKey? = null,
    val price: Long = 0, // Default: 0 SOLs or tokens.
    val tokens: Long = 1, // Default: token(1)
    val bookkeeper: PublicKey = buyer, // Default: identity
    val canceledAt: Long? = null
)

// TODO: handle Result
internal val Bid.metadata get() = MetadataAccount.pda(mintAccount).getOrThrows()

internal val Bid.buyerTokenAccount get() =
    PublicKey.associatedTokenAddress(buyer, mintAccount).address

internal val Bid.paymentAccount get() = if (auctionHouse.isNative) buyer else
    PublicKey.associatedTokenAddress(buyer, auctionHouse.treasuryMint).address

internal val Bid.escrowPayment get() = auctionHouse.buyerEscrowPda(buyer)

internal val Bid.buyerTradeState get() =
    auctionHouse.tradeStatePda(buyer, mintAccount, price, tokens, tokenAccount)

internal val Bid.receiptAddress get() = AuctionHouse.bidReceiptPda(buyerTradeState.address)

fun Bid.buildTransaction(printReceipt: Boolean = true) = Transaction().apply {
    addInstruction(auctioneerAuthority?.let {
        tokenAccount?.let {
            AuctionHouseInstructions.auctioneerBuy(
                wallet = buyer,
                paymentAccount = paymentAccount,
                transferAuthority = buyer,
                treasuryMint = auctionHouse.treasuryMint,
                metadata = metadata,
                escrowPaymentAccount = escrowPayment.address,
                authority = authority,
                auctioneerAuthority = auctioneerAuthority,
                auctionHouse = auctionHouse.address,
                auctionHouseFeeAccount = auctionHouse.auctionHouseFeeAccount,
                buyerTradeState = buyerTradeState.address,
                tokenAccount = tokenAccount,
                ahAuctioneerPda = auctionHouse.auctioneerPda(authority),
                tokenProgram = TokenProgram.PROGRAM_ID,
                systemProgram = SystemProgram.PROGRAM_ID,
                rent = Sysvar.SYSVAR_RENT_PUBKEY,
                tradeStateBump = buyerTradeState.nonce.toUByte(),
                escrowPaymentBump = escrowPayment.nonce.toUByte(),
                buyerPrice = price.toULong(), tokenSize = tokens.toULong()
            )
        } ?: AuctionHouseInstructions.auctioneerPublicBuy(
            wallet = buyer,
            paymentAccount = paymentAccount,
            transferAuthority = buyer,
            treasuryMint = auctionHouse.treasuryMint,
            metadata = metadata,
            escrowPaymentAccount = escrowPayment.address,
            authority = authority,
            auctioneerAuthority = auctioneerAuthority,
            auctionHouse = auctionHouse.address,
            auctionHouseFeeAccount = auctionHouse.auctionHouseFeeAccount,
            buyerTradeState = buyerTradeState.address,
            tokenAccount = buyerTokenAccount,
            ahAuctioneerPda = auctionHouse.auctioneerPda(authority),
            tokenProgram = TokenProgram.PROGRAM_ID,
            systemProgram = SystemProgram.PROGRAM_ID,
            rent = Sysvar.SYSVAR_RENT_PUBKEY,
            tradeStateBump = buyerTradeState.nonce.toUByte(),
            escrowPaymentBump = escrowPayment.nonce.toUByte(),
            buyerPrice = price.toULong(), tokenSize = tokens.toULong()
        )
    } ?: tokenAccount?.let {
        AuctionHouseInstructions.buy(
            wallet = buyer,
            paymentAccount = paymentAccount,
            transferAuthority = buyer,
            treasuryMint = auctionHouse.treasuryMint,
            metadata = metadata,
            escrowPaymentAccount = escrowPayment.address,
            authority = authority,
            auctionHouse = auctionHouse.address,
            auctionHouseFeeAccount = auctionHouse.auctionHouseFeeAccount,
            buyerTradeState = buyerTradeState.address,
            tokenAccount = tokenAccount,
            tokenProgram = TokenProgram.PROGRAM_ID,
            systemProgram = SystemProgram.PROGRAM_ID,
            rent = Sysvar.SYSVAR_RENT_PUBKEY,
            tradeStateBump = buyerTradeState.nonce.toUByte(),
            escrowPaymentBump = escrowPayment.nonce.toUByte(),
            buyerPrice = price.toULong(), tokenSize = tokens.toULong()
        )
    } ?: AuctionHouseInstructions.publicBuy(
        wallet = buyer,
        paymentAccount = paymentAccount,
        transferAuthority = buyer,
        treasuryMint = auctionHouse.treasuryMint,
        metadata = metadata,
        escrowPaymentAccount = escrowPayment.address,
        authority = authority,
        auctionHouse = auctionHouse.address,
        auctionHouseFeeAccount = auctionHouse.auctionHouseFeeAccount,
        buyerTradeState = buyerTradeState.address,
        tokenAccount = buyerTokenAccount,
        tokenProgram = TokenProgram.PROGRAM_ID,
        systemProgram = SystemProgram.PROGRAM_ID,
        rent = Sysvar.SYSVAR_RENT_PUBKEY,
        tradeStateBump = buyerTradeState.nonce.toUByte(),
        escrowPaymentBump = escrowPayment.nonce.toUByte(),
        buyerPrice = price.toULong(), tokenSize = tokens.toULong()
    ))

    // TODO: Since printBidReceipt can't deserialize createAuctioneerBuyInstruction due
    //  to a bug, don't print Auctioneer Bid receipt for the time being.
    if (printReceipt && auctioneerAuthority == null) {

        val receipt = AuctionHouse.bidReceiptPda(buyerTradeState.address)

        addInstruction(
            AuctionHouseInstructions.printBidReceipt(
                receipt = receipt.address,
                bookkeeper = bookkeeper,
                instruction = Sysvar.SYSVAR_INSTRUCTIONS_PUBKEY,
                systemProgram = SystemProgram.PROGRAM_ID,
                rent = Sysvar.SYSVAR_RENT_PUBKEY,
                receiptBump = receipt.nonce.toUByte()
            )
        )
    }
}