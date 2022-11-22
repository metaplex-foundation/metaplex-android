/*
 * Listing
 * Metaplex
 * 
 * Created by Funkatronics on 8/18/2022
 */

package com.metaplex.lib.modules.auctions.models

import com.metaplex.lib.experimental.jen.auctionhouse.AuctionHouseInstructions
import com.metaplex.lib.programs.token_metadata.accounts.MetadataAccount
import com.solana.core.PublicKey
import com.solana.core.Sysvar
import com.solana.core.Transaction
import com.solana.programs.SystemProgram
import com.solana.programs.TokenProgram

// Auctioneer uses "u64::MAX" for the price which is "2^64 − 1".
const val AUCTIONEER_PRICE: Long = -1

data class Listing(
    val auctionHouse: AuctionHouse,
    val seller: PublicKey, // Default: identity
    val authority: PublicKey = auctionHouse.authority, // Default: auctionHouse.authority
    val auctioneerAuthority: PublicKey? = null, // Use Auctioneer ix when provided
    val mintAccount: PublicKey, // Required for checking Metadata
    val tokenAccount: PublicKey = PublicKey.associatedTokenAddress(seller, mintAccount).address, // Default: ATA
    val price: Long = auctioneerAuthority?.let { AUCTIONEER_PRICE } ?: 0, // Default: 0 SOLs or tokens, ignored in Auctioneer.
    val tokens: Long = 1, // Default: token(1)
    val bookkeeper: PublicKey = seller, // Default: identity
    val canceledAt: Long? = null
)

// TODO: handle Result
internal val Listing.metadata get() = MetadataAccount.pda(mintAccount).getOrThrows()

internal val Listing.sellerTradeState get() =
    auctionHouse.tradeStatePda(seller, mintAccount, price, tokens, tokenAccount)

internal val Listing.freeWalletTradeState get() =
    auctionHouse.tradeStatePda(seller, mintAccount, 0, tokens, tokenAccount)

internal val Listing.receiptAddress get() = AuctionHouse.listingReceiptPda(sellerTradeState.address)

internal fun Listing.buildTransaction(printReceipt: Boolean = true) = Transaction().apply {

    // TODO: create the Token Account for a public bid if it does not already exist
    //  see: https://github.com/metaplex-foundation/js/blob/1827aaea479106a966ba026fbc97fe4a78c240c3/packages/js/src/plugins/auctionHouseModule/createBid.ts#L271

    val programAsSigner = AuctionHouse.programAsSignerPda()

    addInstruction(auctioneerAuthority?.let {
        AuctionHouseInstructions.auctioneerSell(
            wallet = seller,
            metadata = metadata,
            authority = authority,
            auctioneerAuthority = auctioneerAuthority,
            auctionHouse = auctionHouse.address,
            auctionHouseFeeAccount = auctionHouse.auctionHouseFeeAccount,
            sellerTradeState = sellerTradeState.address,
            freeSellerTradeState = freeWalletTradeState.address,
            programAsSigner = programAsSigner.address,
            tokenAccount = tokenAccount,
            ahAuctioneerPda = auctionHouse.auctioneerPda(authority),
            tokenProgram = TokenProgram.PROGRAM_ID,
            systemProgram = SystemProgram.PROGRAM_ID,
            rent = Sysvar.SYSVAR_RENT_PUBKEY,
            tradeStateBump = sellerTradeState.nonce.toUByte(),
            freeTradeStateBump = freeWalletTradeState.nonce.toUByte(),
            programAsSignerBump = programAsSigner.nonce.toUByte(),
            tokenSize = tokens.toULong()
        )
    } ?: AuctionHouseInstructions.sell(
        wallet = seller,
        metadata = metadata,
        authority = authority,
        auctionHouse = auctionHouse.address,
        auctionHouseFeeAccount = auctionHouse.auctionHouseFeeAccount,
        sellerTradeState = sellerTradeState.address,
        freeSellerTradeState = freeWalletTradeState.address,
        programAsSigner = programAsSigner.address,
        tokenAccount = tokenAccount,
        tokenProgram = TokenProgram.PROGRAM_ID,
        systemProgram = SystemProgram.PROGRAM_ID,
        rent = Sysvar.SYSVAR_RENT_PUBKEY,
        tradeStateBump = sellerTradeState.nonce.toUByte(),
        freeTradeStateBump = freeWalletTradeState.nonce.toUByte(),
        programAsSignerBump = programAsSigner.nonce.toUByte(),
        buyerPrice = price.toULong(), tokenSize = tokens.toULong(),
    ))

    // TODO: Since printBidReceipt can't deserialize createAuctioneerBuyInstruction due
    //  to a bug, don't print Auctioneer Bid receipt for the time being.
    if (printReceipt && auctioneerAuthority == null) {

        val receipt = AuctionHouse.listingReceiptPda(sellerTradeState.address)

        addInstruction(
            AuctionHouseInstructions.printListingReceipt(
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