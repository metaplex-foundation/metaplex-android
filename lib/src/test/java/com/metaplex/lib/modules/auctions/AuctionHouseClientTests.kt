/*
 * AuctionHouseClientTests
 * metaplex-android
 * 
 * Created by Funkatronics on 8/15/2022
 */

package com.metaplex.lib.modules.auctions

import android.util.Base64
import com.metaplex.data.TestDataProvider
import com.metaplex.lib.drivers.indenty.ReadOnlyIdentityDriver
import com.metaplex.lib.drivers.solana.SolanaConnectionDriver
import com.metaplex.mock.driver.rpc.MockRpcDriver
import com.solana.core.Account
import com.solana.networking.RPCEndpoint
import org.junit.Test

class AuctionHouseClientTests {

//    @Before
//    fun prepare() {
//        jenerate()
//    }

    @Test
    fun testBidPrelim() {
        // given
        val buyer = Account()
        val auctionHouse = TestDataProvider.auctionHouse
        val client = AuctionHouseClient(auctionHouse,
            SolanaConnectionDriver(MockRpcDriver()),
            ReadOnlyIdentityDriver(buyer.publicKey, com.metaplex.lib.solana.SolanaConnectionDriver(RPCEndpoint.mainnetBetaSolana).solanaRPC)
        )

        val tx = client.bid(auctionHouse.treasuryMint, 1).apply {
            setRecentBlockHash("1234")
            sign(buyer)

            println(this)

            println(Base64.encodeToString(this.serialize(), 0))
        }
    }

}