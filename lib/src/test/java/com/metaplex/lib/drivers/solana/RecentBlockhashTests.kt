/*
 * RecentBlockhashTests
 * Metaplex
 * 
 * Created by Funkatronics on 8/24/2022
 */

@file:OptIn(ExperimentalCoroutinesApi::class)

package com.metaplex.lib.drivers.solana

import com.metaplex.mock.driver.rpc.MockRpcDriver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.intellij.lang.annotations.Language
import org.junit.Assert.assertEquals
import org.junit.Test

class RecentBlockhashTests {

    @Test
    fun testBlockhashSerializer() {
        // given
        val expectedBlockhash = BlockhashResponse("theBlockhashString", buildJsonObject {
            put("lamportsPerSignature", 0L)
        })

        @Language("json")
        val responseJson = """
            {
              "value": {
                "blockhash": "${expectedBlockhash.blockhash}",
                "feeCalculator": ${expectedBlockhash.feeCalculator}
              }
            }
        """.trimIndent()

        // when
        val deserializedBlockhash = Json.decodeFromString(BlockhashSerializer(), responseJson)

        // then
        assertEquals(expectedBlockhash, deserializedBlockhash)
    }

    @Test
    fun testRecentBlockhashRequest() = runTest {
        // given
        val rpcService = MockRpcDriver(autoHandleBlockhashRequest = false)
        val blockhashRequest = RecentBlockhashRequest()
        val expectedBlockhash = BlockhashResponse("wooweeLookAtThisBlockhash", buildJsonObject {})

        rpcService.willReturn(blockhashRequest, expectedBlockhash)

        //when
        var result = rpcService.makeRequest(RecentBlockhashRequest(), BlockhashSerializer()).result

        // then
        assertEquals(expectedBlockhash, result)
    }
}