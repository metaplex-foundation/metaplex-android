/*
 * SolanaRpcSerializers
 * Metaplex
 * 
 * Created by Funkatronics on 7/22/2022
 */

package com.metaplex.lib.drivers.solana

import com.metaplex.lib.drivers.rpc.RpcResponse
import com.metaplex.lib.experimental.serialization.serializers.base64.BorshAsBase64JsonArraySerializer
import com.metaplex.lib.experimental.serialization.serializers.solana.AnchorAccountSerializer
import com.solana.vendor.borshj.BorshCodable
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

// TODO: still not happy with the deserialization of the solana response
//  it's working, but I know i can improve the API/devex

@Serializable
data class AccountInfo<D>(val data: D?, val executable: Boolean,
                          val lamports: Long, val owner: String?, val rentEpoch: Long)

@Serializable
internal open class SolanaValue<V>(val value: V?)

internal class SolanaRpcResponse<A>(value: A?) : RpcResponse<SolanaValue<A>>()
internal typealias SolanaAccountResponse<A> = SolanaRpcResponse<AccountInfo<A>>

internal fun <A> SolanaValue.Companion.serializer(serializer: KSerializer<A>) =
    serializer(AccountInfo.serializer(
        BorshAsBase64JsonArraySerializer(
            if (serializer.descriptor.serialName == "BorshCodeable") serializer
            else AnchorAccountSerializer(serializer.descriptor.serialName, serializer)
        )
    ))

internal fun <A> SolanaAccountSerializer(serializer: KSerializer<A>) =
    SolanaValue.serializer(AccountInfo.serializer(
        BorshAsBase64JsonArraySerializer(
            if (serializer.descriptor.serialName == "BorshCodeable") serializer
            else AnchorAccountSerializer(serializer.descriptor.serialName, serializer)
        )
    ))

internal inline fun <reified A> SolanaAccountSerializer() =
    SolanaValue.serializer(AccountInfo.serializer<A?>(
        BorshAsBase64JsonArraySerializer(
            if (A::class is BorshCodable) serializer()
            else AnchorAccountSerializer()
        )
    ))