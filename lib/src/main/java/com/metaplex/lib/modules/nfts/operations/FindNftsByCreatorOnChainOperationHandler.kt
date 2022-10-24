package com.metaplex.lib.modules.nfts.operations

import com.metaplex.lib.Metaplex
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.programs.token_metadata.TokenMetadataProgram
import com.metaplex.lib.shared.*
import com.solana.core.PublicKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

data class FindNftsByCreatorInput (
    val creator: PublicKey,
    val position: Int?
)

class FindNftsByCreatorOnChainOperationHandler(override val connection: Connection,
                                               override val dispatcher: CoroutineDispatcher = Dispatchers.IO)
    : OperationHandler<FindNftsByCreatorInput, List<NFT?>> {

    constructor(metaplex: Metaplex) : this(metaplex.connection)

    val metadataV1GpaBuilder = TokenMetadataProgram.metadataV1Accounts(this.connection)

    override suspend fun handle(input: FindNftsByCreatorInput): Result<List<NFT?>> =
        metadataV1GpaBuilder
            .selectMint()
            .whereCreator(input.position ?: 1, input.creator)
            .get()
            .getOrElse {
                return Result.failure(OperationError.GetFindNftsByCreatorOperation(it))
            }.mapNotNull {
                it.account.data?.publicKey
            }.let { publicKeys ->
                FindNftsByMintListOnChainOperationHandler(connection, dispatcher).handle(publicKeys)
            }
}
