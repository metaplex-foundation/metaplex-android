/*
 * SolanaConnectionDriver
 * Metaplex
 * 
 * Created by Funkatronics on 7/28/2022
 */

package com.metaplex.lib.drivers.solana

import com.metaplex.lib.drivers.rpc.JdkRpcDriver
import com.metaplex.lib.drivers.rpc.JsonRpcDriver
import com.metaplex.lib.drivers.rpc.RpcRequest
import com.metaplex.lib.experimental.serialization.serializers.legacy.BorshCodeableSerializer
import com.metaplex.lib.programs.token_metadata.MasterEditionAccountJsonAdapterFactory
import com.metaplex.lib.programs.token_metadata.MasterEditionAccountRule
import com.metaplex.lib.programs.token_metadata.accounts.*
import com.metaplex.lib.shared.AccountPublicKeyJsonAdapterFactory
import com.metaplex.lib.shared.AccountPublicKeyRule
import com.solana.api.Api
import com.solana.core.PublicKey
import com.solana.models.ProgramAccountConfig
import com.solana.models.SignatureStatusRequestConfiguration
import com.solana.models.buffer.BufferInfo
import com.solana.networking.NetworkingRouter
import com.solana.networking.NetworkingRouterConfig
import com.solana.networking.RPCEndpoint
import com.solana.vendor.borshj.BorshCodable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer

/**
 * [Connection] implementation that wraps the legacy async-callback API into the
 * suspendable API implementation where possible. Methods without a suspendable equivalent are
 * delegated to the legacy implementation. The hope is to eventually deprecate the legacy callback
 * API entirely and move to a new combined API that offers both suspendable and callback interfaces
 * and a better serialization experience (no more decodeTo parameters and moshi/borsh rules)
 *
 * @author Funkatronics
 */
