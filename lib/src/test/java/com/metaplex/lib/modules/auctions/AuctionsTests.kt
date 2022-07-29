/*
 * AuctionsTests
 * Metaplex
 * 
 * Created by Funkatronics on 7/27/2022
 */

package com.metaplex.lib.modules.auctions

import com.metaplex.lib.drivers.solana.SolanaConnectionDriver
import com.metaplex.lib.modules.auctions.models.AuctionHouse
import com.solana.core.PublicKey
import com.solana.networking.RPCEndpoint
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class AuctionsTests {

    // TODO: will add more tests and implement mocked network layer

    @Test
    fun testfindAuctionHouseByAddressReturnsKnownAuctionHouse() {
        // given
        val auctions = AuctionsClient(SolanaConnectionDriver(RPCEndpoint.devnetSolana))
        val address = PublicKey("5xN42RZCk7wA4GjQU2VVDhda8LBL8fAnrKZK921sybLF")
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
        var result: AuctionHouse?
        runBlocking {
            result = auctions.findAuctionHouseByAddress(address).getOrNull()
        }

        // then
        Assert.assertEquals(auctionHouse, result)
    }
}