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
import com.metaplex.lib.programs.token_metadata.MasterEditionAccountJsonAdapterFactory
import com.metaplex.lib.programs.token_metadata.MasterEditionAccountRule
import com.metaplex.lib.programs.token_metadata.accounts.*
import com.metaplex.lib.shared.AccountPublicKeyJsonAdapterFactory
import com.metaplex.lib.shared.AccountPublicKeyRule
import com.solana.api.Api
import com.solana.api.getMultipleAccounts
import com.solana.api.getProgramAccounts
import com.solana.api.getSignatureStatuses
import com.solana.core.PublicKey
import com.solana.models.ProgramAccountConfig
import com.solana.models.RpcSendTransactionConfig
import com.solana.models.SignatureStatusRequestConfiguration
import com.solana.models.buffer.BufferInfo
import com.solana.networking.NetworkingRouterConfig
import com.solana.networking.OkHttpNetworkingRouter
import com.solana.networking.RPCEndpoint
import com.solana.vendor.borshj.BorshCodable
import kotlinx.serialization.KSerializer

// My intention here is not actually to create a new SolanaConnectionDriver, but to merge this new
// API (ConnectionKt) with the existing async-callback API (Connection). I have created this new
// implementation here as a temporary prototype and work area but will merge it into the base impl
class SolanaConnectionDriver(private val rpcService: JsonRpcDriver)
    : ConnectionKt() {

    private var endpoint: RPCEndpoint = RPCEndpoint.mainnetBetaSolana

    constructor(endpoint: RPCEndpoint, rpcService: JsonRpcDriver = JdkRpcDriver(endpoint.url))
            : this(rpcService) { this.endpoint = endpoint }

    @Suppress("UNCHECKED_CAST")
    override suspend fun <A> getAccountInfo(serializer: KSerializer<A>,
                                            account: PublicKey): Result<AccountInfo<A>> =
        makeRequest(
            SolanaAccountRequest(account.toBase58(), RpcSendTransactionConfig.Encoding.base64),
            SolanaAccountSerializer(serializer)
        ).let { result ->
            if (result.isSuccess && result.getOrNull() == null)
                Result.failure(Error("Account return Null"))
            else result as Result<AccountInfo<A>> // safe cast, null case handled above
        }

    private suspend inline fun <reified R> makeRequest(request: RpcRequest,
                                                       serializer: KSerializer<R>): Result<R?> =
        rpcService.makeRequest(request, serializer).let { response ->
            (response.result)?.let { result ->
                return Result.success(result)
            }

            response.error?.let {
                return Result.failure(Error(it.message))
            }

            // an empty error and empty result means we did not find anything, return null
            return Result.success(null)
        }

    //region LEGACY IMPLEMENTATION
    // Temporary, until we complete getProgramAccounts, getMultipleAccountsInfo and getSignatureStatuses
    val solanaRPC: Api = Api(
        OkHttpNetworkingRouter(endpoint,
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

    override fun <T : BorshCodable> getProgramAccounts(
        account: PublicKey,
        programAccountConfig: ProgramAccountConfig,
        decodeTo: Class<T>,
        onComplete: (Result<List<com.solana.models.ProgramAccount<T>>>) -> Unit) {
        solanaRPC.getProgramAccounts(account, programAccountConfig, decodeTo, onComplete)
    }

    override fun <T: BorshCodable> getMultipleAccountsInfo(
        accounts: List<PublicKey>,
        decodeTo: Class<T>,
        onComplete: ((Result<List<BufferInfo<T>?>>) -> Unit)
    ) {
        solanaRPC.getMultipleAccounts(accounts, decodeTo, onComplete)
    }

    override fun getSignatureStatuses(signatures: List<String>,
                                      configs: SignatureStatusRequestConfiguration?,
                                      onComplete: ((Result<com.solana.models.SignatureStatus>) -> Unit)) {
        solanaRPC.getSignatureStatuses(signatures, configs, onComplete)
    }
    //endregion
}