class SolanaConnectionDriver(private val rpcService: JsonRpcDriver,
                             override val transactionOptions: TransactionOptions = TransactionOptions()
)
    : Connection {

    private var endpoint: RPCEndpoint = RPCEndpoint.mainnetBetaSolana

    @JvmOverloads
    constructor(endpoint: RPCEndpoint, rpcService: JsonRpcDriver = JdkRpcDriver(endpoint.url))
            : this(rpcService) { this.endpoint = endpoint }

    // Some things are still using this, so need to keep a reference to it here
    @Deprecated("Deprecated, use an RPC Driver implementation instead")
    val solanaRPC: Api = Api(
        NetworkingRouter(endpoint,
            config = NetworkingRouterConfig(
                listOf(
                    MetadataAccountRule(),
                    MetaplexDataRule(),
                    MetaplexCollectionRule(),
                    AccountPublicKeyRule(),
                    MasterEditionAccountRule(),
                    MetaplexCreatorRule()
                ),
                listOf(
                    MetadataAccountJsonAdapterFactory(),
                    MetaplexDataAdapterJsonAdapterFactory(),
                    AccountPublicKeyJsonAdapterFactory(),
                    MasterEditionAccountJsonAdapterFactory()
                )
            )
        )
    )

    //region CONNECTION
    override suspend fun <R> get(request: RpcRequest, serializer: KSerializer<R>): Result<R> =
        rpcService.makeRequest(request, serializer).let { response ->
            (response.result)?.let { result ->
                return Result.success(result)
            }

            response.error?.let {
                return Result.failure(Error(it.message))
            }

            // an empty error and empty result means we did not find anything, return error
            return Result.failure(NullResultError())
        }

    override suspend fun <A> getAccountInfo(serializer: KSerializer<A>, account: PublicKey)
            : Result<AccountInfo<A>> =
        get(
            AccountRequest(account.toBase58(), transactionOptions),
            SolanaAccountSerializer(serializer)
        ).let { result ->
            @Suppress("UNCHECKED_CAST")
            if (result.exceptionOrNull() is NullResultError
                || (result.isSuccess && result.getOrNull() == null))
                Result.failure(Error("Account return Null"))
            else result as Result<AccountInfo<A>> // safe cast, null case handled above
        }

    override suspend fun <A> getMultipleAccountsInfo(
        serializer: KSerializer<A>,
        accounts: List<PublicKey>
    ): Result<List<AccountInfo<A>?>> =
        get(
            MultipleAccountsRequest(accounts.map { it.toBase58() }, transactionOptions),
            MultipleAccountsSerializer(serializer)
        ).let { result ->
            @Suppress("UNCHECKED_CAST")
            if (result.exceptionOrNull() is NullResultError
                || (result.isSuccess && result.getOrNull() == null)) Result.success(listOf())
            else result as Result<List<AccountInfo<A>?>> // safe cast, null case handled above
        }

    override suspend fun <A> getProgramAccounts(
        serializer: KSerializer<A>,
        account: PublicKey,
        programAccountConfig: ProgramAccountConfig
    ): Result<List<AccountInfoWithPublicKey<A>>> =
        get(
            ProgramAccountRequest(account.toString(),
                programAccountConfig.encoding, programAccountConfig.filters,
                programAccountConfig.dataSlice, programAccountConfig.commitment),
            ProgramAccountsSerializer(serializer)
        ).let { result ->
            @Suppress("UNCHECKED_CAST")
            if (result.exceptionOrNull() is NullResultError
                || (result.isSuccess && result.getOrNull() == null)) Result.success(listOf())
            else result as Result<List<AccountInfoWithPublicKey<A>>> // safe cast, null case handled above
        }

    override suspend fun getSignatureStatuses(
        signatures: List<String>,
        configs: SignatureStatusRequestConfiguration?
    ): Result<List<SignatureStatus>> =
        get(
            SignatureStatusRequest(signatures, configs?.searchTransactionHistory ?: false),
            SignatureStatusesSerializer()
        ).let { result ->
            @Suppress("UNCHECKED_CAST")
            if (result.exceptionOrNull() is NullResultError
                || (result.isSuccess && result.getOrNull() == null)) Result.success(listOf())
            else result as Result<List<SignatureStatus>> // safe cast, null case handled above
        }
    //endregion

    //region DEPRECATED METHODS
    override fun <T: BorshCodable> getAccountInfo(
        account: PublicKey,
        decodeTo: Class<T>,
        onComplete: (Result<BufferInfo<T>>) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            onComplete(getAccountInfo(BorshCodeableSerializer(decodeTo), account)
                .map { it.toBufferInfo() })
        }
    }

    override fun <T : BorshCodable> getMultipleAccountsInfo(
        accounts: List<PublicKey>,
        decodeTo: Class<T>,
        onComplete: (Result<List<BufferInfo<T>?>>) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            onComplete(getMultipleAccountsInfo(BorshCodeableSerializer(decodeTo), accounts)
                .map { it.map { it?.toBufferInfo() } })
        }
    }

    override fun <T : BorshCodable> getProgramAccounts(
        account: PublicKey,
        programAccountConfig: ProgramAccountConfig,
        decodeTo: Class<T>,
        onComplete: (Result<List<com.solana.models.ProgramAccount<T>>>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            onComplete(getProgramAccounts(BorshCodeableSerializer(decodeTo), account, programAccountConfig)
                .map { it.map {
                    com.solana.models.ProgramAccount(it.account.toBufferInfo(), it.publicKey)
                } })
        }
    }

    override fun getSignatureStatuses(signatures: List<String>,
                                      configs: SignatureStatusRequestConfiguration?,
                                      onComplete: ((Result<com.solana.models.SignatureStatus>) -> Unit)) {
        CoroutineScope(Dispatchers.IO).launch {
            onComplete(getSignatureStatuses(signatures, configs).map {
                com.solana.models.SignatureStatus(it.map { sigStatus ->
                    com.solana.models.SignatureStatus.Value(
                        sigStatus.slot, sigStatus.confirmations,
                        sigStatus.err, sigStatus.confirmationStatus
                    )
                })
            })
        }
    }
    //endregion
}