/*
 * FindAuctionHouseByAddressOperationTests
 * Metaplex
 * 
 * Created by Funkatronics on 10/21/2022
 */

@file:OptIn(ExperimentalCoroutinesApi::class)

package com.metaplex.lib.modules.auctions.operations

import com.metaplex.data.TestDataProvider
import com.metaplex.lib.drivers.solana.AccountInfo
import com.metaplex.lib.drivers.solana.AccountRequest
import com.metaplex.lib.drivers.solana.SolanaConnectionDriver
import com.metaplex.mock.driver.rpc.MockErrorRpcDriver
import com.metaplex.mock.driver.rpc.MockRpcDriver
import com.solana.core.Account
import com.solana.core.HotAccount
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class FindAuctionHouseByAddressOperationTests {

    @Test
    fun testFindAuctionHouseByAddressOperationReturnsAuctionHouseModel() = runTest {
        // given
        val expectedAH = TestDataProvider.auctionHouse
        val auctionHouseAddress = HotAccount().publicKey
        val rpcDriver = MockRpcDriver().apply {
            willReturn(AccountRequest(auctionHouseAddress.toBase58()),
                AccountInfo(TestDataProvider.auctionHouseAccount, false, 0, null, 0))
        }

        val connection = SolanaConnectionDriver(rpcDriver)

        // when
        val actualAH = FindAuctionHouseByAddressOperation(connection)
            .run(auctionHouseAddress).getOrThrow()

        // then
        Assert.assertEquals(expectedAH, actualAH)
    }

    @Test
    fun testFindAuctionHouseByAddressOperationHandlesError() = runTest {
        // given
        val auctionHouseAddress = HotAccount().publicKey
        val expectedErrorMessage = "An error occurred"
        val rpcDriver = MockErrorRpcDriver(expectedErrorMessage)
        val connection = SolanaConnectionDriver(rpcDriver)

        // when
        val actualResult = FindAuctionHouseByAddressOperation(connection)
            .run(auctionHouseAddress).exceptionOrNull()

        // then
        Assert.assertNotNull(actualResult)
        Assert.assertEquals(expectedErrorMessage, actualResult?.message)
    }
}