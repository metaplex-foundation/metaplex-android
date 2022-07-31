/*
 * SolanaRpcSerializers
 * Metaplex
 * 
 * Created by Funkatronics on 7/22/2022
 */

package com.metaplex.lib.drivers.solana

import com.metaplex.lib.experimental.serialization.serializers.base64.BorshAsBase64JsonArraySerializer
import com.metaplex.lib.experimental.serialization.serializers.solana.AnchorAccountSerializer
import com.solana.models.buffer.Buffer
import com.solana.models.buffer.BufferInfo
import com.solana.vendor.borshj.BorshCodable
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class AccountInfo<D>(val data: D?, val executable: Boolean,
                          val lamports: Long, val owner: String?, val rentEpoch: Long)

fun <D, T: BorshCodable> AccountInfo<D>.toBufferInfo() =
    BufferInfo(data?.let { Buffer(data as T) }, executable, lamports, owner, rentEpoch)

internal fun <A> SolanaAccountSerializer(serializer: KSerializer<A>) =
    AccountInfoSerializer(
        BorshAsBase64JsonArraySerializer(
            AnchorAccountSerializer(serializer.descriptor.serialName, serializer)
        )
    )

internal inline fun <reified A> SolanaAccountSerializer() =
    AccountInfoSerializer<A?>(BorshAsBase64JsonArraySerializer(AnchorAccountSerializer()))

internal class AccountInfoSerializer<D>(dataSerializer: KSerializer<D>)
    : KSerializer<AccountInfo<D>?> {
    private val serializer = WrappedValue.serializer(AccountInfo.serializer(dataSerializer))
    override val descriptor: SerialDescriptor = serializer.descriptor

    override fun serialize(encoder: Encoder, value: AccountInfo<D>?) =
        encoder.encodeSerializableValue(serializer, WrappedValue(value))

    override fun deserialize(decoder: Decoder): AccountInfo<D>? =
        decoder.decodeSerializableValue(serializer).value
}

@Serializable
private class WrappedValue<V>(val value: V?)

/*
 * The following code is alternative implementation that aliases the existing BufferInfo type
 * instead of creating a new AccountInfo type. This approach works, and the experience for callers
 * is about the same either way. We should discuss this and how we want to move forward.
 */
//typealias AccountInfo<D> = BufferInfo<D>
//
//fun <D> AccountInfo(data: D?, executable: Boolean, lamports: Long,
//                    owner: String?, rentEpoch: Long): AccountInfo<D> =
//    AccountInfo(Buffer(data), executable, lamports, owner, rentEpoch)
//
//val <D> AccountInfo<D>.account get() = this.data?.value
//
//internal fun <A> SolanaAccountSerializer(serializer: KSerializer<A>) =
//    AccountInfoSerializer(BorshAsBase64JsonArraySerializer(
//            AnchorAccountSerializer(serializer.descriptor.serialName, serializer)
//    ))
//
//internal inline fun <reified A> SolanaAccountSerializer() =
//    AccountInfoSerializer<A?>(BorshAsBase64JsonArraySerializer(AnchorAccountSerializer()))
//
//internal class AccountInfoSerializer<D>(dataSerializer: KSerializer<D>)
//    : KSerializer<AccountInfo<D>?> {
//    private val serializer = WrappedValue.serializer(AccountInfoJson.serializer(dataSerializer))
//    override val descriptor: SerialDescriptor = serializer.descriptor
//
//    override fun serialize(encoder: Encoder, value: AccountInfo<D>?) =
//        encoder.encodeSerializableValue(serializer, value.wrap())
//
//    override fun deserialize(decoder: Decoder): AccountInfo<D>? =
//        decoder.decodeSerializableValue(serializer).value?.unwrap()
//}
//
//@Serializable
//private class WrappedValue<V>(val value: V?)
//
//@Serializable
//private data class AccountInfoJson<D>(val data: D?, val executable: Boolean,
//                                      val lamports: Long, val owner: String?, val rentEpoch: Long)
//
//private fun <D> AccountInfoJson<D>.unwrap() =
//    AccountInfo(data, executable, lamports, owner, rentEpoch)
//
//private fun <D> AccountInfo<D>?.wrap() =
//    WrappedValue(this?.let { AccountInfoJson(data?.value, executable, lamports, owner, rentEpoch) })