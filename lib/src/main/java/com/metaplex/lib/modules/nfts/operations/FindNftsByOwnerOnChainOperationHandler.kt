package com.metaplex.lib.modules.nfts.operations

import com.metaplex.lib.Metaplex
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.programs.token_metadata.gpa_builders.TokenGpaBuilder
import com.metaplex.lib.programs.tokens.TokenProgram
import com.metaplex.lib.shared.*
import com.solana.core.PublicKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class FindNftsByOwnerOnChainOperationHandler(override val connection: Connection,
                                             override val dispatcher: CoroutineDispatcher = Dispatchers.IO)
    : OperationHandler<PublicKey, List<NFT?>> {

    constructor(metaplex: Metaplex) : this(metaplex.connection)

    var tokenGpaBuilder: TokenGpaBuilder = TokenProgram.tokenAccounts(this.connection)

    override suspend fun handle(input: PublicKey): Result<List<NFT?>> =
        tokenGpaBuilder
            .selectMint()
            .whereOwner(input)
            .whereAmount(1)
            .get()
            .getOrElse {
                return Result.failure(OperationError.GetFindNftsByOwnerOperation(it))
            }.mapNotNull {
                it.account.data?.publicKey
            }.let { publicKeys ->
                FindNftsByMintListOnChainOperationHandler(connection, dispatcher).handle(publicKeys)
            }
}