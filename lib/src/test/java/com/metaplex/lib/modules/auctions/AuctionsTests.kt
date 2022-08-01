/*
 * AuctionsTests
 * Metaplex
 * 
 * Created by Funkatronics on 7/27/2022
 */

package com.metaplex.lib.modules.auctions

import com.metaplex.lib.drivers.solana.AccountInfo
import com.metaplex.lib.drivers.solana.AccountRequest
import com.metaplex.lib.drivers.solana.SolanaConnectionDriver
import com.metaplex.lib.modules.auctions.models.AuctionHouse
import com.metaplex.mock.driver.rpc.MockRpcDriver
import com.solana.core.PublicKey
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class AuctionsTests {

    private val auctionHouse = AuctionHouse(
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

    @Test
    fun testfindAuctionHouseByAddressReturnsKnownAuctionHouse() {
        // given
        val address = "5xN42RZCk7wA4GjQU2VVDhda8LBL8fAnrKZK921sybLF"
        val expectedAuctionHouse = auctionHouse

        val client = AuctionsClient(SolanaConnectionDriver(MockRpcDriver().apply {
            willReturn(AccountRequest(address),
                AccountInfo(expectedAuctionHouse, false, 0, "", 0))
        }))

        // when
        var result: AuctionHouse?
        runBlocking {
            result = client.findAuctionHouseByAddress(PublicKey(address)).getOrNull()
        }

        // then
        Assert.assertEquals(expectedAuctionHouse, result)
    }

    @Test
    fun testfindAuctionHouseByAddressReturnsNullForBadAddress() {
        // given
        val address = "5xN42RZCk7wA4GjQU2VVDhda8LBL8fAnrKZK921sybLD"
        val client = AuctionsClient(SolanaConnectionDriver(MockRpcDriver()))

        // when
        var result: AuctionHouse?
        runBlocking {
            result = client.findAuctionHouseByAddress(PublicKey(address)).getOrNull()
        }

        // then
        Assert.assertNull(result)
    }

    @Test
    fun testfindAuctionHouseByCreatorAndMintReturnsKnownAuctionHouse() {
        // given
        val creatorAddress = "95emj1a33Ei7B6ciu7gbPm7zRMRpFGs86g5nK5NiSdEK"
        val mintAddress = "So11111111111111111111111111111111111111112"
        val pda = AuctionHouse.pda(PublicKey(creatorAddress), PublicKey(mintAddress)).toBase58()
        val expectedAuctionHouse = auctionHouse

        val client = AuctionsClient(SolanaConnectionDriver(MockRpcDriver().apply {
            willReturn(AccountRequest(pda),
                AccountInfo(expectedAuctionHouse, false, 0, "", 0))
        }))

        // when
        var result: AuctionHouse?
        runBlocking {
            result = client.findAuctionHouseByCreatorAndMint(
                PublicKey(creatorAddress), PublicKey(mintAddress)).getOrNull()
        }

        // then
        Assert.assertEquals(expectedAuctionHouse, result)
    }

    @Test
    fun testfindAuctionHouseByCreatorAndMintReturnsNullForBadAddress() {
        // given
        val creatorAddress = "95emj1a33Ei7B6ciu7gbPm7zRMRpFGs86g5nK5NiSdEK"
        val mintAddress = "So11111111111111111111111111111111111111113"

        val client = AuctionsClient(SolanaConnectionDriver(MockRpcDriver()))

        // when
        var result: AuctionHouse?
        runBlocking {
            result = client.findAuctionHouseByCreatorAndMint(
                PublicKey(creatorAddress), PublicKey(mintAddress)).getOrNull()
        }

        // then
        Assert.assertNull(result)
    }
}