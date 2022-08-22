package com.metaplex.lib.modules.fungibletokens.operations

import com.metaplex.lib.Metaplex
import com.metaplex.lib.modules.fungibletokens.models.FungibleToken
import com.metaplex.lib.programs.token_metadata.accounts.MetadataAccount
import com.metaplex.lib.shared.*
import com.solana.core.PublicKey
import com.solana.models.buffer.BufferInfo
import java.lang.RuntimeException

typealias FindFungibleTokenByMintOperation = OperationResult<PublicKey, OperationError>

class FindFungibleTokenByMintOnChainOperationHandler(override var metaplex: Metaplex): OperationHandler<PublicKey, FungibleToken> {
    override fun handle(operation: FindFungibleTokenByMintOperation): OperationResult<FungibleToken, OperationError> {
        val bufferInfoResult = operation.flatMap { it ->
            val metadataAccount = OperationResult.pure(MetadataAccount.pda(it)).flatMap {
                OperationResult<BufferInfo<MetadataAccount>, OperationError> { cb ->
                    this.metaplex.getAccountInfo(it, MetadataAccount::class.java) { result ->
                        result.onSuccess { buffer ->
                            cb(ResultWithCustomError.success(buffer))
                        }.onFailure {
                            cb(ResultWithCustomError.failure(OperationError.GetMetadataAccountInfoError(RuntimeException(it))))
                        }
                    }
                }
            }
            metadataAccount
        }
        return bufferInfoResult.flatMap {
            val metadataAccount = it.data?.value
            if(metadataAccount != null){
                OperationResult.success(FungibleToken(metadataAccount))
            } else {
                OperationResult.failure(OperationError.NilDataOnAccount)
            }
        }
    }
}