package com.metaplex.lib.shared

import com.metaplex.lib.programs.token_metadata.accounts.MetadataAccount
import com.metaplex.lib.solana.Connection
import com.solana.core.PublicKey
import com.solana.models.buffer.BufferInfo
import kotlinx.coroutines.*
import java.lang.RuntimeException

data class GmaBuilderOptions(
    val chunkSize: Int?,
)

data class MaybeAccountInfoWithPublicKey(
    val pubkey: PublicKey,
    val exists: Boolean,
    val metadata: MetadataAccount?,
)


class GmaBuilder(
    private val connection: Connection,
    private var publicKeys: List<PublicKey>,
    options: GmaBuilderOptions?
) {
    private val chunkSize: Int = options?.chunkSize ?: 100

    fun setPublicKeys(publicKeys: List<PublicKey>): GmaBuilder {
        val newPublicKeys = this.publicKeys.toMutableList()
        newPublicKeys.addAll(publicKeys)
        this.publicKeys = newPublicKeys
        return this
    }

    fun get(): OperationResult<List<MaybeAccountInfoWithPublicKey>, Exception> {
        return this.getChunks(publicKeys)
    }

    private fun getChunks(publicKeys: List<PublicKey>): OperationResult<List<MaybeAccountInfoWithPublicKey>, Exception> {
        return OperationResult { cb ->
            val chunks = publicKeys.chunked(this.chunkSize)
            val chunkOperations = chunks.map { this.getChunk(it) }
            CoroutineScope(Dispatchers.IO).launch {
                processChuncks(chunkOperations){ result ->
                    result.onSuccess {
                        cb(ResultWithCustomError.success(it))
                    }.onFailure {
                        cb(ResultWithCustomError.failure(RuntimeException(it)))
                    }
                }
            }
        }
    }

    private suspend fun <T>processChuncks(chunkOperations: List<OperationResult<List<T>, Exception>>, cb: (Result<List<T>>) -> Unit) {
        val results = mutableListOf<T>()
        runBlocking {
            for (chunks in chunkOperations) {
                launch {
                    val job = async {
                        chunks.run { result ->
                            result.onSuccess {
                                results.addAll(it)
                            }.onFailure {
                                cb(Result.failure(RuntimeException(it)))
                            }
                        }
                    }
                    job.await()
                }
            }
        }
        cb(Result.success(results))
    }

    private fun getChunk(publicKeys: List<PublicKey>): OperationResult<List<MaybeAccountInfoWithPublicKey>, Exception> {
        return OperationResult<List<BufferInfo<MetadataAccount>>, ResultError> { cb ->
            this.connection.getMultipleAccountsInfo(publicKeys, MetadataAccount::class.java) {
                it.onSuccess { list ->
                    cb(ResultWithCustomError.success(list))
                }.onFailure { error ->
                    cb(ResultWithCustomError.failure(ResultError(error)))
                }
            }
        }.flatMap { accounts ->
            val maybeAccounts = mutableListOf<MaybeAccountInfoWithPublicKey>()
            publicKeys.zip(accounts).forEach { pair ->
                val publicKey = pair.first
                val account = pair.second
                account.data?.value?.let {
                    maybeAccounts.add(MaybeAccountInfoWithPublicKey(publicKey, true, it))
                } ?: run {
                    maybeAccounts.add(MaybeAccountInfoWithPublicKey(publicKey, false, null))
                }
            }
            OperationResult.success(maybeAccounts)
        }
    }
}