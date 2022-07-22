/*
 * AuctionHouseTests
 * Metaplex
 * 
 * Created by Funkatronics on 7/19/2022
 */

package com.metaplex.lib.modules.auctions.models

import com.metaplex.lib.experimental.serialization.format.Borsh
import com.solana.core.PublicKey
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.junit.Assert
import org.junit.Test
import java.util.*

class AuctionHouseTests {

    @Test
    fun testMetadataAccountDecode() {
        // given
        val serializedMA =
            "KGzXa9VV9TAEtHzufKa8OAi/6IpK58sA4UvpebMwlRH09EjLPsCzRroWgGVI8DRPOstZE5Hr3Dbczhyy/k6" +
            "gHciqP/D1kj4rJyAAAABUcm9sZXggVGltZWtlZXBlciAjMzAyNwAAAAAAAAAAAAoAAABUUk9MRVgAAAAAyA" +
            "AAAGh0dHBzOi8vYXJ3ZWF2ZS5uZXQvbTJhMkduQlU0OUVVajJQWEpfbmJzLUxoRzBKOUNlQ05pNEdsYklub" +
            "TBaMAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"

        val expectedMA = MetadataAccount(
            key = 4u,
            update_authority = PublicKey("D9Ysnx4sNgkKsUSkRr5Dt8iTDMWraRDhjVkPX2hgx7Rj"),
            mint = PublicKey("2WqXLjv2gaFPRH22pKwYAQ6NbsX4fnYLEETCQmNu2Rfc"),
            data = MetaplexData(
                name = "Trolex Timekeeper #3027",
                symbol = "TROLEX",
                uri="https://arweave.net/m2a2GnBU49EUj2PXJ_nbs-LhG0J9CeCNi4GlbInm0Z0",
                sellerFeeBasisPoints = 0u,
                hasCreators = false,
                addressCount = 0u,
                creators = listOf()
            ),
            primarySaleHappened = false,
            isMutable = false,
            editionNonce = null,
            tokenStandard = null,
            collection = null
        )

            // when
        val deserializedMA = decodeFromBase64<MetadataAccount>(serializedMA)

        // then
//        Assert.assertTrue(expectedMA.equals(deserializedMA))
        Assert.assertEquals(expectedMA, deserializedMA)
    }

    @Test
    fun testAuctionHouseDecode() {
        // given
        val serializedAH =
            "KGzXa9VV9TC9WuTFjCbG0RR7CXM/o6Jb3sLJjqqmeNyI+uBYcn8mGLvtruiZlJOUc5O5XrLpZ+PbnWq5BpA" +
            "KKCT7T9RhxMiSeA6Gza4qx8mdg9W+r6hT4lSELlTjkLzxG/JeSifEZqx4DobNrirHyZ2D1b6vqFPiVIQuVO" +
            "OQvPEb8l5KJ8RmrAabiFf+q4GE+2h/Y0YYwDXaxDncGus7VZig8AAAAAABeA6Gza4qx8mdg9W+r6hT4lSEL" +
            "lTjkLzxG/JeSifEZqx4DobNrirHyZ2D1b6vqFPiVIQuVOOQvPEb8l5KJ8RmrP3+/MgAAAAAAAAAAAAAAAAA" +
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"

        val expectedAH = AuctionHouse(
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

        // when
        val deserializedAH = decodeFromBase64<AuctionHouse>(serializedAH)

        // then
        Assert.assertEquals(expectedAH, deserializedAH)
    }

    @Test
    fun testAuctionHouseEncode() {
        // given
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

        // when
        val serializedAH = encodeToBase64(auctionHouse)
        val deserializedAH = decodeFromBase64<AuctionHouse>(serializedAH)

        // then
        Assert.assertEquals(auctionHouse, deserializedAH)
    }

    fun <T> decodeFromBase64(theString: String, deserializer: DeserializationStrategy<T>): T {
        val bytes = Base64.getDecoder().decode(theString)
        return Borsh.decodeFromByteArray(deserializer, bytes)
    }

    inline fun <reified T> decodeFromBase64(theString: String): T = decodeFromBase64(theString, serializer())

    fun <T> encodeToBase64(theObject: T, serializer: SerializationStrategy<T>): String {
        val encodedBytes = Borsh.encodeToByteArray(serializer, theObject)
        return Base64.getEncoder().encodeToString(encodedBytes)
    }

    inline fun <reified T> encodeToBase64(theObject: T): String = encodeToBase64(theObject, serializer())
}