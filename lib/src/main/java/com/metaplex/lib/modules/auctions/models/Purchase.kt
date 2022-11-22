/*
 * Purchase
 * Metaplex
 * 
 * Created by Funkatronics on 8/26/2022
 */

package com.metaplex.lib.modules.auctions.models

import com.metaplex.lib.experimental.jen.auctionhouse.AuctionHouseInstructions
import com.metaplex.lib.programs.token_metadata.accounts.MetadataAccount
import com.solana.core.*
import com.solana.programs.AssociatedTokenProgram
import com.solana.programs.SystemProgram
import com.solana.programs.TokenProgram

data class Purchase(
    val auctionHouse: AuctionHouse,
    val bookkeeper: PublicKey,
    val buyer: PublicKey,
    val seller: PublicKey,
    val mintAccount: PublicKey,
    val auctioneerAuthority: PublicKey? = null, // Use Auctioneer ix when provided
    val buyerTradeState: PublicKey,
    val sellerTradeState: PublicKey,
    val tokens: Long,
    val price: Long
)

val Purchase.assetTokenAccount get() =
    PublicKey.associatedTokenAddress(seller, mintAccount).address

val Purchase.assetMetadata get() = MetadataAccount.pda(mintAccount).getOrThrows()

val Purchase.escrowPayment get() = auctionHouse.buyerEscrowPda(buyer)

val Purchase.freeTradeState get() =
    auctionHouse.tradeStatePda(seller, mintAccount, 0, tokens, assetTokenAccount)

val Purchase.sellerPaymentReceiptAccount get() = if (auctionHouse.isNative) seller
else PublicKey.associatedTokenAddress(seller, auctionHouse.treasuryMint).address

val Purchase.buyerReceiptTokenAccount get() =
    PublicKey.associatedTokenAddress(buyer, mintAccount).address


fun Purchase.buildTransaction(printReceipt: Boolean = true) = Transaction().apply {

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

    // for each creator: (need to get asset creators list)
    additionalAccounts.add(AccountMeta(seller, false, true))

    if (!auctionHouse.isNative)
        additionalAccounts.add(AccountMeta(PublicKey.associatedTokenAddress(seller, mintAccount).address, false, true))
    // end for each

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