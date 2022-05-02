package com.metaplex.lib.modules.nfts.operations

import com.metaplex.lib.Metaplex
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.shared.OperationError
import com.metaplex.lib.shared.OperationHandler
import com.metaplex.lib.shared.OperationResult
import com.solana.core.PublicKey

class FindNftByMintOnChainOperationHandler(override var metaplex: Metaplex): OperationHandler<PublicKey, NFT> {
    override fun handle(operation: OperationResult<PublicKey, OperationError>): OperationResult<NFT, OperationError> {
        TODO("Not yet implemented")
    }

}