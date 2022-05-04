package com.metaplex.lib.modules.nfts.operations

import com.metaplex.lib.Metaplex
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.programs.token_metadata.MasterEditionAccount
import com.metaplex.lib.programs.token_metadata.MetadataAccount
import com.metaplex.lib.shared.*
import com.solana.core.PublicKey
import com.solana.models.buffer.BufferInfo
import java.lang.RuntimeException

typealias FindNftByMintOperation = OperationResult<PublicKey, OperationError>

class FindNftByMintOnChainOperationHandler(override var metaplex: Metaplex): OperationHandler<PublicKey, NFT> {
    override fun handle(operation: OperationResult<PublicKey, OperationError>): OperationResult<NFT, OperationError> {
        val bufferInfoResult = operation.flatMap {
            val metadataAccount = OperationResult.pure(MetadataAccount.pda(it)).flatMap {
                OperationResult<BufferInfo<MetadataAccount>, OperationError> { cb ->
                    this.metaplex.getAccountInfo(it, MetadataAccount::class.java) { result ->
                        result.onSuccess {
                            cb(ResultWithCustomError.success(it))
                        }.onFailure {
                            cb(ResultWithCustomError.failure(OperationError.GetMetadataAccountInfoError(RuntimeException(it))))
                        }
                    }
                }
            }

            val masterEditionAccount = OperationResult.pure(MasterEditionAccount.pda(it)).flatMap {
                OperationResult<BufferInfo<MasterEditionAccount>, OperationError> { cb ->
                    this.metaplex.getAccountInfo(it, MasterEditionAccount::class.java) { result ->
                        result.onSuccess {
                            cb(ResultWithCustomError.success(it))
                        }.onFailure {
                            cb(ResultWithCustomError.failure(OperationError.GetMetadataAccountInfoError(RuntimeException(it))))
                        }
                    }
                }
            }

            OperationResult.map2(metadataAccount, masterEditionAccount) { metadataAccountBuffer, masterEditionAccountBuffer ->
                Pair(metadataAccountBuffer, masterEditionAccountBuffer)
            }
        }
        return bufferInfoResult.flatMap {
            val metadataAccount = it.first.data?.value
            val masterEditionAccount = it.second.data?.value
            if(metadataAccount != null && masterEditionAccount != null){
                OperationResult.success(NFT(metadataAccount, masterEditionAccount))
            } else {
                OperationResult.failure(OperationError.NilDataOnAccount)
            }
        }
    }
}