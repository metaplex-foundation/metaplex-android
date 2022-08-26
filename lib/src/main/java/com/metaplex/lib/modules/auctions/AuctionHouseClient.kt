/*
 * AuctionHouseClient
 * Metaplex
 * 
 * Created by Funkatronics on 8/11/2022
 */

package com.metaplex.lib.modules.auctions

import com.metaplex.lib.drivers.indenty.IdentityDriver
import com.metaplex.lib.drivers.solana.ConnectionKt
import com.metaplex.lib.modules.auctions.models.*
import com.solana.core.PublicKey
import com.solana.core.Transaction
import com.solana.programs.AssociatedTokenProgram
import com.solana.programs.TokenProgram
import kotlin.coroutines.suspendCoroutine

/**
 * NFT Auction House Client
 *
 * This object represents a client for a specific Auction House
 *
 * @author Funkatronics
 */
class AuctionHouseClient(val auctionHouse: AuctionHouse, val connectionDriver: ConnectionKt,
                         // dog_using_computer.jpg
                         // let the caller figure out signing, or pass in all signer accounts explicitly?
                         val signer: IdentityDriver) {

    suspend fun list(mint: PublicKey, price: Long, printReceipt: Boolean = true): Result<Listing> {

        if (auctionHouse.hasAuctioneer) throw Error("Auctioneer Authority Required")

        val tokens: Long = 1

        val seller = signer.publicKey // TODO: add buyer input
        val authority = auctionHouse.authority // TODO: add authority input

        Listing(auctionHouse, seller, authority,
            mintAccount = mint, price = price, tokens = tokens).apply {

            buildTransaction(printReceipt).signAndSend().getOrElse {
                return Result.failure(it) // we cant proceed further, return the error
            }

            return Result.success(this)
        }
    }

    suspend fun bid(mint: PublicKey, price: Long, printReceipt: Boolean = true): Result<Bid> {

        if (auctionHouse.hasAuctioneer) throw Error("Auctioneer Authority Required")

        val tokens: Long = 1

        val buyer = signer.publicKey // TODO: add buyer input
        val authority = auctionHouse.authority // TODO: add authority input

        Bid(auctionHouse, mint, buyer, authority, price = price, tokens = tokens).apply {

            buildTransaction(printReceipt).signAndSend().getOrElse {
                return Result.failure(it) // we cant proceed further, return the error
            }

            return Result.failure(Error("WTF"))
        }
    }

    suspend fun executeSale(asset: Asset, listing: Listing, bid: Bid, auctioneerAuthority: PublicKey?,
                    bookkeeper: PublicKey = signer.publicKey,
                    printReceipt: Boolean = true): Result<Purchase> {

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

//        val tokens: Long = 1
//
//        val buyer = signer.publicKey // TODO: add buyer input
//        val authority = auctionHouse.authority // TODO: add authority input

        Purchase(auctionHouse, bookkeeper, bid.buyer, listing.seller, asset, auctioneerAuthority,
            bid.buyerTradeState.address, listing.sellerTradeState.address, bid.price, bid.tokens).apply {

            buildTransaction(printReceipt).signAndSend().getOrElse {
                return Result.failure(it) // we cant proceed further, return the error
            }

            return Result.success(this)
        }
    }

    suspend fun cancelListing(listing: Listing, mint: PublicKey,
                              authority: PublicKey? = null): Result<String> =
        buildAuctionCancelInstruction(auctionHouse,
            listing.seller, mint, listing.sellerTradeState.address,
            listing.price, listing.tokens, listing.receiptAddress.address, authority
        ).signAndSend()

    suspend fun cancelBid(mint: PublicKey, bid: Bid, authority: PublicKey? = null): Result<String> =
        buildAuctionCancelInstruction(auctionHouse,
            bid.buyer, mint, bid.buyerTradeState.address,
            bid.price, bid.tokens, bid.receiptAddress.address, authority
        ).signAndSend()

    private suspend fun Transaction.signAndSend(): Result<String> {

        setRecentBlockHash(connectionDriver.getRecentBlockhash().getOrElse {
            return Result.failure(it) // we cant proceed further, return the error
        })

        // TODO: refactor identity driver to use coroutines?
        return Result.success(
            suspendCoroutine { continuation ->
                signer.signTransaction(this) { result ->
                    result.onSuccess { signedTx ->

                        // TODO: I think I would prefer to handle the send here rather than
                        //  delegating to the identity driver, but #we'llgetthere
                        signer.sendTransaction(signedTx) { continuation.resumeWith(it) }
                    }.onFailure { continuation.resumeWith(Result.failure(it)) }
                }
        })
    }
}

// TODO: should move this stuff to appropriate places
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