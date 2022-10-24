/*
 * SolanaConnection
 * Metaplex
 * 
 * Created by Funkatronics on 7/27/2022
 */

package com.metaplex.lib.drivers.solana

import android.util.Base64
import com.metaplex.lib.drivers.rpc.RpcRequest
import com.solana.core.PublicKey
import com.solana.core.Transaction
import com.solana.models.ProgramAccountConfig
import com.solana.models.SignatureStatusRequestConfiguration
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.serializer

/**
 * [Connection] interface that specifies the API for interacting with on chain solana accounts/data
 *
 * async-callback methods are deprecated and will be removed from this interface in a future version
 *
 * @author Funkatronics
 */
interface Connection {

    val transactionOptions: TransactionOptions

    suspend fun <R> get(request: RpcRequest, serializer: KSerializer<R>): Result<R>

    suspend fun <A> getAccountInfo(serializer: KSerializer<A>, account: PublicKey)
    : Result<AccountInfo<A>>

    suspend fun <A> getMultipleAccountsInfo(serializer: KSerializer<A>, accounts: List<PublicKey>)
    : Result<List<AccountInfo<A>?>>

    suspend fun <A> getProgramAccounts(serializer: KSerializer<A>, account: PublicKey,
                                       programAccountConfig: ProgramAccountConfig
    ): Result<List<AccountInfoWithPublicKey<A>>>

    suspend fun getSignatureStatuses(signatures: List<String>,
                                     configs: SignatureStatusRequestConfiguration?)
    : Result<List<SignatureStatus>>
}

//region ERRORS
sealed class ConnectionError(message: String) : Error(message)
class NullResultError : ConnectionError("Request returned null result")
//endregion

//region UTIL OPERATIONS
suspend fun Connection.getRecentBlockhash(): Result<String> =
    get(RecentBlockhashRequest(), BlockhashSerializer()).map { it?.blockhash ?: "" }

suspend fun Connection.sendTransaction(serializedTransaction: String): Result<String> =
    get(SendTransactionRequest(serializedTransaction, transactionOptions), String.serializer())

suspend fun Connection.sendTransaction(transaction: Transaction): Result<String> =
    sendTransaction(Base64.encodeToString(transaction.serialize(), Base64.DEFAULT))
//endregion

// Inlines let us hide the serialization complexity while still providing full control
// For example, to find an AuctionHouse by address:
//      val auctionHouse = connectionKt.getAccountInfo<AuctionHouse>(address)
// And optionally, to find an account and supply your own serializer :
//      val customAccount = connectionKt.getAccountInfo(customSerializer, address)
suspend inline fun <reified A> Connection.getAccountInfo(account: PublicKey)
        : Result<AccountInfo<A>> = getAccountInfo(serializer(), account)

suspend inline fun <reified A> Connection.getMultipleAccountsInfo(accounts: List<PublicKey>)
        : Result<List<AccountInfo<A>?>> = getMultipleAccountsInfo(serializer(), accounts)

suspend inline fun <reified A> Connection.getProgramAccounts(account: PublicKey, config: ProgramAccountConfig)
        : Result<List<AccountInfoWithPublicKey<A>?>> = getProgramAccounts(serializer(), account, config)