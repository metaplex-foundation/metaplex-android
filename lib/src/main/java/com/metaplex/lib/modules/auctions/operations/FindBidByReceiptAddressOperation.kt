/*
 * FindBidByReceiptAddressOperation
 * Metaplex
 * 
 * Created by Funkatronics on 10/24/2022
 */

package com.metaplex.lib.modules.auctions.operations

import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.experimental.jen.auctionhouse.BidReceipt
import com.metaplex.lib.modules.auctions.models.Bid
import com.metaplex.lib.modules.token.operations.FindTokenMetadataAccountOperation
import com.metaplex.lib.serialization.serializers.solana.AnchorAccountSerializer
import com.metaplex.lib.shared.*
import com.solana.core.PublicKey
import kotlinx.coroutines.*

class FindBidReceiptByAddressOperation(override val connection: Connection)
    : SuspendOperation<PublicKey, BidReceipt> {
    override suspend fun run(input: PublicKey): Result<BidReceipt> =
        connection.getAccountInfo(AnchorAccountSerializer<BidReceipt>(), input).map {
            it.data!! // safe unwrap, successful result will not have null
        }
}

class FindBidByReceiptAddressOperationHandler(override val connection: Connection,
                                              override val dispatcher: CoroutineDispatcher = Dispatchers.IO)
    : OperationHandler<PublicKey, Bid> {
    override suspend fun handle(input: PublicKey): Result<Bid> = withContext(dispatcher) {
        FindBidReceiptByAddressOperation(connection).run(input).map { receipt ->

            val auctionHouse = FindAuctionHouseByAddressOperation(connection)
                .run(receipt.auctionHouse).getOrThrow()

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