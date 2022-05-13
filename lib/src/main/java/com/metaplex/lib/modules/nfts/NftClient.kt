package com.metaplex.lib.modules.nfts

import com.metaplex.lib.Metaplex
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.modules.nfts.operations.*
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

    fun findNftsByCreator(creator: PublicKey, position: Int? = 1, onComplete: (ResultWithCustomError<List<NFT?>, OperationError>) -> Unit) {
        val operation = FindNftsByCreatorOnChainOperationHandler(this.metaplex)
        operation.handle(FindNftsByCreatorOperation.pure(ResultWithCustomError.success(
            FindNftsByCreatorInput(
                creator,
                position
            )))).run { onComplete(it) }
    }

    fun findNftsByCandyMachine(candyMachine: PublicKey, version: UInt? = 2U, onComplete: (ResultWithCustomError<List<NFT?>, OperationError>) -> Unit) {
        val operation = FindNftsByCandyMachineOnChainOperationHandler(this.metaplex)
        operation.handle(FindNftsByCandyMachineOperation.pure(ResultWithCustomError.success(
            FindNftsByCandyMachineInput(
                candyMachine,
                version
            )))).run { onComplete(it) }
    }

    fun findNftsByOwner(publicKey: PublicKey, onComplete: (ResultWithCustomError<List<NFT?>, OperationError>) -> Unit) {
        val operation = FindNftsByOwnerOnChainOperationHandler(this.metaplex)
        operation.handle(FindNftsByOwnerOperation.pure(ResultWithCustomError.success(publicKey))).run { onComplete(it) }
    }
}