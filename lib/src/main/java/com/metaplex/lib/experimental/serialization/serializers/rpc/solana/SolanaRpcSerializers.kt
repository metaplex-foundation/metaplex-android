/*
 * SolanaRpcSerializers
 * Metaplex
 * 
 * Created by Marco Martinez on 7/22/2022
 * Copyright © Daxko, LLC 2022
 */

package com.metaplex.lib.experimental.serialization.serializers.rpc.solana

import com.metaplex.lib.experimental.serialization.serializers.base64.BorshAsBase64JsonArraySerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * This wrapper object is required to allow us to use generic types in the Solana RPC
 * serializers. This gives us the ability to (de)serialize any type of data in any format
 * using contextual serialization. Currently we are exploring alternatives to this approach.
 */
data class DataWrapper<D>(val data: D)

/**
 *
 */
internal class ContextualDataSerializer<T>(dataSerializer: KSerializer<T>):
    KSerializer<DataWrapper<T>> {
    private val delegateSerializer = BorshAsBase64JsonArraySerializer(dataSerializer)
    override val descriptor: SerialDescriptor = dataSerializer.descriptor

    override fun serialize(encoder: Encoder, value: DataWrapper<T>) =
        encoder.encodeSerializableValue(delegateSerializer, value.data)

    override fun deserialize(decoder: Decoder): DataWrapper<T> =
        DataWrapper(decoder.decodeSerializableValue(delegateSerializer))
}