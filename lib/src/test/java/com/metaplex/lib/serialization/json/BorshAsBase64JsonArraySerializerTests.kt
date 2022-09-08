/*
 * Borsh
 * Metaplex
 * 
 * Created by Funkatronics on 7/22/2022
 */

package com.metaplex.lib.serialization.json

import com.metaplex.lib.serialization.serializers.base64.BorshAsBase64JsonArraySerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import org.junit.Assert
import org.junit.Test

class BorshAsBase64JsonArraySerializerTests {

    @Serializable
    internal data class MockSerializableObject(val name: String, val value: Int)

    private val mockObjectSerializer get() =
        BorshAsBase64JsonArraySerializer(MockSerializableObject.serializer())

    @Test
    fun testByteArraySerializesToJsonStringArray() {
        // given
        val serializableObject = MockSerializableObject("test object", 1234)
        val expectedBase64String = "CwAAAHRlc3Qgb2JqZWN00gQAAA=="
        val expectedJson = JsonArray(listOf(
            JsonPrimitive(expectedBase64String),
            JsonPrimitive("base64")
        ))

        // when
        val actualJson = Json.encodeToJsonElement(mockObjectSerializer,serializableObject)

        // then
        Assert.assertEquals(expectedJson, actualJson)
    }

    @Test
    fun testByteArrayDeserializesFromJsonStringArray() {
        // given
        val base64String = "CwAAAHRlc3Qgb2JqZWN00gQAAA=="
        val serializableObject = MockSerializableObject("test object", 1234)
        val encodedJson = JsonArray(listOf(
            JsonPrimitive(base64String),
            JsonPrimitive("base64")
        ))

        // when
        val actualDeserializedObject =
            Json.decodeFromJsonElement(mockObjectSerializer, encodedJson)

        // then
        Assert.assertEquals(serializableObject, actualDeserializedObject)
    }
}