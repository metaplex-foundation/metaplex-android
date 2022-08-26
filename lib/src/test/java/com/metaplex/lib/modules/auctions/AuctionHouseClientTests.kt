/*
 * AuctionHouseClientTests
 * metaplex-android
 * 
 * Created by Funkatronics on 8/15/2022
 */

package com.metaplex.lib.modules.auctions

import com.metaplex.data.TestDataProvider
import com.metaplex.lib.drivers.indenty.IdentityDriver
import com.metaplex.lib.drivers.solana.BlockhashResponse
import com.metaplex.lib.drivers.solana.RecentBlockhashRequest
import com.metaplex.lib.drivers.solana.SolanaConnectionDriver
import com.metaplex.lib.modules.auctions.models.Bid
import com.metaplex.lib.modules.auctions.models.Listing
import com.metaplex.mock.driver.rpc.MockRpcDriver
import com.solana.core.Account
import com.solana.core.PublicKey
import com.solana.core.Transaction
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.buildJsonObject
import org.junit.Assert.assertEquals
import org.junit.Test

class AuctionHouseClientTests {

//    @Before
//    fun prepare() {
//        jenerate()
//    }

    inline fun <R, T> Result<T>.fold(
        onSuccess: (value: T) -> R,
        onFailure: (exception: Throwable) -> R
    ): R = when {
        isSuccess -> onSuccess(this.getOrThrow())
        isFailure -> onFailure(this.exceptionOrNull()!!)
        else -> onFailure(Error("Unkown Error occurred"))
    }

    fun <T> Result<T>.getOrNullSafe(): T? = runCatching {
        getOrNull()
    }.getOrElse {
        null
    }

    @Test
    fun testListingReturnsExpectedListingModel() {
        // given
        val seller = Account()
        val auctionHouse = TestDataProvider.auctionHouse
        val rpcDriver = MockRpcDriver().apply {
            willReturn(
                RecentBlockhashRequest(),
                BlockhashResponse(seller.publicKey.toBase58(), buildJsonObject {  })
            )
        }

        val mockIdentityDriver = object : IdentityDriver {
            override val publicKey: PublicKey = seller.publicKey

            override fun sendTransaction(
                transaction: Transaction,
                recentBlockHash: String?,
                onComplete: (Result<String>) -> Unit
            ) {
                onComplete(Result.success("transaction result"))
            }

            override fun signTransaction(
                transaction: Transaction,
                onComplete: (Result<Transaction>) -> Unit
            ) {
                transaction.sign(seller)
                onComplete(Result.success(transaction))
            }

            override fun signAllTransactions(
                transactions: List<Transaction>,
                onComplete: (Result<List<Transaction?>>) -> Unit
            ) = TODO("Not yet implemented")
        }

        val client =
            AuctionHouseClient(auctionHouse, SolanaConnectionDriver(rpcDriver), mockIdentityDriver)

        val expectedListing = Listing(auctionHouse,
            mintAccount = auctionHouse.treasuryMint,
            seller = seller.publicKey,
            bookkeeper = seller.publicKey,
            price=1,
        )

        // when
        var actualListing: Listing?
        runBlocking {
            actualListing = client.list(auctionHouse.treasuryMint, 1).getOrNullSafe()
        }

        // then
        assertEquals(expectedListing, actualListing)
    }

    @Test
    fun testBidReturnsExpectedBidModel() {
        // given
        val buyer = Account()
        val auctionHouse = TestDataProvider.auctionHouse
        val rpcDriver = MockRpcDriver().apply {
            willReturn(
                RecentBlockhashRequest(),
                BlockhashResponse(buyer.publicKey.toBase58(), buildJsonObject {  })
            )
        }

        val mockIdentityDriver = object : IdentityDriver {
            override val publicKey: PublicKey = buyer.publicKey

            override fun sendTransaction(
                transaction: Transaction,
                recentBlockHash: String?,
                onComplete: (Result<String>) -> Unit
            ) {
                onComplete(Result.success("transaction result"))
            }

            override fun signTransaction(
                transaction: Transaction,
                onComplete: (Result<Transaction>) -> Unit
            ) {
                transaction.sign(buyer)
                onComplete(Result.success(transaction))
            }

            override fun signAllTransactions(
                transactions: List<Transaction>,
                onComplete: (Result<List<Transaction?>>) -> Unit
            ) = TODO("Not yet implemented")
        }

        val client =
            AuctionHouseClient(auctionHouse, SolanaConnectionDriver(rpcDriver), mockIdentityDriver)

        val expectedBid = Bid(auctionHouse,
            mintAccount = auctionHouse.treasuryMint,
            buyer = buyer.publicKey,
            bookkeeper = buyer.publicKey,
            price=1,
        )

        // when
        var actualBid: Bid?
        runBlocking {
            actualBid = client.bid(auctionHouse.treasuryMint, 1).getOrNull()
        }

        // then
        assertEquals(expectedBid, actualBid)
    }

}