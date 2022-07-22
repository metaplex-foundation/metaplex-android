/*
 * PublicKeySerializers
 * Metaplex
 * 
 * Created by Funkatronics on 7/20/2022
 */

package com.metaplex.lib.experimental.serialization.serializers

import com.solana.core.PublicKey
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ByteArraySerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object PublicKeyAsByteArraySerializer : KSerializer<PublicKey> {

    private val delegateSerializer = ByteArraySerializer()

    // TODO: the recommended approach is to delegate the serializer as shown in the
    //  kotlinx.serialization docs, but we do not have the latest version so this method
    //  (SerialDescriptor(serialName, descriptor)) is not currently available to us.
    //  https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/serializers.md#delegating-serializers
//    override val descriptor: SerialDescriptor = SerialDescriptor("PublicKey", delegateSerializer.descriptor)
    override val descriptor: SerialDescriptor = delegateSerializer.descriptor

    override fun serialize(encoder: Encoder, value: PublicKey) =
        encoder.encodeSerializableValue(delegateSerializer, value.toByteArray())
//        value.toByteArray().forEach { b -> encoder.encodeByte(b) }

    override fun deserialize(decoder: Decoder): PublicKey =
//        PublicKey(decoder.decodeSerializableValue(delegateSerializer)) // does not work, i think we need to update plugin version
        PublicKey((0 until 32).map { decoder.decodeByte() }.toByteArray())
}