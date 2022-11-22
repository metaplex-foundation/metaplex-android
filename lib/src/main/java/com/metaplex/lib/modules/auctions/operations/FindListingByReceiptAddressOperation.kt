/*
 * FindListingByReceiptAddressOperation
 * Metaplex
 * 
 * Created by Funkatronics on 10/24/2022
 */

package com.metaplex.lib.modules.auctions.operations

import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.experimental.jen.auctionhouse.ListingReceipt
import com.metaplex.lib.modules.auctions.models.Listing
import com.metaplex.lib.modules.token.operations.FindTokenMetadataAccountOperation
import com.metaplex.lib.serialization.serializers.solana.AnchorAccountSerializer
import com.metaplex.lib.shared.*
import com.solana.core.PublicKey
import kotlinx.coroutines.*

class FindListingReceiptByAddressOperation(override val connection: Connection)
    : SuspendOperation<PublicKey, ListingReceipt> {
    override suspend fun run(input: PublicKey): Result<ListingReceipt> =
        connection.getAccountInfo(AnchorAccountSerializer<ListingReceipt>(), input).map {
            it.data!! // safe unwrap, successful result will not have null
        }
}

class FindListingByReceiptAddressOperationHandler(override val connection: Connection,
                                                  override val dispatcher: CoroutineDispatcher = Dispatchers.IO)
    : OperationHandler<PublicKey, Listing> {
    override suspend fun handle(input: PublicKey): Result<Listing> = withContext(dispatcher) {
        FindListingReceiptByAddressOperation(connection).run(input).map { receipt ->

            val auctionHouse = FindAuctionHouseByAddressOperation(connection)
                .run(receipt.auctionHouse).getOrThrow()

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