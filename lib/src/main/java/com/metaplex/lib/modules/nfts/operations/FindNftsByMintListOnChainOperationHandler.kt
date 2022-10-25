package com.metaplex.lib.modules.nfts.operations

import com.metaplex.lib.Metaplex
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.programs.token_metadata.accounts.MetadataAccount
import com.metaplex.lib.shared.*
import com.solana.core.PublicKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class FindNftsByMintListOnChainOperationHandler(override val connection: Connection,
                                                override val dispatcher: CoroutineDispatcher = Dispatchers.IO)
    : OperationHandler<List<PublicKey>, List<NFT?>> {

    constructor(metaplex: Metaplex) : this(metaplex.connection)

    // Rather than refactoring GmaBuilder to use coroutines, I just pulled the required logic
    // out and implemented it here. In the future we can refactor GmaBuilder if needed
    private val chunkSize = 100

    override suspend fun handle(input: List<PublicKey>): Result<List<NFT?>> =
        Result.success(input.map {
            MetadataAccount.pda(it).getOrDefault(null)
                ?: return Result.failure(OperationError.CouldNotFindPDA)
        }.chunked(chunkSize).map { chunk ->
            // TODO: how can I parallelize this?
            connection.getMultipleAccountsInfo(MetadataAccount.serializer(), chunk).getOrElse {
                return Result.failure(OperationError.GmaBuilderError(it))
            }
        }.flatten().map { account ->
            account?.data?.let {
                NFT(account.data, null)
            }
        })
}