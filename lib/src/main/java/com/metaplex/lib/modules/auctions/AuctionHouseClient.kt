/*
 * AuctionHouseClient
 * Metaplex
 * 
 * Created by Funkatronics on 8/11/2022
 */

package com.metaplex.lib.modules.auctions

import com.metaplex.lib.drivers.indenty.IdentityDriver
import com.metaplex.lib.experimental.jen.auctionhouse.AuctionHouseInstructions
import com.metaplex.lib.experimental.jen.auctionhouse.BidReceipt
import com.metaplex.lib.experimental.jen.auctionhouse.ListingReceipt
import com.metaplex.lib.modules.auctions.models.*
import com.metaplex.lib.solana.Connection
import com.solana.core.PublicKey
import com.solana.core.Sysvar
import com.solana.core.Transaction
import com.solana.programs.AssociatedTokenProgram
import com.solana.programs.SystemProgram
import com.solana.programs.TokenProgram

/**
 * NFT Auction House Client
 *
 * This object represents a client for a specific Auction House
 *
 * @author Funkatronics
 */
class AuctionHouseClient(val auctionHouse: AuctionHouse, val connectionDriver: Connection,
                         val identityDriver: IdentityDriver) {

    fun list(mint: PublicKey, price: Long): Transaction {

        if (auctionHouse.hasAuctioneer) throw Error("Auctioneer Authority Required")

        val tokens: Long = 1

        val seller = identityDriver.publicKey // TODO: add buyer input
        val authority = auctionHouse.authority // TODO: add authority input

        return CreateListingRequest(auctionHouse, seller, authority,
            mintAccount = mint, price = price, tokens = tokens).buildTransaction()
    }

    fun bid(mint: PublicKey, price: Long): Transaction {

        if (auctionHouse.hasAuctioneer) throw Error("Auctioneer Authority Required")

        val tokens: Long = 1

        val buyer = identityDriver.publicKey // TODO: add buyer input
        val authority = auctionHouse.authority // TODO: add authority input

        return CreateBidRequest(auctionHouse, mint, buyer, authority, price = price, tokens = tokens)
            .buildTransaction()
    }

    fun executeSale(listing: Listing, bid: Bid, auctioneerAuthority: PublicKey?,
                    bookkeeper: PublicKey = identityDriver.publicKey,
                    printReceipt: Boolean = true): Transaction {

        // TODO: need to handle these error states
//        if (!listing.auctionHouse.address.equals(bid.auctionHouse.address)) {
//            throw BidAndListingHaveDifferentAuctionHousesError()
//        }
//        if (!listing.asset.address.equals(bid.asset.address)) {
//            throw BidAndListingHaveDifferentMintsError()
//        }
//        if (bid.canceledAt) {
//            throw CanceledBidIsNotAllowedError()
//        }
//        if (listing.canceledAt) {
//            throw CanceledListingIsNotAllowedError()
//        }
//        if (auctionHouse.hasAuctioneer && !auctioneerAuthority) {
//            throw AuctioneerAuthorityRequiredError()
//        }

        val tokens: Long = 1

        val buyer = identityDriver.publicKey // TODO: add buyer input
        val authority = auctionHouse.authority // TODO: add authority input

        val escrowPayment = auctionHouse.buyerEscrowPda(buyer)

        val freeTradeState = auctionHouse.tradeStatePda(
            listing.seller, listing.asset.mintAddress, 0, tokens, listing.asset.tokenAccount
        )

        val programAsSigner = AuctionHouse.programAsSignerPda()

        val sellerPaymentReceiptAccount = if (auctionHouse.isNative) listing.seller
        else PublicKey.associatedTokenAddress(listing.seller, auctionHouse.treasuryMint).address

        val buyerReceiptTokenAccount =
            PublicKey.associatedTokenAddress(bid.buyer, listing.asset.mintAddress).address

        return Transaction().apply {
            addInstruction(auctioneerAuthority?.let {
                AuctionHouseInstructions.auctioneerExecuteSale(
                    buyer = bid.buyer, seller = listing.seller,
                    tokenAccount = listing.asset.tokenAccount,
                    tokenMint = listing.asset.mintAddress,
                    metadata = listing.asset.metadata,
                    treasuryMint = auctionHouse.treasuryMint,
                    escrowPaymentAccount = escrowPayment.address,
                    sellerPaymentReceiptAccount = sellerPaymentReceiptAccount,
                    buyerReceiptTokenAccount = buyerReceiptTokenAccount,
                    authority = auctionHouse.authority,
                    auctioneerAuthority = auctioneerAuthority,
                    auctionHouse = auctionHouse.address,
                    auctionHouseFeeAccount = auctionHouse.auctionHouseFeeAccount,
                    auctionHouseTreasury = auctionHouse.auctionHouseTreasury,
                    buyerTradeState = bid.tradeState,
                    sellerTradeState = listing.tradeState,
                    freeTradeState = freeTradeState.address,
                    ahAuctioneerPda = auctionHouse.auctioneerPda(auctioneerAuthority),
                    tokenProgram = TokenProgram.PROGRAM_ID,
                    systemProgram = SystemProgram.PROGRAM_ID,
                    ataProgram = AssociatedTokenProgram.SPL_ASSOCIATED_TOKEN_ACCOUNT_PROGRAM_ID,
                    programAsSigner = programAsSigner.address,
                    rent = Sysvar.SYSVAR_RENT_ADDRESS,
                    freeTradeStateBump = freeTradeState.nonce.toUByte(),
                    escrowPaymentBump = escrowPayment.nonce.toUByte(),
                    programAsSignerBump = programAsSigner.nonce.toUByte(),
                    buyerPrice = bid.price,
                    tokenSize = bid.tokenSize
                )
            } ?: AuctionHouseInstructions.executeSale(
                buyer = bid.buyer, seller = listing.seller,
                tokenAccount = listing.asset.tokenAccount,
                tokenMint = listing.asset.mintAddress,
                metadata = listing.asset.metadata,
                treasuryMint = auctionHouse.treasuryMint,
                escrowPaymentAccount = escrowPayment.address,
                sellerPaymentReceiptAccount = sellerPaymentReceiptAccount,
                buyerReceiptTokenAccount = buyerReceiptTokenAccount,
                authority = auctionHouse.authority,
                auctionHouse = auctionHouse.address,
                auctionHouseFeeAccount = auctionHouse.auctionHouseFeeAccount,
                auctionHouseTreasury = auctionHouse.auctionHouseTreasury,
                buyerTradeState = bid.tradeState,
                sellerTradeState = listing.tradeState,
                freeTradeState = freeTradeState.address,
                tokenProgram = TokenProgram.PROGRAM_ID,
                systemProgram = SystemProgram.PROGRAM_ID,
                ataProgram = AssociatedTokenProgram.SPL_ASSOCIATED_TOKEN_ACCOUNT_PROGRAM_ID,
                programAsSigner = programAsSigner.address,
                rent = Sysvar.SYSVAR_RENT_ADDRESS,
                freeTradeStateBump = freeTradeState.nonce.toUByte(),
                escrowPaymentBump = escrowPayment.nonce.toUByte(),
                programAsSignerBump = programAsSigner.nonce.toUByte(),
                buyerPrice = bid.price,
                tokenSize = bid.tokenSize
            ))

            if (printReceipt && listing.purchaseReceipt != null && bid.purchaseReceipt != null) {

                val purchaseReceipt =
                    AuctionHouse.purchaseReceiptPda(listing.tradeState, bid.tradeState)

                addInstruction(
                    AuctionHouseInstructions.printPurchaseReceipt(
                        purchaseReceipt = purchaseReceipt.address,
                        listingReceipt = listing.purchaseReceipt!!,
                        bidReceipt = bid.purchaseReceipt!!,
                        bookkeeper = bookkeeper,
                        systemProgram = SystemProgram.PROGRAM_ID,
                        rent = Sysvar.SYSVAR_RENT_ADDRESS,
                        instruction = PublicKey(SYSVAR_INSTRUCTIONS_PUBKEY),
                        purchaseReceiptBump = purchaseReceipt.nonce.toUByte()
                    )
                )
            }
        }
    }

    fun cancelListing(listing: ListingReceipt, mint: PublicKey, authority: PublicKey? = null) =
        cancel(
            listing.seller, mint, listing.tradeState, listing.price,
            listing.tokenSize, listing.purchaseReceipt, authority
        )

    fun cancelBid(mint: PublicKey, bid: BidReceipt, authority: PublicKey? = null) = cancel(
        bid.buyer, mint, bid.tradeState, bid.price, bid.tokenSize, bid.purchaseReceipt, authority
    )

    private fun cancel(wallet: PublicKey, mint: PublicKey, tradeState: PublicKey,
                       price: ULong, tokenSize: ULong, purchaseReceipt: PublicKey? = null,
                       authority: PublicKey? = null): Transaction {

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
                        buyerPrice = price,
                        tokenSize = tokenSize
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
                    buyerPrice = price,
                    tokenSize = tokenSize
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
}

// TODO: should move this stuff to appropriate places
// Auctioneer uses "u64::MAX" for the price which is "2^64 − 1".
const val AUCTIONEER_PRICE: Long = -1
const val SYSVAR_INSTRUCTIONS_PUBKEY = "Sysvar1nstructions1111111111111111111111111"

// cherry picked from SolanaKT
fun PublicKey.Companion.associatedTokenAddress(walletAddress: PublicKey,
                                               tokenMintAddress: PublicKey)
: PublicKey.ProgramDerivedAddress =
    findProgramAddress(
        listOf(
            walletAddress.toByteArray(),
            TokenProgram.PROGRAM_ID.toByteArray(),
            tokenMintAddress.toByteArray()
        ),
        AssociatedTokenProgram.SPL_ASSOCIATED_TOKEN_ACCOUNT_PROGRAM_ID
    )