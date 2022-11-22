/*
 * AuctionsClient
 * Metaplex
 * 
 * Created by Funkatronics on 7/27/2022
 */

package com.metaplex.lib.modules.auctions

import com.metaplex.lib.drivers.indenty.IdentityDriver
import com.metaplex.lib.modules.auctions.models.AuctionHouse
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.drivers.solana.TransactionOptions
import com.metaplex.lib.experimental.jen.auctionhouse.BidReceipt
import com.metaplex.lib.experimental.jen.auctionhouse.ListingReceipt
import com.metaplex.lib.extensions.signSendAndConfirm
import com.metaplex.lib.modules.auctions.builders.CreateAuctionHouseTransactionBuilder
import com.metaplex.lib.modules.auctions.models.Bid
import com.metaplex.lib.modules.auctions.models.Listing
import com.metaplex.lib.modules.auctions.operations.FindAuctionHouseByAddressOperationHandler
import com.metaplex.lib.modules.auctions.operations.FindBidByReceiptAddressOperationHandler
import com.metaplex.lib.modules.auctions.operations.FindListingByReceiptAddressOperationHandler
import com.metaplex.lib.modules.token.WRAPPED_SOL_MINT_ADDRESS
import com.metaplex.lib.modules.token.operations.FindTokenMetadataAccountOperation
import com.metaplex.lib.serialization.serializers.solana.AnchorAccountSerializer
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
            FindAuctionHouseByAddressOperationHandler(connection, dispatcher).handle(address)

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
    suspend fun findListingByReceipt(address: PublicKey): Result<Listing> =
        FindListingByReceiptAddressOperationHandler(connection, dispatcher).handle(address)

    /**
     * Attempts to find a Listing account on chain via its receipt [address]
     */
    suspend fun findBidByReceipt(address: PublicKey): Result<Bid> =
        FindBidByReceiptAddressOperationHandler(connection, dispatcher).handle(address)

    /**
     * Creates a new Auction House instance on chain with the supplied parameters
     */
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