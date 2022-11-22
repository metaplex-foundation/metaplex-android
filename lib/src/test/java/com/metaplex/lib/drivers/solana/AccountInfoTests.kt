/*
 * SolanaRpcResponseTests
 * Metaplex
 * 
 * Created by Funkatronics on 7/22/2022
 */

package com.metaplex.lib.drivers.solana

import com.metaplex.data.TestDataProvider
import com.metaplex.data.model.json
import com.metaplex.data.model.responseJson
import com.metaplex.lib.serialization.serializers.base64.BorshAsBase64JsonArraySerializer
import com.metaplex.lib.serialization.serializers.solana.AnchorAccountSerializer
import com.metaplex.lib.modules.auctions.models.AuctionHouse
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.junit.Assert
import org.junit.Test

class AccountInfoTests {

    @Test
    fun testAccountInfoDeserialize() {
        // given
        val expectedResponse =
            AccountInfo("the data", false, 123456, "the owner", 123)

        // when
        val actualResponse = Json.decodeFromString<AccountInfo<String>>(expectedResponse.json(serializer()))

        // then
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun testSolanaAccountSerializer() {
        // given
        @Serializable data class TestAccount(val data: String)
        val serializer = SolanaAccountSerializer(TestAccount.serializer())
        val expectedResponse = AccountInfo(TestAccount("test data"),
            false, 123456, "the owner", 123)

        // when
        val actualResponse = Json.decodeFromString(serializer, expectedResponse.responseJson(
            BorshAsBase64JsonArraySerializer(TestAccount.serializer())
        ))

        // then
        Assert.assertEquals(expectedResponse, actualResponse)
    }
}