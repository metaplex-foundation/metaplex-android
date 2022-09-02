package com.metaplex.lib.modules.nfts.operations

import com.metaplex.lib.ASYNC_CALLBACK_DEPRECATION_MESSAGE
import com.metaplex.lib.Metaplex
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.experimental.serialization.serializers.legacy.BorshCodeableSerializer
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.programs.token_metadata.accounts.MetadataAccount
import com.metaplex.lib.shared.*
import com.solana.core.PublicKey
import kotlinx.coroutines.*
import java.lang.RuntimeException

typealias FindNftsByMintListOperation = OperationResult<List<PublicKey>, OperationError>

class FindNftsByMintListOnChainOperationHandler(override val connection: Connection,
                                                override val dispatcher: CoroutineDispatcher = Dispatchers.IO)
    : OperationHandler<List<PublicKey>, List<NFT?>> {

    override var metaplex: Metaplex
        get() = maybeMetaplex ?: throw IllegalStateException(
            "Metaplex object was not injected, and dependency forwarding is obsolete and has been " +
                    "replaced with direct dependency injection")
        set(value) {
            maybeMetaplex = value
        }

    private var maybeMetaplex: Metaplex? = null

    constructor(metaplex: Metaplex) : this(metaplex.connection) { this.maybeMetaplex = metaplex}

    // Rather than refactoring GmaBuilder to use coroutines, I just pulled the required logic
    // out and implemented it here. In the future we can refactor GmaBuilder if needed
    private val chunkSize = 100

    override suspend fun handle(input: List<PublicKey>): Result<List<NFT?>> =
        Result.success(input.map {
            MetadataAccount.pda(it).getOrDefault(null)
                ?: return Result.failure(OperationError.CouldNotFindPDA)
        }.chunked(chunkSize).map { chunk ->
            // TODO: how can I parallelize this?
            gma(chunk).getOrElse {
                return Result.failure(OperationError.GmaBuilderError(it))
            }
        }.flatten().map { account ->
            if (account.exists && account.metadata != null)
                NFT(account.metadata, null)
            else null
        })

    private suspend fun gma(publicKeys: List<PublicKey>): Result<List<MaybeAccountInfoWithPublicKey>> =
        connection.getMultipleAccountsInfo(
            BorshCodeableSerializer(MetadataAccount::class.java), publicKeys).map {
                publicKeys.zip(it) { publicKey, account ->
                    val metadata = account?.data as? MetadataAccount
                    MaybeAccountInfoWithPublicKey(publicKey, metadata != null, metadata)
                }
        }

    @Deprecated(ASYNC_CALLBACK_DEPRECATION_MESSAGE, ReplaceWith("handle(input)"))
    override fun handle(operation: FindNftsByMintListOperation): OperationResult<List<NFT?>, OperationError> =
        operation.flatMap { mintKeys ->
            OperationResult { cb ->
                CoroutineScope(dispatcher).launch {
                    handle(mintKeys)
                        .onSuccess { buffer ->
                            cb(ResultWithCustomError.success(buffer))
                        }.onFailure {
                            cb(ResultWithCustomError.failure(it as? OperationError
                                ?: OperationError.GmaBuilderError(it)))
                        }
                }
            }
        }
}