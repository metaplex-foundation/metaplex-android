/*
 * AuctionsClient
 * Metaplex
 * 
 * Created by Funkatronics on 7/27/2022
 */

package com.metaplex.lib.modules.auctions

import com.metaplex.lib.drivers.indenty.IdentityDriver
import com.metaplex.lib.drivers.solana.getAccountInfo
import com.metaplex.lib.modules.auctions.models.AuctionHouse
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.drivers.solana.TransactionOptions
import com.metaplex.lib.experimental.jen.auctionhouse.BidReceipt
import com.metaplex.lib.experimental.jen.auctionhouse.ListingReceipt
import com.metaplex.lib.extensions.signSendAndConfirm
import com.metaplex.lib.modules.auctions.builders.CreateAuctionHouseTransactionBuilder
import com.metaplex.lib.modules.auctions.models.Bid
import com.metaplex.lib.modules.auctions.models.Listing
import com.metaplex.lib.modules.auctions.operations.FindAuctionHouseByAddressOperation
import com.metaplex.lib.modules.token.WRAPPED_SOL_MINT_ADDRESS
import com.metaplex.lib.modules.token.operations.FindTokenMetadataAccountOperation
import com.solana.core.PublicKey
import kotlinx.coroutines.*

/**
 * NFT Auctions Client
 *
 * @author Funkatronics
 */
class AuctionsClient(val connection: Connection, val signer: IdentityDriver,
                     private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
                     private val txOptions: TransactionOptions = connection.transactionOptions) {

    /**
     * Attempts to find an AuctionHouse account on chain via its [address]
     */
    suspend fun findAuctionHouseByAddress(address: PublicKey): Result<AuctionHouse> =
        withContext(dispatcher) {
            FindAuctionHouseByAddressOperation(connection).run(address)
        }

    /**
     * Attempts to find an AuctionHouse account on chain via its [creator] address and
     * treasury [mint] address
     */
    suspend fun findAuctionHouseByCreatorAndMint(creator: PublicKey,
                                                 treasuryMint: PublicKey): Result<AuctionHouse> =
        findAuctionHouseByAddress(AuctionHouse.pda(creator, treasuryMint).address)

    /**
     * Attempts to find a Listing account on chain via its receipt [address]
     */
    suspend fun findListingByReceipt(address: PublicKey): Result<Listing> {
        connection.apply {
            return getAccountInfo<ListingReceipt>(address).map {
                it.data!!.let { receipt ->// safe unwrap, successful result will not have null
                    val auctionHouse = findAuctionHouseByAddress(receipt.auctionHouse).getOrThrow()
                    val metadata = FindTokenMetadataAccountOperation(connection)
                        .run(receipt.metadata).getOrThrow().data!!

                    Listing(auctionHouse,
                        receipt.seller,
                        auctionHouse.authority,
                        null,
                        metadata.mint,
                        PublicKey.associatedTokenAddress(receipt.seller, metadata.mint).address,
                        receipt.price.toLong(),
                        receipt.tokenSize.toLong(),
                        receipt.bookkeeper,
                        receipt.canceledAt
                    )
                }
            }
        }
    }

    /**
     * Attempts to find a Listing account on chain via its receipt [address]
     */
    suspend fun findBidByReceipt(address: PublicKey): Result<Bid> {
        connection.apply {
            return getAccountInfo<BidReceipt>(address).map {
                it.data!!.let { receipt ->// safe unwrap, successful result will not have null
                    val auctionHouse = findAuctionHouseByAddress(receipt.auctionHouse).getOrThrow()
                    val metadata = FindTokenMetadataAccountOperation(connection)
                        .run(receipt.metadata).getOrThrow().data!!

                    Bid(auctionHouse,
                        metadata.mint,
                        receipt.buyer,
                        auctionHouse.authority,
                        null,
                        null,
                        receipt.price.toLong(),
                        receipt.tokenSize.toLong(),
                        receipt.bookkeeper,
                        receipt.canceledAt
                    )
                }
            }
        }
    }

    /**
     * Async-callback version of [findAuctionHouseByAddress]
     */
    fun findAuctionHouseByAddress(address: PublicKey, onComplete: (Result<AuctionHouse>) -> Unit) =
        CoroutineScope(Dispatchers.IO).launch {
            onComplete(findAuctionHouseByAddress(address))
        }

    /**
     * Async-callback version of [findAuctionHouseByCreatorAndMint]
     */
    fun findAuctionHouseByCreatorAndMint(creator: PublicKey, mint: PublicKey,
                                         onComplete: (Result<AuctionHouse>) -> Unit) =
        CoroutineScope(Dispatchers.IO).launch {
            onComplete(findAuctionHouseByCreatorAndMint(creator, mint))
        }

    suspend fun createAuctionHouse(
        sellerFeeBasisPoints: Int,
        canChangeSalePrice: Boolean = false,
        requireSignOff: Boolean = canChangeSalePrice,
        treasuryMint: PublicKey = PublicKey(WRAPPED_SOL_MINT_ADDRESS),
        authority: PublicKey = signer.publicKey,
        transactionOptions: TransactionOptions = txOptions
    ): Result<AuctionHouse> {

        AuctionHouse(
            treasuryWithdrawalDestinationOwner = signer.publicKey,
            feeWithdrawalDestination = signer.publicKey,
            treasuryMint = treasuryMint,
            authority = authority,
            creator = signer.publicKey,
            sellerFeeBasisPoints = sellerFeeBasisPoints.toUShort(),
            requiresSignOff = requireSignOff,
            canChangeSalePrice = canChangeSalePrice
        ).apply {
            CreateAuctionHouseTransactionBuilder(this, signer.publicKey, connection, dispatcher)
                .build()
                .getOrThrow()
                .signSendAndConfirm(connection, signer, listOf(), transactionOptions)

            return Result.success(this)
        }
    }
}