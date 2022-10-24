/*
 * TestDataProvider
 * Metaplex
 *
 * Created by Funkatronics on 8/2/2022
 */
package com.metaplex.data

import com.metaplex.data.model.TestAuctionHouse
import com.metaplex.data.model.TestAuctionHouseAccount

object TestDataProvider {

    val auctionHouse get() = TestAuctionHouse()
    val auctionHouseAccount get() = TestAuctionHouseAccount()
    val auctionHouseWithAuctioneer get() = TestAuctionHouse(true)

    val badAddress = "So11111111111111111111111111111111111111113"
}