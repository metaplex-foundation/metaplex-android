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
import com.metaplex.lib.drivers.rpc.makeRequest
import com.metaplex.lib.experimental.serialization.serializers.base64.BorshAsBase64JsonArraySerializer
import com.metaplex.lib.experimental.serialization.serializers.solana.AnchorAccountSerializer
import com.solana.core.PublicKey
import com.solana.models.RpcSendTransactionConfig
import com.solana.networking.RPCEndpoint
import kotlinx.serialization.KSerializer

// My intention here is not actually to create a new SolanaConnectionDriver, but to merge this new
// API (ConnectionKt) with the existing async-callback API (Connection). I have created this new
// implementation here as a temporary prototype and work area but will merge it into the base impl
class SolanaConnectionDriver(endpoint: RPCEndpoint) : ConnectionKt(endpoint) {

    private val url = endpoint.url
    private val rpcService: JsonRpcDriver = JdkRpcDriver(endpoint.url) // TODO: inject

    override suspend fun <A> getAccountInfo(serializer: KSerializer<A>,
                                            account: PublicKey): Result<AccountInfo<A>> {
        println("SolanaConnectionDriver: getAccountInfo")

        // hard coding the request params for now
        // could get them from solana.api.getAccountInfo but that seems cumbersome
        // need to chat with ajamaica to discuss this.
        val request = SolanaAccountRequest(account.toBase58(),
            RpcSendTransactionConfig.Encoding.base64)

        // TODO: still need to find a better way to figure out how to combine all the serializers:
        //  1. Deserialize Response
        //  2. Deserialize nested Borsh encoded data from the Response
        //  3. Deserialize AnchorAccount from the borsh data (if needed)
        //  3. Deserialize the final object (A) from the remaining borsh data

        // we either need to preregister our custom serializers in the json config:
//        val json = Json {
//            ignoreUnknownKeys = true
//            serializersModule = SerializersModule {
//                contextual(ContextualDataSerializer(AnchorAccountSerializer<AuctionHouse>()))
//            }
//        }

        // OR we have to manually link the nested serializers like so (gross):
        val metaSerializer = AccountInfo.serializer(
            BorshAsBase64JsonArraySerializer(
                if (serializer.descriptor.serialName == "BorshCodeable") serializer
                else AnchorAccountSerializer(serializer.descriptor.serialName, serializer)
            )
        )

        return makeRequest(request, metaSerializer) as Result<AccountInfo<A>>
    }

    private suspend inline fun <reified R> makeRequest(request: RpcRequest,
                                                       serializer: KSerializer<R>): Result<R> =
        rpcService.makeRequest(request).let { response ->
            response.value(serializer)?.let { result ->
                return Result.success(result)
            }

            response.error!!.let {
                return Result.failure(Error(it.message))
            }
        }
}