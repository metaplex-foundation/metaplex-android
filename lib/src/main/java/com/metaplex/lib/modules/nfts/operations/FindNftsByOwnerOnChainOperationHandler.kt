package com.metaplex.lib.modules.nfts.operations

import com.metaplex.lib.ASYNC_CALLBACK_DEPRECATION_MESSAGE
import com.metaplex.lib.Metaplex
import com.metaplex.lib.drivers.solana.AccountPublicKey
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.programs.token_metadata.gpa_builders.TokenGpaBuilder
import com.metaplex.lib.programs.tokens.TokenProgram
import com.metaplex.lib.shared.*
import com.solana.core.PublicKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

typealias FindNftsByOwnerOperation = OperationResult<PublicKey, OperationError>

class FindNftsByOwnerOnChainOperationHandler(override val connection: Connection,
                                             override val dispatcher: CoroutineDispatcher = Dispatchers.IO)
    : OperationHandler<PublicKey, List<NFT?>> {

    override var metaplex: Metaplex
        get() = maybeMetaplex ?: throw IllegalStateException(
            "Metaplex object was not injected, and dependency forwarding is obsolete and has been " +
                    "replaced with direct dependency injection")
        set(value) {
            maybeMetaplex = value
        }

    private var maybeMetaplex: Metaplex? = null

    constructor(metaplex: Metaplex) : this(metaplex.connection) { this.maybeMetaplex = metaplex}

    var tokenGpaBuilder: TokenGpaBuilder = TokenProgram.tokenAccounts(this.connection)

    override suspend fun handle(input: PublicKey): Result<List<NFT?>> =
        tokenGpaBuilder
            .selectMint()
            .whereOwner(input)
            .whereAmount(1)
            .getSuspend()
            .getOrElse {
                return Result.failure(OperationError.GetFindNftsByOwnerOperation(it))
            }.mapNotNull {
                it.account.data?.publicKey
            }.let { publicKeys ->
                FindNftsByMintListOnChainOperationHandler(connection, dispatcher).handle(publicKeys)
            }

    @Deprecated(ASYNC_CALLBACK_DEPRECATION_MESSAGE, ReplaceWith("handle(input)"))
    override fun handle(operation: FindNftsByOwnerOperation): OperationResult<List<NFT?>, OperationError> =
        operation.flatMap { mintKeys ->
            OperationResult { cb ->
                CoroutineScope(dispatcher).launch {
                    handle(mintKeys)
                        .onSuccess { buffer ->
                            cb(ResultWithCustomError.success(buffer))
                        }.onFailure {
                            cb(ResultWithCustomError.failure(it as? OperationError
                                ?: OperationError.GetFindNftsByOwnerOperation(it)))
                        }
                }
            }
        }
}