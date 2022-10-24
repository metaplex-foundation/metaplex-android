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
import com.metaplex.data.model.publicKey
import com.metaplex.lib.MetaplexTestUtils
import com.metaplex.lib.drivers.indenty.KeypairIdentityDriver
import com.metaplex.lib.drivers.indenty.ReadOnlyIdentityDriver
import com.metaplex.lib.drivers.solana.AccountRequest
import com.metaplex.lib.drivers.solana.SolanaConnectionDriver
import com.metaplex.lib.generateConnectionDriver
import com.metaplex.lib.modules.auctions.models.AuctionHouse
import com.metaplex.lib.modules.auctions.models.address
import com.metaplex.mock.driver.rpc.MockRpcDriver
import com.solana.core.Account
import com.solana.core.HotAccount
import com.solana.core.PublicKey
import com.util.airdrop
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class AuctionsTests {

    //region UNIT
    @Test
    fun testfindAuctionHouseByAddressReturnsKnownAuctionHouse() = runTest {
        // given
        val address = TestDataProvider.auctionHouse.publicKey
        val auctionHouseAccount = TestDataProvider.auctionHouseAccount
        val expectedAuctionHouse = TestDataProvider.auctionHouse
        val connection = SolanaConnectionDriver(MockRpcDriver().apply {
            willReturn(AccountRequest(address), TestAccountResponse(auctionHouseAccount))
        })

        val client = AuctionsClient(connection,
            ReadOnlyIdentityDriver(HotAccount().publicKey, connection))

        // when
        var result = client.findAuctionHouseByAddress(PublicKey(address)).getOrNull()

        // then
        Assert.assertEquals(expectedAuctionHouse, result)
    }

    @Test
    fun testfindAuctionHouseByAddressReturnsNullForBadAddress() = runTest {
        // given
        val address = TestDataProvider.badAddress
        val connection = SolanaConnectionDriver(MockRpcDriver())
        val client = AuctionsClient(connection,
            ReadOnlyIdentityDriver(HotAccount().publicKey, connection))

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
        val pda = AuctionHouse.pda(creator, treasuryMint).address.toBase58()
        val auctionHouseAccount = TestDataProvider.auctionHouseAccount
        val expectedAuctionHouse = TestDataProvider.auctionHouse
        val connection = SolanaConnectionDriver(MockRpcDriver().apply {
            willReturn(AccountRequest(pda), TestAccountResponse(auctionHouseAccount))
        })

        val client = AuctionsClient(connection,
            ReadOnlyIdentityDriver(HotAccount().publicKey, connection))

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
        val connection = SolanaConnectionDriver(MockRpcDriver())
        val client = AuctionsClient(connection,
            ReadOnlyIdentityDriver(HotAccount().publicKey, connection))

        // when
        var result = client.findAuctionHouseByCreatorAndMint(creator, treasuryMint).getOrNull()

        // then
        Assert.assertNull(result)
    }
    //endregion

    //region INTEGRATION
    @Test
    fun testCreateNewAuctionHouseMinimumConfiguration() = runTest {
        // given
        val signer = HotAccount()
        val connection = MetaplexTestUtils.generateConnectionDriver()
        val identityDriver = KeypairIdentityDriver(signer, connection)
        val client = AuctionsClient(connection, identityDriver)

        // when
        connection.airdrop(signer.publicKey, 1f)
        val auctionHouse = client.createAuctionHouse(250).map {
            client.findAuctionHouseByAddress(it.address)
        }

        // then
        Assert.assertNotNull(auctionHouse)
    }
    //endregion
}