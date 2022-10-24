/*
 * FindAuctionHouseByAddressOperationHandler
 * Metaplex
 * 
 * Created by Funkatronics on 10/21/2022
 */

package com.metaplex.lib.modules.auctions.operations

import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.modules.auctions.models.AuctionHouse
import com.metaplex.lib.serialization.serializers.solana.AnchorAccountSerializer
import com.metaplex.lib.shared.*
import com.solana.core.PublicKey
import kotlinx.coroutines.*

import com.metaplex.lib.experimental.jen.auctionhouse.AuctionHouse as AuctionHouseAccount

class FindAuctionHouseByAddressOperation(override val connection: Connection)
    : SuspendOperation<PublicKey, AuctionHouse> {
    override suspend fun run(input: PublicKey): Result<AuctionHouse> =
        connection.getAccountInfo(AnchorAccountSerializer<AuctionHouseAccount>(), input)
            .map {
                it.data!!.let { // safe unwrap, successful result will not have null
                    AuctionHouse(
                        it.treasuryWithdrawalDestination,
                        it.feeWithdrawalDestination,
                        it.treasuryMint,
                        it.authority,
                        it.creator,
                        it.sellerFeeBasisPoints,
                        it.requiresSignOff,
                        it.canChangeSalePrice,
                        it.hasAuctioneer
                    )
                }
            }
}

class FindAuctionHouseByAddressOperationHandler(override val connection: Connection,
                                            override val dispatcher: CoroutineDispatcher = Dispatchers.IO)
    : OperationHandler<PublicKey, AuctionHouse> {
    override suspend fun handle(input: PublicKey): Result<AuctionHouse> = withContext(dispatcher) {
        FindAuctionHouseByAddressOperation(connection).run(input)
    }
}