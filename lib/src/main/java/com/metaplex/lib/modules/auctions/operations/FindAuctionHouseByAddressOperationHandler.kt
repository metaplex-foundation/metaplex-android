/*
 * FindAuctionHouseByAddressOperationHandler
 * Metaplex
 * 
 * Created by Funkatronics on 10/21/2022
 */

package com.metaplex.lib.modules.auctions.operations

import com.metaplex.lib.ASYNC_CALLBACK_DEPRECATION_MESSAGE
import com.metaplex.lib.Metaplex
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.drivers.solana.getAccountInfo
import com.metaplex.lib.modules.auctions.models.AuctionHouse
import com.metaplex.lib.shared.*
import com.solana.core.PublicKey
import kotlinx.coroutines.*

class FindAuctionHouseByAddressOperation(override val connection: Connection)
    : SuspendOperation<PublicKey, AuctionHouse> {
    override suspend fun run(input: PublicKey): Result<AuctionHouse> =
        connection.getAccountInfo<com.metaplex.lib.experimental.jen.auctionhouse.AuctionHouse>(input)
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

    override var metaplex: Metaplex
        get() = maybeMetaplex ?: throw IllegalStateException(
            "Metaplex object was not injected, and dependency forwarding is obsolete and has been " +
                    "replaced with direct dependency injection")
        set(value) {
            maybeMetaplex = value
        }

    private var maybeMetaplex: Metaplex? = null

    constructor(metaplex: Metaplex) : this(metaplex.connection) { this.maybeMetaplex = metaplex}

    override suspend fun handle(input: PublicKey): Result<AuctionHouse> = withContext(dispatcher) {
        FindAuctionHouseByAddressOperation(connection).run(input)
    }

    @Deprecated(ASYNC_CALLBACK_DEPRECATION_MESSAGE, ReplaceWith("handle(input)"))
    override fun handle(operation: OperationResult<PublicKey, OperationError>)
    : OperationResult<AuctionHouse, OperationError> =
        operation.flatMap { mintKey ->
            OperationResult { cb ->
                CoroutineScope(dispatcher).launch {
                    handle(mintKey)
                        .onSuccess { buffer ->
                            cb(ResultWithCustomError.success(buffer))
                        }.onFailure {
                            cb(
                                ResultWithCustomError.failure(
                                OperationError.GetMetadataAccountInfoError(java.lang.RuntimeException(it))))
                        }
                }
            }
        }
}