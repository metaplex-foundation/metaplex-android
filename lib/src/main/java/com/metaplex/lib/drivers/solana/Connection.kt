/*
 * SolanaConnection
 * Metaplex
 * 
 * Created by Funkatronics on 7/27/2022
 */

package com.metaplex.lib.drivers.solana

import com.metaplex.lib.ASYNC_CALLBACK_DEPRECATION_MESSAGE
import com.solana.core.PublicKey
import com.solana.models.ProgramAccount
import com.solana.models.ProgramAccountConfig
import com.solana.models.SignatureStatusRequestConfiguration
import com.solana.models.buffer.BufferInfo
import com.solana.vendor.borshj.BorshCodable
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

/**
 * [Connection] interface that specifies the API for interacting with on chain solana accounts/data
 *
 * async-callback methods are deprecated and will be removed from this interface in a future version
 *
 * @author Funkatronics
 */
interface Connection {

    suspend fun getRecentBlockhash(): Result<String>

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

    //region DEPRECATED METHODS
    @Deprecated(ASYNC_CALLBACK_DEPRECATION_MESSAGE,
        ReplaceWith("getAccountInfo(serializer, account)"))
    fun <T: BorshCodable> getAccountInfo(account: PublicKey,
                                         decodeTo: Class<T>,
                                         onComplete: ((Result<BufferInfo<T>>) -> Unit))


    @Deprecated(ASYNC_CALLBACK_DEPRECATION_MESSAGE,
        ReplaceWith("getMultipleAccountsInfo(serializer, accounts)"))
    fun <T: BorshCodable> getMultipleAccountsInfo(
        accounts: List<PublicKey>,
        decodeTo: Class<T>,
        onComplete: ((Result<List<BufferInfo<T>?>>) -> Unit)
    )

    @Deprecated(ASYNC_CALLBACK_DEPRECATION_MESSAGE,
        ReplaceWith("getProgramAccounts(serializer, account, programAccountConfig)"))
    fun <T: BorshCodable> getProgramAccounts(account: PublicKey,
                                             programAccountConfig: ProgramAccountConfig,
                                             decodeTo: Class<T>,
                                             onComplete: (Result<List<ProgramAccount<T>>>) -> Unit
    )

    @Deprecated(ASYNC_CALLBACK_DEPRECATION_MESSAGE,
        ReplaceWith("getSignatureStatuses(signatures, configs)"))
    fun getSignatureStatuses(signatures: List<String>,
                             configs: SignatureStatusRequestConfiguration?,
                             onComplete: ((Result<com.solana.models.SignatureStatus>) -> Unit))
    //endregion
}

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