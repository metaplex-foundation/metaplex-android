package com.metaplex.lib.modules.nfts.operations

import com.metaplex.lib.Metaplex
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.programs.token_metadata.gpa_builders.TokenGpaBuilder
import com.metaplex.lib.programs.tokens.TokenProgram
import com.metaplex.lib.shared.*
import com.solana.core.PublicKey

typealias FindNftsByOwnerOperation = OperationResult<PublicKey, OperationError>

class FindNftsByOwnerOnChainOperationHandler(override var metaplex: Metaplex) :
    OperationHandler<PublicKey, List<NFT?>> {
    var tokenGpaBuilder: TokenGpaBuilder = TokenProgram.tokenAccounts(this.metaplex.connection)
    override fun handle(operation: FindNftsByOwnerOperation): OperationResult<List<NFT?>, OperationError> {
        return operation.flatMap { owner ->
            this.tokenGpaBuilder
                .selectMint()
                .whereOwner(owner)
                //.whereAmount(1)
                .getDataAsPublicKeys()
                .mapError { OperationError.GetFindNftsByOwnerOperation(it) }
        }.flatMap { publicKeys ->
            val findNftsByMintListOnChainOperationHandlerOperation = FindNftsByMintListOnChainOperationHandler(this.metaplex)
            findNftsByMintListOnChainOperationHandlerOperation.handle(
                FindNftsByMintListOperation.pure(
                    ResultWithCustomError.success(
                        publicKeys
                    )
                )
            )
        }
    }
}