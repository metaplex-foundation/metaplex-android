package com.metaplex.lib.modules.nfts

import com.metaplex.lib.Metaplex
import com.metaplex.lib.drivers.indenty.IdentityDriver
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.drivers.solana.TransactionOptions
import com.metaplex.lib.extensions.signSendAndConfirm
import com.metaplex.lib.modules.nfts.builders.CreateNftTransactionBuilder
import com.metaplex.lib.modules.nfts.models.Metadata
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.modules.nfts.operations.*
import com.metaplex.lib.modules.token.TokenClient
import com.solana.core.HotAccount
import com.solana.core.PublicKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * NFT Client
 *
 * A client for interacting with Non-Fungible Metaplex Tokens
 *
 * @author ajamacia
 * @author Funkatronics
 */
class NftClient(private val connection: Connection, val signer: IdentityDriver,
                private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
                private val txOptions: TransactionOptions = connection.transactionOptions)
    : TokenClient(connection, dispatcher) {

    constructor(metaplex: Metaplex) : this(metaplex.connection, metaplex.identity())

    //region LOOKUPS
    override suspend fun findByMint(mintKey: PublicKey): Result<NFT> =
        FindNftByMintOnChainOperationHandler(connection, dispatcher).handle(mintKey)

    suspend fun findAllByMintList(mintKeys: List<PublicKey>): Result<List<NFT?>> =
        FindNftsByMintListOnChainOperationHandler(connection, dispatcher).handle(mintKeys)

    suspend fun findAllByOwner(publicKey: PublicKey): Result<List<NFT?>> =
        FindNftsByOwnerOnChainOperationHandler(connection, dispatcher).handle(publicKey)

    suspend fun findAllByCreator(creator: PublicKey, position: Int? = 1): Result<List<NFT?>> =
        FindNftsByCreatorOnChainOperationHandler(connection, dispatcher)
            .handle(FindNftsByCreatorInput(creator, position))

    suspend fun findAllByCandyMachine(candyMachine: PublicKey, version: Int? = 2): Result<List<NFT?>> =
        FindNftsByCandyMachineOnChainOperationHandler(connection, dispatcher)
            .handle(FindNftsByCandyMachineInput(candyMachine, version))
    //endregion

    // CREATE
    suspend fun create(
        metadata: Metadata, isCollection: Boolean = false,
        transactionOptions: TransactionOptions = txOptions
    ): Result<NFT> = runCatching {

        val newMintAccount = HotAccount()

        CreateNftTransactionBuilder(newMintAccount.publicKey, metadata, isCollection,
            signer.publicKey, connection, dispatcher)
            .build().getOrThrow()
            .signSendAndConfirm(connection, signer, listOf(newMintAccount), transactionOptions)

        return FindNftByMintOnChainOperationHandler(connection, dispatcher)
            .handle(newMintAccount.publicKey)
    }

    @Deprecated("Deprecated, please use the signed integer version instead",
        replaceWith = ReplaceWith("findAllByCandyMachine(candyMachine, version)"))
    suspend fun findAllByCandyMachine(candyMachine: PublicKey, version: UInt? = 2U)
    : Result<List<NFT?>> = findAllByCandyMachine(candyMachine, version?.toInt())
}