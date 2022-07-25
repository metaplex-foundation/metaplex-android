/*
 * SolanaRpcResponseTests
 * Metaplex
 * 
 * Created by Funkatronics on 7/22/2022
 */

package com.metaplex.lib.serialization.rpc.solana

import com.metaplex.lib.experimental.serialization.serializers.rpc.RpcResult
import com.metaplex.lib.experimental.serialization.serializers.rpc.solana.ContextualDataSerializer
import com.metaplex.lib.experimental.serialization.serializers.rpc.solana.SolanaResult
import com.metaplex.lib.experimental.serialization.serializers.rpc.solana.data
import com.metaplex.lib.experimental.serialization.serializers.solana.AnchorAccountSerializer
import com.metaplex.lib.modules.auctions.models.AuctionHouse
import com.solana.core.PublicKey
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.junit.Assert
import org.junit.Test

class SolanaRpcResponseTests {

    val jsonResponseString = "{" +
            "\"jsonrpc\":\"2.0\"," +
            "\"result\":{" +
            "\"context\":{" +
            "\"apiVersion\":\"1.10.29\"," +
            "\"slot\":149332850" +
            "}," +
            "\"value\":{" +
            "\"data\":[" +
            "\"KGzXa9VV9TC9WuTFjCbG0RR7CXM/o6Jb3sLJjqqmeNyI+uBYcn8mGLvtruiZlJOUc5O5XrLpZ+PbnWq5BpAKKCT7T9RhxMiSeA6Gza4qx8mdg9W+r6hT4lSELlTjkLzxG/JeSifEZqx4DobNrirHyZ2D1b6vqFPiVIQuVOOQvPEb8l5KJ8RmrAabiFf+q4GE+2h/Y0YYwDXaxDncGus7VZig8AAAAAABeA6Gza4qx8mdg9W+r6hT4lSELlTjkLzxG/JeSifEZqx4DobNrirHyZ2D1b6vqFPiVIQuVOOQvPEb8l5KJ8RmrP3+/MgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"," +
            "\"base64\"" +
            "]," +
            "\"executable\":false," +
            "\"lamports\":4085520," +
            "\"owner\":\"hausS13jsjafwWwGqZTUQRmWyvyxn9EQpqMwV1PBBmk\"," +
            "\"rentEpoch\":345" +
            "}" +
            "}," +
            "\"id\":1" +
            "}"

    val auctionHouse = AuctionHouse(
        auctionHouseFeeAccount = PublicKey("DkAScnZa6GqjXkPYPAU4kediZmR2EESHXutFzR4U6TGs"),
        auctionHouseTreasury = PublicKey("DebSyCbsnzMppVLt1umD4tUcJV6bSQW4z3nQVXQpWhCV"),
        treasuryWithdrawalDestination = PublicKey("95emj1a33Ei7B6ciu7gbPm7zRMRpFGs86g5nK5NiSdEK"),
        feeWithdrawalDestination = PublicKey("95emj1a33Ei7B6ciu7gbPm7zRMRpFGs86g5nK5NiSdEK"),
        treasuryMint = PublicKey("So11111111111111111111111111111111111111112"),
        authority = PublicKey("95emj1a33Ei7B6ciu7gbPm7zRMRpFGs86g5nK5NiSdEK"),
        creator = PublicKey("95emj1a33Ei7B6ciu7gbPm7zRMRpFGs86g5nK5NiSdEK"),
        bump = 253u,
        treasuryBump = 254u,
        feePayerBump = 252u,
        sellerFeeBasisPoints = 200u,
        requiresSignOff = false,
        canChangeSalePrice = false,
        escrowPaymentBump = 0u,
        hasAuctioneer = false,
        auctioneerPdaBump = 0u
    )

    val json = Json {
        ignoreUnknownKeys = true
        serializersModule = SerializersModule {
            contextual(ContextualDataSerializer(AnchorAccountSerializer<AuctionHouse>()))
        }
    }

    @Test
    fun testSolanaRpcDeserialize() {
        // given
        val responseJson = jsonResponseString
        val expectedResponse = RpcResult(
            SolanaResult(auctionHouse, false, 4085520,
                "hausS13jsjafwWwGqZTUQRmWyvyxn9EQpqMwV1PBBmk", 345)
        )

        // when
        val actualResponse = json.decodeFromString<RpcResult<SolanaResult<AuctionHouse>>>(responseJson)
        val actualAH: AuctionHouse? = actualResponse.data

        // then
        Assert.assertEquals(expectedResponse, actualResponse)
        Assert.assertEquals(auctionHouse, actualAH)
    }
}

val <T> RpcResult<SolanaResult<T>>.data get() = result?.data