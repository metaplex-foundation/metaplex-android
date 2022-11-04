/*
 * CandyMachineClient
 * Metaplex
 *
 * Created by Funkatronics on 9/16/2022
 */

package com.metaplex.lib.modules.candymachines

import com.metaplex.lib.drivers.indenty.IdentityDriver
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.drivers.solana.TransactionOptions
import com.metaplex.lib.experimental.jen.candymachine.ConfigLine
import com.metaplex.lib.experimental.jen.candymachine.ConfigLineSettings
import com.metaplex.lib.extensions.signSendAndConfirm
import com.metaplex.lib.modules.candymachines.builders.*
import com.metaplex.lib.modules.candymachines.models.*
import com.metaplex.lib.modules.candymachines.operations.FindCandyMachineByAddressOperationHandler
import com.metaplex.lib.modules.candymachines.operations.FindCandyGuardByAddressOperationHandler
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.modules.nfts.operations.FindNftByMintOnChainOperationHandler
import com.solana.core.Account
import com.solana.core.HotAccount
import com.solana.core.PublicKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class CandyMachineClient(val connection: Connection, val signer: IdentityDriver,
                         private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
                         private val txOptions: TransactionOptions = connection.transactionOptions) {

    suspend fun findByAddress(address: PublicKey): Result<CandyMachine> =
        FindCandyMachineByAddressOperationHandler(connection, dispatcher).handle(address)

    suspend fun findCandyGuardByBaseAddress(base: PublicKey): Result<CandyGuard> =
        FindCandyGuardByAddressOperationHandler(connection, dispatcher)
            .handle(CandyGuard.pda(base).address)

    suspend fun create(
        sellerFeeBasisPoints: Int, itemsAvailable: Long, collection: PublicKey,
        collectionUpdateAuthority: PublicKey, authority: PublicKey = signer.publicKey,
        withoutCandyGuard: Boolean = false,
        transactionOptions: TransactionOptions = txOptions
    ): Result<CandyMachine> = runCatching {

        val candyMachineAccount = HotAccount()
        val candyMachineAddress = candyMachineAccount.publicKey
        val mintAuthority = if (withoutCandyGuard) authority else CandyGuard.pda(signer.publicKey).address

        CandyMachine(
            address = candyMachineAddress,
            authority = authority,
            mintAuthority = mintAuthority,
            sellerFeeBasisPoints = sellerFeeBasisPoints.toUShort(),
            itemsAvailable = itemsAvailable,
            collectionMintAddress = collection,
            collectionUpdateAuthority = collectionUpdateAuthority,
            configLineSettings = ConfigLineSettings(
                prefixName = "",
                nameLength = 32.toUInt(),
                prefixUri = "",
                uriLength = 200.toUInt(),
                isSequential = false,
            )
        ).apply {

            CreateCandyMachineTransactionBuilder(
                this, withoutCandyGuard, signer.publicKey, connection, dispatcher
            ).build().getOrThrow()
                .signSendAndConfirm(connection, signer, listOf(candyMachineAccount), transactionOptions)

            return Result.success(this)
        }
    }

    suspend fun createCandyGuard(
        guards: List<Guard>, groups: Map<String, List<Guard>> = mapOf(),
        authority: PublicKey = signer.publicKey, transactionOptions: TransactionOptions = txOptions
    ): Result<CandyGuard> = runCatching {

        val base = HotAccount()

        CandyGuard(base.publicKey, authority, guards, groups).apply {

            CreateCandyGuardTransactionBuilder(this, signer.publicKey, connection, dispatcher)
                .build()
                .getOrThrow()
                .signSendAndConfirm(connection, signer, listOf(base), transactionOptions)

            return Result.success(CandyGuard(base.publicKey, authority, guards))
        }
    }

    suspend fun wrapCandyGuard(
        candyGuard: CandyGuard, candyMachine: PublicKey, authority: Account? = null,
        transactionOptions: TransactionOptions = txOptions
    ): Result<String>  {

        val authorityAddress = authority?.publicKey ?: signer.publicKey
        val additionalSigners = authority?.let { listOf(authority) } ?: listOf()

        return WrapCandyGuardTransactionBuilder(
            candyGuard.base, candyMachine, authorityAddress, signer.publicKey, connection, dispatcher
        ).build().getOrThrow()
            .signSendAndConfirm(connection, signer, additionalSigners,
                transactionOptions = transactionOptions)
    }

    suspend fun setCollection(candyMachine: CandyMachine, collection: PublicKey,
                              transactionOptions: TransactionOptions = txOptions): Result<String> =
        SetCollectionTransactionBuilder(candyMachine, collection, signer.publicKey, connection, dispatcher)
            .build().mapCatching {
                it.signSendAndConfirm(connection, signer, listOf(), transactionOptions).getOrThrow()
            }

    suspend fun insertItems(candyMachine: CandyMachine, items: List<CandyMachineItem>,
                            authority: PublicKey = signer.publicKey,
                            transactionOptions: TransactionOptions = txOptions): Result<String> =
        AddConfigLinesTransactionBuilder(candyMachine, authority, connection, dispatcher)
            .addItems(items.map { ConfigLine(it.name, it.uri) })
            .build().mapCatching {
                it.signSendAndConfirm(connection, signer, transactionOptions = transactionOptions)
                    .getOrThrow()
            }

    suspend fun mintNft(candyMachine: CandyMachine,
                        transactionOptions: TransactionOptions = txOptions): Result<NFT> =
        runCatching {

            val newMintAccount = HotAccount()

            MintNftTransactionBuilder(candyMachine, newMintAccount.publicKey, signer.publicKey,
                connection, dispatcher)
                .build()
                .getOrThrow()
                .signSendAndConfirm(connection, signer, listOf(newMintAccount), transactionOptions)

            return FindNftByMintOnChainOperationHandler(connection, dispatcher)
                .handle(newMintAccount.publicKey)
        }
}

suspend fun CandyMachineClient.refresh(candyMachine: CandyMachine): Result<CandyMachine> =
    findByAddress(candyMachine.address)