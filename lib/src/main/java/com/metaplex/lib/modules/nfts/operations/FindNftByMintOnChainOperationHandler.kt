package com.metaplex.lib.modules.nfts.operations

import com.metaplex.lib.ASYNC_CALLBACK_DEPRECATION_MESSAGE
import com.metaplex.lib.Metaplex
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.modules.token.operations.FindTokenMasterEditionAccountOperation
import com.metaplex.lib.modules.token.operations.FindTokenMetadataAccountOperation
import com.metaplex.lib.shared.*
import com.solana.core.PublicKey
import kotlinx.coroutines.*
import java.lang.RuntimeException

typealias FindNftByMintOperation = OperationResult<PublicKey, OperationError>

class FindNftByMintOnChainOperationHandler(override val connection: Connection,
                                           override val dispatcher: CoroutineDispatcher = Dispatchers.IO)
    : OperationHandler<PublicKey, NFT> {

    constructor(metaplex: Metaplex) : this(metaplex.connection)

    override suspend fun handle(input: PublicKey): Result<NFT> = withContext(dispatcher) {

        // Launch the metadata job asynchronously
        val metadataJob = async {
            FindTokenMetadataAccountOperation(connection)
                .run(input).getOrElse {
                    throw OperationError.GetMetadataAccountInfoError(it)
                }.data
        }

        val masterEditionAccount = FindTokenMasterEditionAccountOperation(connection)
            .run(input).getOrElse {
                throw OperationError.GetMasterEditionAccountInfoError(it)
            }.data

        val metadataAccount = metadataJob.await()

        if (metadataAccount != null && masterEditionAccount != null) {
            Result.success(NFT(metadataAccount, masterEditionAccount))
        } else {
            Result.failure(OperationError.NilDataOnAccount)
        }
    }

    @Deprecated(ASYNC_CALLBACK_DEPRECATION_MESSAGE, ReplaceWith("handle(input)"))
    override fun handle(operation: FindNftByMintOperation): OperationResult<NFT, OperationError> =
        operation.flatMap { mintKey ->
            OperationResult { cb ->
                CoroutineScope(dispatcher).launch {
                    handle(mintKey)
                        .onSuccess { buffer ->
                            cb(ResultWithCustomError.success(buffer))
                        }.onFailure {
                            cb(ResultWithCustomError.failure(
                                OperationError.GetMetadataAccountInfoError(RuntimeException(it))))
                        }
                }
            }
        }
}