/*
 * BorshSerializationTests
 * Metaplex
 * 
 * Created by Funkatronics on 7/25/2022
 */

package com.metaplex.lib.serialization.format

import com.metaplex.lib.experimental.serialization.format.Borsh
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.serializer
import org.junit.Assert
import org.junit.Test

class BorshSerializationTests {

    @Test
    fun testBorshEncodeDecode() {
        // given
        val testClass = BorshTestClass(
            200u, 200u, 200u, 200u,
            -100, -100, -100, -100,
            123.4f, 123.4,
            "borsh test string", BorshTestEnum.ENUM2,
            "optional string", 1234,
            listOf(1, 2, 3, 4), mapOf("map1" to 1, "map2" to 2, "map3" to 3),
            hashMapOf("hash1" to 1, "hash2" to 2, "hash3" to 3), hashSetOf(1, 2, 3),
             BorshTestSubclass("subclass", 1234)
        )

        // when
        val encodedBorsh = Borsh.encodeToByteArray(BorshTestClass.serializer(), testClass)
        val decodedObject = Borsh.decodeFromByteArray(BorshTestClass.serializer(), encodedBorsh)

        // then
        Assert.assertEquals(testClass, decodedObject)
    }

    @Test
    fun testBorshEncodeDecodeWithOptionals() {
        // given
        val testClass = BorshTestClass(
            200u, 200u, 200u, 200u,
            -100, -100, -100, -100,
            123.4f, 123.4,
            "borsh test string", BorshTestEnum.ENUM2,
            null, null,
            listOf(1, 2, 3, 4), mapOf("map1" to 1, "map2" to 2, "map3" to 3),
            hashMapOf("hash1" to 1, "hash2" to 2, "hash3" to 3), hashSetOf(1, 2, 3),
            BorshTestSubclass("subclass", 1234)
        )

        // when
        val encodedBorsh = Borsh.encodeToByteArray(BorshTestClass.serializer(), testClass)
        val decodedObject = Borsh.decodeFromByteArray(BorshTestClass.serializer(), encodedBorsh)

        // then
        Assert.assertEquals(testClass, decodedObject)
    }

    @Test
    fun testBorshEncodeFloatNaNThrowsError() {
        // given
        val testDouble = Float.NaN

        // when
        val actualError = runCatching {
            Borsh.encodeToByteArray(Float.serializer(), testDouble)
        }.exceptionOrNull()

        // then
        Assert.assertTrue(actualError is SerializationException)
    }

    @Test
    fun testBorshEncodeDoubleNaNThrowsError() {
        // given
        val testDouble = Double.NaN

        // when
        val actualError = runCatching {
            Borsh.encodeToByteArray(Double.serializer(), testDouble)
        }.exceptionOrNull()

        // then
        Assert.assertTrue(actualError is SerializationException)
    }
}