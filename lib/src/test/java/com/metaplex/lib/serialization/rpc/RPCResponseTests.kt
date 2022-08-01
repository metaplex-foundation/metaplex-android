package com.metaplex.lib.serialization.rpc

import com.metaplex.lib.drivers.rpc.RpcError
import com.metaplex.lib.drivers.rpc.RpcResponse
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.*
import org.intellij.lang.annotations.Language
import org.junit.Assert
import org.junit.Test

class RPCResponseTests {

//    @Test
//    fun testNonRpcJsonReturnsError() {
//        // given
//        @Language("json")
//        val responseJson = """
//            { "name": "Tim", "data":{ "id": 1234 } }
//        """.trimIndent()
//        val expectedResponse = RpcError(-2, "Input JSON is not JSON-RPC 2.0")
//
//        // when
//        val actualResponse = Json.decodeFromString(RpcResponse.serializer(Unit.serializer()), responseJson)
//
//        // then
//        Assert.assertEquals(expectedResponse, actualResponse)
//    }

    @Test
    fun testRpcDeserialization() {
        // given
        @Language("json")
        val responseJson = """
            { "jsonrpc": "2.0", "result": "this is a result"}
        """.trimIndent()
        val expectedResponse = RpcResponse("this is a result")

        // when
        val actualResponse = Json.decodeFromString(RpcResponse.serializer(String.serializer()), responseJson)

        // then
        Assert.assertEquals(expectedResponse.result, actualResponse.result)
    }

    @Test
    fun testRpcError() {
        // given
        @Language("json")
        val responseJson = """
            { "jsonrpc": "2.0", "error":{"code": 1234, "message": "error 1234"}}
        """.trimIndent()
        val expectedError = RpcError(1234, "error 1234")

        // when
        val actualResponse = Json.decodeFromString(RpcResponse.serializer(Unit.serializer()), responseJson)

        // then
        Assert.assertEquals(expectedError, actualResponse.error)
        Assert.assertNull(actualResponse.result)
    }

//    @Test
//    fun testRpcErrorWithData() {
//        // given
//        @Language("json")
//        val responseJson = """
//            { "jsonrpc": "2.0", "error":{"code": 1234, "message": "error 1234", "data": 5678}}
//        """.trimIndent()
//        val expectedResponse = RpcError(1234, "error 1234", 5678)
//
//        // when
//        val actualResponse = Json.decodeFromString(RpcResponse.serializer(Unit.serializer()), responseJson)
//
//        // then
//        Assert.assertEquals(expectedResponse, actualResponse)
//    }
//
//    @Test
//    fun testRpcErrorWithData2() {
//        // given
//        @Language("json")
//        val responseJson = """
//            { "jsonrpc": "2.0", "error":{"code": 1234, "message": "error 1234", "data": "some data"}}
//        """.trimIndent()
//        val expectedResponse = RpcErrorWithData(1234, "error 1234", 5678)
//
//        // when
//        val actualResponse = Json.decodeFromString(RpcResponse.serializer(Unit.serializer()), responseJson)
//
//        // then
//        Assert.assertEquals(expectedResponse, actualResponse)
//    }
}