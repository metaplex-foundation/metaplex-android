package com.metaplex.lib.modules.nfts.operations

import com.metaplex.lib.ASYNC_CALLBACK_DEPRECATION_MESSAGE
import com.metaplex.lib.Metaplex
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.modules.nfts.models.MetaplexContstants
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.shared.*
import com.solana.core.PublicKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bitcoinj.core.Base58
import java.lang.RuntimeException

data class FindNftsByCandyMachineInput(
    val candyMachine : PublicKey,
    val version : UInt?,
)

typealias FindNftsByCandyMachineOperation = OperationResult<FindNftsByCandyMachineInput, OperationError>

val candyMachineId = PublicKey("cndy3Z4yapfJBmL3ShUp5exZKqR3z33thTzeNMm2gRZ")
class FindNftsByCandyMachineOnChainOperationHandler(override val connection: Connection,
                                                    override val dispatcher: CoroutineDispatcher = Dispatchers.IO)
    : OperationHandler<FindNftsByCandyMachineInput, List<NFT?>> {

    override var metaplex: Metaplex
        get() = maybeMetaplex ?: throw IllegalStateException(
            "Metaplex object was not injected, and dependency forwarding is obsolete and has been " +
                    "replaced with direct dependency injection")
        set(value) {
            maybeMetaplex = value
        }

    private var maybeMetaplex: Metaplex? = null

    constructor(metaplex: Metaplex) : this(metaplex.connection) { this.maybeMetaplex = metaplex}

    override suspend fun handle(input: FindNftsByCandyMachineInput): Result<List<NFT?>> {
        val cmAddress = if((input.version ?: 2) == 2){
            val pdaSeeds = listOf(
                "candy_machine".toByteArray(),
                candyMachineId.toByteArray(),
            )
            val pdaAddres = PublicKey.findProgramAddress(
                pdaSeeds,
                candyMachineId
            )
            pdaAddres.address
        } else input.candyMachine

        return FindNftsByCreatorOnChainOperationHandler(connection, dispatcher)
            .handle(FindNftsByCreatorInput(cmAddress, 1))
    }

    @Deprecated(ASYNC_CALLBACK_DEPRECATION_MESSAGE, ReplaceWith("handle(input)"))
    override fun handle(operation: FindNftsByCandyMachineOperation): OperationResult<List<NFT?>, OperationError> {
        return operation.flatMap { mintKey ->
            OperationResult { cb ->
                CoroutineScope(dispatcher).launch {
                    handle(mintKey)
                        .onSuccess { buffer ->
                            cb(ResultWithCustomError.success(buffer))
                        }.onFailure {
                            cb(ResultWithCustomError.failure(it as OperationError))
                        }
                }
            }
        }
    }
}