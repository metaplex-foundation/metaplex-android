package com.metaplex.lib.modules.nfts

import com.metaplex.lib.ASYNC_CALLBACK_DEPRECATION_MESSAGE
import com.metaplex.lib.Metaplex
import com.metaplex.lib.drivers.indenty.IdentityDriver
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.drivers.solana.TransactionOptions
import com.metaplex.lib.extensions.signSendAndConfirm
import com.metaplex.lib.modules.candymachines.CandyMachineClient
import com.metaplex.lib.modules.nfts.builders.CreateNftTransactionBuilder
import com.metaplex.lib.modules.nfts.models.Metadata
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.modules.nfts.operations.*
import com.metaplex.lib.modules.token.TokenClient
import com.metaplex.lib.shared.OperationError
import com.metaplex.lib.shared.ResultWithCustomError
import com.solana.core.Account
import com.solana.core.PublicKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

    suspend fun findAllByCandyMachine(candyMachine: PublicKey, version: UInt? = 2U): Result<List<NFT?>> =
        FindNftsByCandyMachineOnChainOperationHandler(connection, dispatcher)
            .handle(FindNftsByCandyMachineInput(candyMachine, version))
    //endregion

    // CREATE
    suspend fun create(
        metadata: Metadata, isCollection: Boolean = false,
        transactionOptions: TransactionOptions = txOptions
    ): Result<NFT> = runCatching {

        val newMintAccount = Account()

        CreateNftTransactionBuilder(newMintAccount.publicKey, metadata, isCollection,
            signer.publicKey, connection, dispatcher)
            .build().getOrThrow()
            .signSendAndConfirm(connection, signer, listOf(newMintAccount), transactionOptions)

        return FindNftByMintOnChainOperationHandler(connection)//, dispatcher)
            .handle(newMintAccount.publicKey)
    }

    //region DEPRECATED METHODS
    //region ASYNC-CALLBACK API
    @Deprecated(ASYNC_CALLBACK_DEPRECATION_MESSAGE, ReplaceWith("findByMint(mintKey)"))
    fun findByMint(mintKey: PublicKey,
                   onComplete: (ResultWithCustomError<NFT,OperationError>) -> Unit) {
        CoroutineScope(dispatcher).launch { findByMint(mintKey)
            .onSuccess { onComplete(ResultWithCustomError.success(it)) }
            .onFailure { onComplete(ResultWithCustomError.failure(it as OperationError)) }
        }
    }

    @Deprecated(ASYNC_CALLBACK_DEPRECATION_MESSAGE, ReplaceWith("findAllByMintList(mintKeys)"))
    fun findAllByMintList(mintKeys: List<PublicKey>, onComplete: (ResultWithCustomError<List<NFT?>, OperationError>) -> Unit) {
        CoroutineScope(dispatcher).launch { findAllByMintList(mintKeys)
            .onSuccess { onComplete(ResultWithCustomError.success(it)) }
            .onFailure { onComplete(ResultWithCustomError.failure(it as OperationError)) }
        }
    }

    @Deprecated(ASYNC_CALLBACK_DEPRECATION_MESSAGE, ReplaceWith("findAllByOwner(mintKeys)"))
    fun findAllByOwner(publicKey: PublicKey, onComplete: (ResultWithCustomError<List<NFT?>, OperationError>) -> Unit) {
        CoroutineScope(dispatcher).launch { findAllByOwner(publicKey)
            .onSuccess { onComplete(ResultWithCustomError.success(it)) }
            .onFailure { onComplete(ResultWithCustomError.failure(it as OperationError)) }
        }
    }

    @Deprecated(ASYNC_CALLBACK_DEPRECATION_MESSAGE, ReplaceWith("findAllByCreator(mintKeys)"))
    fun findAllByCreator(creator: PublicKey, position: Int? = 1, onComplete: (ResultWithCustomError<List<NFT?>, OperationError>) -> Unit) {
        CoroutineScope(dispatcher).launch { findAllByCreator(creator, position)
            .onSuccess { onComplete(ResultWithCustomError.success(it)) }
            .onFailure { onComplete(ResultWithCustomError.failure(it as OperationError)) }
        }
    }

    @Deprecated(ASYNC_CALLBACK_DEPRECATION_MESSAGE, ReplaceWith("findAllByCandyMachine(mintKeys)"))
    fun findAllByCandyMachine(candyMachine: PublicKey, version: UInt? = 2U, onComplete: (ResultWithCustomError<List<NFT?>, OperationError>) -> Unit) {
        CoroutineScope(dispatcher).launch { findAllByCandyMachine(candyMachine, version)
            .onSuccess { onComplete(ResultWithCustomError.success(it)) }
            .onFailure { onComplete(ResultWithCustomError.failure(it as OperationError)) }
        }
    }
    //endregion

    //region LEGACY API
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
    //endregion
}