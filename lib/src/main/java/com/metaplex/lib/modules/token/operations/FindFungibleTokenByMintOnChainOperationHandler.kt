package com.metaplex.lib.modules.token.operations

import com.metaplex.lib.ASYNC_CALLBACK_DEPRECATION_MESSAGE
import com.metaplex.lib.Metaplex
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.modules.token.models.FungibleToken
import com.metaplex.lib.programs.token_metadata.accounts.MetadataAccount
import com.metaplex.lib.shared.*
import com.solana.core.PublicKey
import kotlinx.coroutines.*
import java.lang.RuntimeException

typealias FindFungibleTokenByMintOperation = OperationResult<PublicKey, OperationError>

class FindFungibleTokenByMintOnChainOperationHandler(override val connection: Connection,
                                                     override val dispatcher: CoroutineDispatcher = Dispatchers.IO)
    : OperationHandler<PublicKey, FungibleToken> {

    override var metaplex: Metaplex
        get() = maybeMetaplex ?: throw IllegalStateException(
            "Metaplex object was not injected, and dependency forwarding is obsolete and has been " +
                    "replaced with direct dependency injection")
        set(value) {
            maybeMetaplex = value
        }

    private var maybeMetaplex: Metaplex? = null

    constructor(metaplex: Metaplex) : this(metaplex.connection) { this.maybeMetaplex = metaplex}

    override suspend fun handle(input: PublicKey): Result<FungibleToken> = withContext(dispatcher) {
        FindTokenMetadataAccountOperation(connection).run(MetadataAccount.pda(input).getOrThrows())
            .mapCatching {
                it.data?.let { FungibleToken(it) } ?: throw OperationError.NilDataOnAccount
            }
    }

    @Deprecated(ASYNC_CALLBACK_DEPRECATION_MESSAGE, ReplaceWith("handle(input)"))
    override fun handle(operation: FindFungibleTokenByMintOperation): OperationResult<FungibleToken, OperationError> {
        return operation.flatMap { mintKey ->
            OperationResult { cb ->
                CoroutineScope(dispatcher).launch {
                    handle(mintKey)
                        .onSuccess { buffer ->
                            cb(ResultWithCustomError.success(buffer))
                        }.onFailure {
                            cb(ResultWithCustomError.failure(OperationError.GetMetadataAccountInfoError(RuntimeException(it))))
                        }
                }
            }
        }
    }
}