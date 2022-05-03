package com.metaplex.lib.shared

import com.metaplex.lib.Metaplex
import java.lang.Exception

sealed class OperationError: ResultError(){
    object NilDataOnAccount: OperationError()
    object CouldNotFindPDA: OperationError()
    data class GmaBuilderError(val exception: Exception): OperationError()
    data class GetMasterEditionAccountInfoError(val exception: Exception): OperationError()
    data class GetMetadataAccountInfoError(val exception: Exception): OperationError()
    data class GetFindNftsByCreatorOperation(val exception: Exception): OperationError()
    data class GetFindNftsByOwnerOperation(val exception: Exception): OperationError()
}

interface OperationHandler<I,O> {
    var metaplex: Metaplex
    fun handle(operation: OperationResult<I, OperationError>): OperationResult<O, OperationError>
}
