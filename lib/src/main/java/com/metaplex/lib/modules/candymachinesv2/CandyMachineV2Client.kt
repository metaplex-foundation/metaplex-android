/*
 * CandyMachineClient
 * Metaplex
 *
 * Created by Funkatronics on 9/13/2022
 */

package com.metaplex.lib.modules.candymachinesv2

import com.metaplex.lib.drivers.indenty.IdentityDriver
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.drivers.solana.TransactionOptions
import com.metaplex.lib.extensions.signSendAndConfirm
import com.metaplex.lib.modules.candymachinesv2.builders.CreateCandyMachineV2TransactionBuilder
import com.metaplex.lib.modules.candymachinesv2.builders.MintNftTransactionBuilder
import com.metaplex.lib.modules.candymachinesv2.models.CandyMachineV2
import com.metaplex.lib.modules.candymachinesv2.operations.FindCandyMachineV2ByAddressOperationHandler
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.modules.nfts.operations.FindNftByMintOnChainOperationHandler
import com.solana.core.HotAccount
import com.solana.core.PublicKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class CandyMachineV2Client(val connection: Connection, val signer: IdentityDriver,
                           private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
                           private val txOptions: TransactionOptions = connection.transactionOptions) {

    suspend fun findByAddress(address: PublicKey): Result<CandyMachineV2> =
        FindCandyMachineV2ByAddressOperationHandler(connection, dispatcher).handle(address)

    suspend fun create(
        price: Long, sellerFeeBasisPoints: Int, itemsAvailable: Long,
        wallet: PublicKey = signer.publicKey, authority: PublicKey = signer.publicKey,
        transactionOptions: TransactionOptions = txOptions
    ): Result<CandyMachineV2> = runCatching {

        val candyMachineAccount = HotAccount()
        val candyMachineAddress = candyMachineAccount.publicKey

        CandyMachineV2(
            address = candyMachineAddress,
            authority = authority,
            wallet = wallet,
            price = price,
            sellerFeeBasisPoints = sellerFeeBasisPoints.toUShort(),
            itemsAvailable = itemsAvailable
        ).apply {

            CreateCandyMachineV2TransactionBuilder(this, wallet, connection, dispatcher)
                .build()
                .getOrThrow()
                .signSendAndConfirm(connection, signer, listOf(candyMachineAccount), transactionOptions)

            return Result.success(this)
        }
    }

    suspend fun mintNft(
        candyMachine: CandyMachineV2, transactionOptions: TransactionOptions = txOptions)
    : Result<NFT> = runCatching {

        val newMintAccount = HotAccount()

        MintNftTransactionBuilder(candyMachine, newMintAccount.publicKey, signer.publicKey, connection, dispatcher)
            .build()
            .getOrThrow()
            .signSendAndConfirm(connection, signer, listOf(newMintAccount), transactionOptions)

        return FindNftByMintOnChainOperationHandler(connection, dispatcher)
            .handle(newMintAccount.publicKey)
    }
}