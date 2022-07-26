/*
 * RpcSerializers
 * Metaplex
 * 
 * Created by Funkatronics on 7/22/2022
 */

package com.metaplex.lib.experimental.serialization.serializers.rpc

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

class JsonRpcSerializer<D, ED>(val dataSerializer: KSerializer<D>,
                               errorDataSerializer: KSerializer<ED>) : KSerializer<RpcResponse> {

    private val errorSerializer = RpcErrorSerializer(errorDataSerializer)

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("RpcResponse") {
        element<String>("jsonrpc")
        element<Int?>("id", isOptional = true)
        element("result", dataSerializer.descriptor, isOptional = true)
        element("error", errorSerializer.descriptor, isOptional = true)
    }

    override fun serialize(encoder: Encoder, value: RpcResponse) {
        require(encoder is JsonEncoder)
        encoder.encodeJsonElement(buildJsonObject {
            put("jsonrpc", "2.0") // required for JSON-RPC 2.0 format
            when (value) {
                is RpcCallError ->
                    put("error", Json.encodeToJsonElement(errorSerializer, value))
                is RpcResult<*> ->
                    put("result", Json.encodeToJsonElement(dataSerializer, value.result as D))
            }
        })
    }
    override fun deserialize(decoder: Decoder): RpcResponse {
        require(decoder is JsonDecoder)
        decoder.decodeJsonElement().also { json: JsonElement ->
            json.jsonObject["jsonrpc"]?.jsonPrimitive?.apply {
                if (isString && content == "2.0")
                    json.jsonObject["result"]?.apply {
                        return RpcResult(Json.decodeFromJsonElement(dataSerializer, this))
                    } ?: json.jsonObject["error"]?.apply {
                        return Json.decodeFromJsonElement(errorSerializer, this)
                    }
            } ?: return RpcError(-2, "Input JSON is not JSON-RPC 2.0")
        }

        return RpcError(-1, "Unknown Error")
    }

    internal class RpcErrorSerializer<ED>(val errorDataSerializer: KSerializer<ED>)
        : JsonContentPolymorphicSerializer<RpcCallError>(RpcCallError::class) {
        override fun selectDeserializer(element: JsonElement)
                : DeserializationStrategy<out RpcCallError> =
            if (element.jsonObject.containsKey("data"))
                RpcErrorWithData.serializer(errorDataSerializer)
            else RpcError.serializer()
    }
}

