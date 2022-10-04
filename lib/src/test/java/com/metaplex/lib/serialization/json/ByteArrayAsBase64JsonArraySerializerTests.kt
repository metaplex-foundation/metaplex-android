/*
 * ByteArrayAsBase64JsonArraySerializerTests
 * Metaplex
 * 
 * Created by Funkatronics on 7/22/2022
 */

package com.metaplex.lib.serialization.json

import com.metaplex.lib.serialization.serializers.base64.ByteArrayAsBase64JsonArraySerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.*
import org.junit.Assert
import org.junit.Test

class ByteArrayAsBase64JsonArraySerializerTests {

    @Test
    fun testByteArraySerializesToJsonStringArray() {
        // given
        val bytes = byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7)
        val expectedBase64String = "AAECAwQFBgc="
        val expectedJson = JsonArray(listOf(
            JsonPrimitive(expectedBase64String),
            JsonPrimitive("base64")
        ))

        // when
        val actualJson = Json.encodeToJsonElement(ByteArrayAsBase64JsonArraySerializer, bytes)

        // then
        Assert.assertEquals(expectedJson, actualJson)
    }

    @Test
    fun testByteArrayDeserializesFromJsonStringArray() {
        // given
        val base64String = "AAECAwQFBgc="
        val expectedBytes = byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7)
        val encodedJson = JsonArray(listOf(
            JsonPrimitive(base64String),
            JsonPrimitive("base64")
        ))

        // when
        val actualBytes =
            Json.decodeFromJsonElement(ByteArrayAsBase64JsonArraySerializer, encodedJson)

        // then
        Assert.assertArrayEquals(expectedBytes, actualBytes)
    }

    @Test
    fun testDeserializeFromNonBase64JsonArrayThrowsSerializationException() {
        // given
        val encodedJson = JsonArray(listOf(
            JsonPrimitive("some string"),
            JsonPrimitive("some other string")
        ))

        // when
        val actualError = runCatching {
            Json.decodeFromJsonElement(ByteArrayAsBase64JsonArraySerializer, encodedJson)
        }.exceptionOrNull()

        // then
        Assert.assertTrue(actualError is SerializationException)
    }

    @Test
    fun testDeserializeFromJsonObjectThrowsSerializationException() {
        // given
        val encodedJson = JsonObject(mapOf("data" to JsonPrimitive("some data")))

        // when
        val actualError = runCatching {
            Json.decodeFromJsonElement(ByteArrayAsBase64JsonArraySerializer, encodedJson)
        }.exceptionOrNull()

        // then
        Assert.assertTrue(actualError is SerializationException)
    }
}