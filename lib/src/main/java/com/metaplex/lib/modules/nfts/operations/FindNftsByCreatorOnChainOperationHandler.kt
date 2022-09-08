package com.metaplex.lib.modules.nfts.operations

import com.metaplex.lib.ASYNC_CALLBACK_DEPRECATION_MESSAGE
import com.metaplex.lib.Metaplex
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.programs.token_metadata.TokenMetadataProgram
import com.metaplex.lib.shared.*
import com.solana.core.PublicKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class FindNftsByCreatorInput (
    val creator: PublicKey,
    val position: Int?
)

typealias FindNftsByCreatorOperation = OperationResult<FindNftsByCreatorInput, OperationError>

class FindNftsByCreatorOnChainOperationHandler(override val connection: Connection,
                                               override val dispatcher: CoroutineDispatcher = Dispatchers.IO)
    : OperationHandler<FindNftsByCreatorInput, List<NFT?>> {

    override var metaplex: Metaplex
        get() = maybeMetaplex ?: throw IllegalStateException(
            "Metaplex object was not injected, and dependency forwarding is obsolete and has been " +
                    "replaced with direct dependency injection")
        set(value) {
            maybeMetaplex = value
        }

    private var maybeMetaplex: Metaplex? = null

    constructor(metaplex: Metaplex) : this(metaplex.connection) { this.maybeMetaplex = metaplex}

    val metadataV1GpaBuilder = TokenMetadataProgram.metadataV1Accounts(this.connection)

    override suspend fun handle(input: FindNftsByCreatorInput): Result<List<NFT?>> =
        metadataV1GpaBuilder
            .selectMint()
            .whereCreator(input.position ?: 1, input.creator)
            .getSuspend()
            .getOrElse {
                return Result.failure(OperationError.GetFindNftsByCreatorOperation(it))
            }.mapNotNull {
                it.account.data?.publicKey
            }.let { publicKeys ->
                FindNftsByMintListOnChainOperationHandler(connection, dispatcher).handle(publicKeys)
            }

    @Deprecated(ASYNC_CALLBACK_DEPRECATION_MESSAGE, ReplaceWith("handle(input)"))
    override fun handle(operation: FindNftsByCreatorOperation): OperationResult<List<NFT?>, OperationError> =
        operation.flatMap { mintKeys ->
            OperationResult { cb ->
                CoroutineScope(dispatcher).launch {
                    handle(mintKeys)
                        .onSuccess { buffer ->
                            cb(ResultWithCustomError.success(buffer))
                        }.onFailure {
                            cb(ResultWithCustomError.failure(it as? OperationError
                                ?: OperationError.GetFindNftsByCreatorOperation(it)))
                        }
                }
            }
        }
}
