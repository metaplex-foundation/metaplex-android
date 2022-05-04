package com.metaplex.lib.modules.nfts.operations

import com.metaplex.lib.Metaplex
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.programs.token_metadata.MetadataAccount
import com.metaplex.lib.shared.*
import com.solana.core.PublicKey
import java.lang.RuntimeException

class FindNftsByMintListOnChainOperationHandler(override var metaplex: Metaplex): OperationHandler<List<PublicKey>, List<NFT?>> {
    private val gmaBuilder = GmaBuilder(metaplex.connection, listOf(), null)
    override fun handle(operation: OperationResult<List<PublicKey>, OperationError>): OperationResult<List<NFT?>, OperationError> {
        val result = operation.flatMap { publicKeys ->
            val pdas = mutableListOf<PublicKey>()
            for (mintKey in publicKeys){
                MetadataAccount.pda(mintKey).getOrDefault(null)?.let { publicKey ->
                    pdas.add(publicKey)
                } ?: run {
                    return@flatMap OperationResult.failure(OperationError.CouldNotFindPDA)
                }
            }
            return@flatMap OperationResult.success(pdas)
        }

        val resultAccounts = result.flatMap { publicKeys ->
            gmaBuilder.setPublicKeys(publicKeys.toList())
                .get()
                .mapError { OperationError.GmaBuilderError(RuntimeException(it)) }
        }

        return resultAccounts.flatMap { accountInfos ->
            val nfts = mutableListOf<NFT?>()
            for (accountInfo in accountInfos){
                if(accountInfo.exists && accountInfo.metadata != null) {
                    nfts.add(NFT(accountInfo.metadata, null))
                } else {
                    nfts.add(null)
                }
            }
            OperationResult.success(nfts)
        }
    }
}