package com.metaplex.lib.shared

import com.metaplex.lib.ASYNC_CALLBACK_DEPRECATION_MESSAGE
import com.metaplex.lib.Metaplex
import com.metaplex.lib.drivers.solana.Connection
import kotlinx.coroutines.CoroutineDispatcher
import java.lang.Exception

sealed class OperationError: ResultError(){
    object NilDataOnAccount: OperationError()
    object CouldNotFindPDA: OperationError()
    data class GmaBuilderError(val exception: Throwable): OperationError()
    data class GetMasterEditionAccountInfoError(val exception: Throwable): OperationError()
    data class GetMetadataAccountInfoError(val exception: Throwable): OperationError()
    data class GetFindNftsByCreatorOperation(val exception: Throwable): OperationError()
    data class GetFindNftsByOwnerOperation(val exception: Throwable): OperationError()
}

interface OperationHandler<I,O> {
    val connection: Connection
    val dispatcher: CoroutineDispatcher
    suspend fun handle(input: I): Result<O>
}
