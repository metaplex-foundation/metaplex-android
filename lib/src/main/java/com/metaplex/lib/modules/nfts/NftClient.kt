package com.metaplex.lib.modules.nfts

import com.metaplex.lib.Metaplex
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.modules.nfts.operations.*
import com.metaplex.lib.shared.OperationError
import com.metaplex.lib.shared.ResultWithCustomError
import com.solana.core.PublicKey

class NftClient(private val metaplex: Metaplex) {

    fun findByMint(mintKey: PublicKey, onComplete: (ResultWithCustomError<NFT,OperationError>) -> Unit){
        val operation = FindNftByMintOnChainOperationHandler(this.metaplex)
        operation.handle(FindNftByMintOperation.pure(ResultWithCustomError.success(mintKey))).run(onComplete)
    }

    fun findAllByMintList(mintKeys: List<PublicKey>, onComplete: (ResultWithCustomError<List<NFT?>, OperationError>) -> Unit){
        val operation = FindNftsByMintListOnChainOperationHandler(this.metaplex)
        operation.handle(FindNftsByMintListOperation.pure(ResultWithCustomError.success(
            mintKeys
        ))).run(onComplete)
    }

    fun findAllByOwner(publicKey: PublicKey, onComplete: (ResultWithCustomError<List<NFT?>, OperationError>) -> Unit) {
        val operation = FindNftsByOwnerOnChainOperationHandler(this.metaplex)
        operation.handle(FindNftsByOwnerOperation.pure(ResultWithCustomError.success(publicKey))).run { onComplete(it) }
    }

    fun findAllByCreator(creator: PublicKey, position: Int? = 1, onComplete: (ResultWithCustomError<List<NFT?>, OperationError>) -> Unit) {
        val operation = FindNftsByCreatorOnChainOperationHandler(this.metaplex)
        operation.handle(FindNftsByCreatorOperation.pure(ResultWithCustomError.success(
            FindNftsByCreatorInput(
                creator,
                position
            )))).run { onComplete(it) }
    }

    fun findAllByCandyMachine(candyMachine: PublicKey, version: UInt? = 2U, onComplete: (ResultWithCustomError<List<NFT?>, OperationError>) -> Unit) {
        val operation = FindNftsByCandyMachineOnChainOperationHandler(this.metaplex)
        operation.handle(FindNftsByCandyMachineOperation.pure(ResultWithCustomError.success(
            FindNftsByCandyMachineInput(
                candyMachine,
                version
            )))).run { onComplete(it) }
    }

    //region DEPRECATED METHODS
    @Deprecated("This method is obsolete and has been replaced by findByMint()",
        ReplaceWith("findByMint(mintKey, onComplete)"), DeprecationLevel.WARNING)
    fun findNftByMint(mintKey: PublicKey, onComplete: (ResultWithCustomError<NFT,OperationError>) -> Unit){
        findByMint(mintKey, onComplete)
    }

    @Deprecated("This method is obsolete and has been replaced by findAllByMintList()",
        ReplaceWith("findAllByMintList(mintKeys, onComplete)"), DeprecationLevel.WARNING)
    fun findNftByMintList(mintKeys: List<PublicKey>, onComplete: (ResultWithCustomError<List<NFT?>, OperationError>) -> Unit){
        findAllByMintList(mintKeys, onComplete)
    }

    @Deprecated("This method is obsolete and has been replaced by findAllByOwner()",
        ReplaceWith("findAllByOwner(publicKey, onComplete)"), DeprecationLevel.WARNING)
    fun findNftsByOwner(publicKey: PublicKey, onComplete: (ResultWithCustomError<List<NFT?>, OperationError>) -> Unit) {
        findAllByOwner(publicKey, onComplete)
    }

    @Deprecated("This method is obsolete and has been replaced by findAllByCreator()",
        ReplaceWith("findAllByCreator(creator, position, onComplete)"), DeprecationLevel.WARNING)
    fun findNftsByCreator(creator: PublicKey, position: Int? = 1, onComplete: (ResultWithCustomError<List<NFT?>, OperationError>) -> Unit) {
        findAllByCreator(creator, position, onComplete)
    }

    @Deprecated("This method is obsolete and has been replaced by findAllByCandyMachine()",
        ReplaceWith("findAllByCandyMachine(candyMachine, version, onComplete)"), DeprecationLevel.WARNING)
    fun findNftsByCandyMachine(candyMachine: PublicKey, version: UInt? = 2U, onComplete: (ResultWithCustomError<List<NFT?>, OperationError>) -> Unit) {
        findAllByCandyMachine(candyMachine, version, onComplete)
    }
    //endregion
}