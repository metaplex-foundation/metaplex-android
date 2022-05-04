package com.metaplex.lib.modules.nfts

import com.metaplex.lib.Metaplex
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.modules.nfts.operations.FindNftByMintOnChainOperationHandler
import com.metaplex.lib.modules.nfts.operations.FindNftByMintOperation
import com.metaplex.lib.modules.nfts.operations.FindNftsByMintListOnChainOperationHandler
import com.metaplex.lib.modules.nfts.operations.FindNftsByMintListOperation
import com.metaplex.lib.shared.OperationError
import com.metaplex.lib.shared.ResultWithCustomError
import com.solana.core.PublicKey

class NftClient(private val metaplex: Metaplex) {

    fun findNftByMint(mintKey: PublicKey, onComplete: (ResultWithCustomError<NFT,OperationError>) -> Unit){
        val operation = FindNftByMintOnChainOperationHandler(this.metaplex)
        operation.handle(FindNftByMintOperation.pure(ResultWithCustomError.success(mintKey))).run(onComplete)
    }

    fun findNftByMintList(mintKeys: List<PublicKey>, onComplete: (ResultWithCustomError<List<NFT?>, OperationError>) -> Unit){
        val operation = FindNftsByMintListOnChainOperationHandler(this.metaplex)
        operation.handle(FindNftsByMintListOperation.pure(ResultWithCustomError.success(
            mintKeys
        ))).run(onComplete)
    }
}