package com.metaplex.lib.modules.fungibletokens

import com.metaplex.lib.ASYNC_CALLBACK_DEPRECATION_MESSAGE
import com.metaplex.lib.Metaplex
import com.metaplex.lib.modules.fungibletokens.models.FungibleToken
import com.metaplex.lib.modules.fungibletokens.operations.*
import com.metaplex.lib.modules.nfts.operations.FindNftsByCandyMachineInput
import com.metaplex.lib.modules.nfts.operations.FindNftsByCandyMachineOnChainOperationHandler
import com.metaplex.lib.shared.OperationError
import com.metaplex.lib.shared.ResultWithCustomError
import com.solana.core.PublicKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.suspendCoroutine

// TODO: Deprecate this module and revamp NFT module into a shared Token module, or at least
//  create an abstraction that alleviates all of this code duplication between Nft and Ft
class FungibleTokenClient(private val metaplex: Metaplex) {

    suspend fun findByMint(mintKey: PublicKey): Result<FungibleToken> =
        FindFungibleTokenByMintOnChainOperationHandler(this.metaplex).handle(mintKey)

    @Deprecated(ASYNC_CALLBACK_DEPRECATION_MESSAGE, ReplaceWith("findByMint(mintKey)"))
    fun findByMint(mintKey: PublicKey, onComplete: (ResultWithCustomError<FungibleToken,OperationError>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            findByMint(mintKey)
                .onSuccess { onComplete(ResultWithCustomError.success(it)) }
                .onFailure { onComplete(ResultWithCustomError.failure(OperationError.GetMetadataAccountInfoError(it))) }
        }
    }
}