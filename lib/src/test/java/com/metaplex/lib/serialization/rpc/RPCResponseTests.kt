package com.metaplex.lib.serialization.rpc

import com.metaplex.lib.experimental.serialization.serializers.rpc.*
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.*
import org.junit.Assert
import org.junit.Test

class RPCResponseTests {

    @Test
    fun testNonRpcJsonReturnsError() {
        // given
        val responseJson = "{ \"name\": \"Tim\", \"data\":{ \"id\": 1234 } }"
        val expectedResponse = RpcError(-2, "Input JSON is not JSON-RPC 2.0")

        // when
        val actualResponse = Json.decodeFromString(JsonRpcSerializer(Unit.serializer(), Unit.serializer()), responseJson)

        // then
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun testRpcDeserialization() {
        // given
        val responseJson = "{ \"jsonrpc\": \"2.0\", \"result\": \"this is a result\"}"
        val expectedResponse = RpcResult("this is a result")

        // when
        val actualResponse = Json.decodeFromString(JsonRpcSerializer(String.serializer(), Unit.serializer()), responseJson)

        // then
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun testRpcError() {
        // given
        val responseJson = "{ \"jsonrpc\": \"2.0\", \"error\":{\"code\": 1234, \"message\": \"error 1234\"}}"
        val expectedResponse = RpcError(1234, "error 1234")

        // when
        val actualResponse = Json.decodeFromString(JsonRpcSerializer(Unit.serializer(), Unit.serializer()), responseJson)

        // then
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun testRpcErrorWithData() {
        // given
        val responseJson = "{ \"jsonrpc\": \"2.0\", \"error\":{\"code\": 1234, \"message\": \"error 1234\", \"data\": 5678}}"
        val expectedResponse = RpcErrorWithData(1234, "error 1234", 5678)

        // when
        val actualResponse = Json.decodeFromString(JsonRpcSerializer(Unit.serializer(), Int.serializer()), responseJson)

        // then
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun testRpcErrorWithData2() {
        // given
        val responseJson = "{ \"jsonrpc\": \"2.0\", \"error\":{\"code\": 1234, \"message\": \"error 1234\", \"data\": \"some data\"}}"
        val expectedResponse = RpcErrorWithData(1234, "error 1234", "some data")

        // when
        val actualResponse = Json.decodeFromString(JsonRpcSerializer(Unit.serializer(), String.serializer()), responseJson)

        // then
        Assert.assertEquals(expectedResponse, actualResponse)
    }
}