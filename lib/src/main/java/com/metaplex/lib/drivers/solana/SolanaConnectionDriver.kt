/*
 * SolanaConnectionDriver
 * Metaplex
 * 
 * Created by Funkatronics on 7/28/2022
 */

package com.metaplex.lib.drivers.solana

import com.metaplex.lib.drivers.rpc.JdkRpcDriver
import com.metaplex.lib.drivers.rpc.JsonRpcDriver
import com.metaplex.lib.experimental.serialization.serializers.base64.BorshAsBase64JsonArraySerializer
import com.metaplex.lib.experimental.serialization.serializers.rpc.solana.SolanaAccountResponse
import com.metaplex.lib.experimental.serialization.serializers.rpc.solana.SolanaResponse
import com.metaplex.lib.experimental.serialization.serializers.rpc.solana.value
import com.metaplex.lib.experimental.serialization.serializers.solana.AnchorAccountSerializer
import com.metaplex.lib.solana.Connection
import com.solana.api.Api
import com.solana.core.PublicKey
import com.solana.models.ProgramAccount
import com.solana.models.ProgramAccountConfig
import com.solana.models.SignatureStatus
import com.solana.models.SignatureStatusRequestConfiguration
import com.solana.models.buffer.BufferInfo
import com.solana.networking.NetworkingRouter
import com.solana.networking.RPCEndpoint
import com.solana.vendor.borshj.BorshCodable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.lang.reflect.Type

// My intention here is not actually to create a new SolanaConnectionDriver, but to merge this new
// API (ConnectionKt) with the existing async-callback API (Connection). I have created this new
// implementation here as a temporary prototype and work area but will merge it into the base impl
class SolanaConnectionDriver : ConnectionKt, Connection {

    override suspend fun <A> getAccountInfo(accountSerializer: KSerializer<A>,
                                            account: PublicKey
    ): Result<A> {
        println("SolanaConnectionDriver: getAccountInfo")

        // hard coding the request params for now
        // could get them from solana.api.getAccountInfo but that seems cumbersome
        // need to chat with ajamaica to discuss this.
        val request = SolanaAccountRequest(account.toBase58(), mapOf(
            "commitment" to "max",
            "encoding" to "base64"
        ))

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
        val serializer = SolanaAccountResponse.serializer(
            BorshAsBase64JsonArraySerializer(
                AnchorAccountSerializer(accountSerializer.descriptor.serialName, accountSerializer)
            )
        )

        // TODO: implement some kind of service/driver provider abstraction
        with(JdkRpcDriver() as JsonRpcDriver) {
            makeRequest(request, SolanaResponse.serializer()).let { response ->
                response.value(serializer)?.let { result ->
                    return Result.success(result.data)
                }

                response.error!!.let {
                    return Result.failure(Error(it.message))
                }
            }
        }

//        return makeRequest(request, serializer)
    }

    override fun <T : BorshCodable> getAccountInfo(
        account: PublicKey,
        decodeTo: Class<T>,
        onComplete: (Result<BufferInfo<T>>) -> Unit
    ) {
        // as an example, I am creating an anonymous NetworkingRouter object here
        // my intent is to show why its not really worth interacting through the NetworkingRouter
        Api(object : NetworkingRouter {
            override val endpoint = RPCEndpoint.devnetSolana
            override fun <T> request(method: String, params: List<Any>?, clazz: Type?,
                                     onComplete: (Result<T>) -> Unit) {

                // at this point we need to make the RPC request and return a T instance
                // so this method is responsible for both the RPC service, AND the deserialization
                // of that request. This makes it very difficult to decouple from moshi/BorshCodable

                // So one approach would be to manually serialize between moshi/BorshCobeable
                // so that we can use the new coroutine RPC implementation:
                CoroutineScope(Dispatchers.IO).launch {

                    // build a request object.
                    // It does not really matter if this is kotlin serializable or moshi,
                    // all that matters is that we can build the correct request body JSON
                    val request = SolanaAccountRequest(account.toBase58(), mapOf(
                        "commitment" to "max",
                        "encoding" to "base64"
                    ))

                    // We need a custom serializer to map between the borsh codeable framework
                    // used by the Connection interface, and the kotlin serialization stuff
                    val serializer = object : KSerializer<T> {
                        override val descriptor: SerialDescriptor
                            get() = TODO("Not yet implemented")

                        override fun deserialize(decoder: Decoder): T {
                            TODO("Not yet implemented")
                            // deserialize using moshi...
                            // moshi.adapter(...)
                            // adapter.fromJson(decoder.decodeString())
                            // etc.
                        }

                        override fun serialize(encoder: Encoder, value: T) {
                            TODO("Not yet implemented")
                            // we don't need to serialize here.
                        }
                    }

                    // now we can make the RPC call
                    with(JdkRpcDriver() as JsonRpcDriver) {
                        makeRequest(request, SolanaResponse.serializer()).let { response ->

                            // would need to find a way around type erasure here
//                            response.value(serializer)?.let { result ->
//                                onComplete(Result.success(result))
//                            }

                            response.error!!.let {
                                onComplete(Result.failure(Error(it.message)))
                            }
                        }
                    }
                }

                // Alternatively, we could just continue calling the existing OkHttpNetworkingRouter
                // but we would still need to map newer classes (AuctionHouse) into moshi/BorshCodable

                // This seems like a no win situation: either we have to find a way to map between
                // @Serializable and BorshCodeable, or we continue working with moshi for our models
            }
        })
    }

    override fun <T : BorshCodable> getProgramAccounts(
        account: PublicKey,
        programAccountConfig: ProgramAccountConfig,
        decodeTo: Class<T>,
        onComplete: (Result<List<ProgramAccount<T>>>) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun <T : BorshCodable> getMultipleAccountsInfo(
        accounts: List<PublicKey>,
        decodeTo: Class<T>,
        onComplete: (Result<List<BufferInfo<T>?>>) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun getSignatureStatuses(
        signatures: List<String>,
        configs: SignatureStatusRequestConfiguration?,
        onComplete: (Result<SignatureStatus>) -> Unit
    ) {
        TODO("Not yet implemented")
    }
}