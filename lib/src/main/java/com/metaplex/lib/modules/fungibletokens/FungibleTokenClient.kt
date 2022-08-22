package com.metaplex.lib.modules.fungibletokens

import com.metaplex.lib.Metaplex
import com.metaplex.lib.modules.fungibletokens.models.FungibleToken
import com.metaplex.lib.modules.fungibletokens.operations.*
import com.metaplex.lib.shared.OperationError
import com.metaplex.lib.shared.ResultWithCustomError
import com.solana.core.PublicKey

class FungibleTokenClient(private val metaplex: Metaplex) {
    fun findByMint(mintKey: PublicKey, onComplete: (ResultWithCustomError<FungibleToken,OperationError>) -> Unit){
        val operation = FindFungibleTokenByMintOnChainOperationHandler(this.metaplex)
        operation.handle(FindFungibleTokenByMintOperation.pure(ResultWithCustomError.success(mintKey))).run(onComplete)
    }
}