/*
 * AuctionsTests
 * Metaplex
 * 
 * Created by Funkatronics on 7/27/2022
 */

@file:OptIn(ExperimentalCoroutinesApi::class)

package com.metaplex.lib.modules.auctions

import com.metaplex.data.TestDataProvider
import com.metaplex.data.model.TestAccountResponse
import com.metaplex.data.model.address
import com.metaplex.lib.drivers.solana.AccountRequest
import com.metaplex.lib.drivers.solana.SolanaConnectionDriver
import com.metaplex.lib.modules.auctions.models.AuctionHouse
import com.metaplex.mock.driver.rpc.MockRpcDriver
import com.solana.core.PublicKey
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class AuctionsTests {

    @Test
    fun testfindAuctionHouseByAddressReturnsKnownAuctionHouse() = runTest {
        // given
        val address = TestDataProvider.auctionHouse.address
        val expectedAuctionHouse = TestDataProvider.auctionHouse

        val client = AuctionsClient(SolanaConnectionDriver(MockRpcDriver().apply {
            willReturn(AccountRequest(address), TestAccountResponse(expectedAuctionHouse))
        }))

        // when
        var result = client.findAuctionHouseByAddress(PublicKey(address)).getOrNull()

        // then
        Assert.assertEquals(expectedAuctionHouse, result)
    }

    @Test
    fun testfindAuctionHouseByAddressReturnsNullForBadAddress() = runTest {
        // given
        val address = TestDataProvider.badAddress
        val client = AuctionsClient(SolanaConnectionDriver(MockRpcDriver()))

        // when
        var result = client.findAuctionHouseByAddress(PublicKey(address)).getOrNull()

        // then
        Assert.assertNull(result)
    }

    @Test
    fun testfindAuctionHouseByCreatorAndMintReturnsKnownAuctionHouse() = runTest {
        // given
        val creator = TestDataProvider.auctionHouse.creator
        val treasuryMint = TestDataProvider.auctionHouse.treasuryMint
        val pda = AuctionHouse.pda(creator, treasuryMint).toBase58()
        val expectedAuctionHouse = TestDataProvider.auctionHouse

        val client = AuctionsClient(SolanaConnectionDriver(MockRpcDriver().apply {
            willReturn(AccountRequest(pda), TestAccountResponse(expectedAuctionHouse))
        }))

        // when
        var result = client.findAuctionHouseByCreatorAndMint(creator, treasuryMint).getOrNull()

        // then
        Assert.assertEquals(expectedAuctionHouse, result)
    }

    @Test
    fun testfindAuctionHouseByCreatorAndMintReturnsNullForBadAddress() = runTest {
        // given
        val creator = PublicKey(TestDataProvider.badAddress)
        val treasuryMint = PublicKey(TestDataProvider.badAddress)

        val client = AuctionsClient(SolanaConnectionDriver(MockRpcDriver()))

        // when
        var result = client.findAuctionHouseByCreatorAndMint(creator, treasuryMint).getOrNull()

        // then
        Assert.assertNull(result)
    }
}