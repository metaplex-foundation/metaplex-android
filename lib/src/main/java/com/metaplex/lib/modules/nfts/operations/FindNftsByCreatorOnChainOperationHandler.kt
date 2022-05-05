package com.metaplex.lib.modules.nfts.operations

import com.metaplex.lib.Metaplex
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.programs.token_metadata.TokenMetadataProgram
import com.metaplex.lib.shared.*
import com.solana.core.PublicKey

data class FindNftsByCreatorInput (
    val creator: PublicKey,
    val position: Int?
)

typealias FindNftsByCreatorOperation = OperationResult<FindNftsByCreatorInput, OperationError>

class FindNftsByCreatorOnChainOperationHandler(override var metaplex: Metaplex) : OperationHandler<FindNftsByCreatorInput, List<NFT?>> {
    val metadataV1GpaBuilder = TokenMetadataProgram.metadataV1Accounts(this.metaplex.connection)

    override fun handle(operation: OperationResult<FindNftsByCreatorInput, OperationError>): OperationResult<List<NFT?>, OperationError> {
        val publicKeys: OperationResult<List<PublicKey>, OperationError> = operation.flatMap { input ->
            val position = input.position ?: 1
            val creator = input.creator
            metadataV1GpaBuilder
                .selectMint()
                .whereCreator(position, creator)
            this.metadataV1GpaBuilder.getDataAsPublicKeys().mapError { OperationError.GetFindNftsByCreatorOperation(it) }
        }
        return publicKeys.flatMap { publicKeys ->
            val operation = FindNftsByMintListOnChainOperationHandler(this.metaplex)
            operation.handle(FindNftsByMintListOperation.pure(
                ResultWithCustomError.success(
                publicKeys
            )))
        }
    }
}
