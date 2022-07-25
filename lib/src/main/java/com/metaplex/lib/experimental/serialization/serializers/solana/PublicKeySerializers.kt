/*
 * PublicKeySerializers
 * Metaplex
 * 
 * Created by Funkatronics on 7/20/2022
 */

package com.metaplex.lib.experimental.serialization.serializers.solana

import com.solana.core.PublicKey
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object PublicKeyAs32ByteSerializer : KSerializer<PublicKey> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("PublicKey")

    override fun serialize(encoder: Encoder, value: PublicKey) =
        value.toByteArray().forEach { b -> encoder.encodeByte(b) }

    override fun deserialize(decoder: Decoder): PublicKey =
        PublicKey((0 until 32).map { decoder.decodeByte() }.toByteArray())
}