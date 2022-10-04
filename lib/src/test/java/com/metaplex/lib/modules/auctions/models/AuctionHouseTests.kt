/*
 * AuctionHouseTests
 * Metaplex
 * 
 * Created by Funkatronics on 7/19/2022
 */

package com.metaplex.lib.modules.auctions.models

import com.metaplex.data.TestDataProvider
import com.metaplex.data.model.borsh
import com.metaplex.lib.serialization.format.Borsh
import com.metaplex.lib.serialization.serializers.solana.AnchorAccountSerializer
import org.junit.Assert
import org.junit.Test

class AuctionHouseTests {

    @Test
    fun testAuctionHouseDecodeFromBorsh() {
        // given
        val serializedAH = TestDataProvider.auctionHouse.borsh
        val expectedAH = TestDataProvider.auctionHouse

        // when
        val deserializedAH =
            Borsh.decodeFromByteArray<AuctionHouse>(AnchorAccountSerializer(), serializedAH)

        // then
        Assert.assertEquals(expectedAH, deserializedAH)
    }

    @Test
    fun testAuctionHouseEncode() {
        // given
        val expectedSerializedBorsh = TestDataProvider.auctionHouse.borsh
        val auctionHouse = TestDataProvider.auctionHouse

        // when
        val serializedAH = Borsh.encodeToByteArray(AnchorAccountSerializer(), auctionHouse)
        val deserializedAH =
            Borsh.decodeFromByteArray<AuctionHouse>(AnchorAccountSerializer(), serializedAH)

        // then
        Assert.assertArrayEquals(expectedSerializedBorsh, serializedAH)
        Assert.assertEquals(auctionHouse, deserializedAH)
    }
}