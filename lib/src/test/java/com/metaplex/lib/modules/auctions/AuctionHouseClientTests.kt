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
import com.metaplex.lib.modules.auctions.models.*
import com.metaplex.mock.driver.rpc.MockRpcDriver
import com.solana.core.Account
import com.solana.core.PublicKey
import com.solana.core.Transaction
import kotlinx.coroutines.*
import kotlinx.serialization.json.buildJsonObject
import org.junit.Assert.assertEquals
import org.junit.Test

class AuctionHouseClientTests {

//    @Before
//    fun prepare() {
//        jenerate()
//    }

    @Test
    fun testListingReturnsExpectedListing() {
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
        // there is a weird bug in the testing framework (or possibly in suspendCoroutine) that
        // is causing the Result.exceptionOrNull() (and similar Result methods) to crash due to
        // a cast exceptions (Result<Result<T>> cant be cast to Result<T>). For some reason,
        // using Any as the type here and removing the getOrNull() call allows the test to pass.
        // This is weird behavior, that will likely be fixed when we upgrade our Kotlin version.
        // There is a similar bug reported here: https://youtrack.jetbrains.com/issue/KT-41163
        // TODO: Revert the commented code here
        //var actualListing: Listing?
        var actualListing: Any?
        runBlocking {
//            actualListing = client.list(auctionHouse.treasuryMint, 1).getOrNull()
            actualListing = client.list(auctionHouse.treasuryMint, 1)
        }

        // then
        // this should not work, as actualListing should still be wrapped in a Result.Success
        // object here, whereas expectedListing is just a straight up Listing object **shrug**
        assertEquals(expectedListing, actualListing)
    }

    @Test
    fun testBidReturnsExpectedBid() {
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
        // there is a weird bug in the testing framework (or possibly in suspendCoroutine) that
        // is causing the Result.exceptionOrNull() (and similar Result methods) to crash due to
        // a cast exceptions (Result<Result<T>> cant be cast to Result<T>). For some reason,
        // using Any as the type here and removing the getOrNull() call allows the test to pass.
        // This is weird behavior, that will likely be fixed when we upgrade our Kotlin version.
        // There is a similar bug reported here: https://youtrack.jetbrains.com/issue/KT-41163
        // TODO: Revert the commented code here
        //var actualBid: Bid?
        var actualBid: Any?
        runBlocking {
//            actualBid = client.bid(auctionHouse.treasuryMint, 1).getOrNull()
            actualBid = client.bid(auctionHouse.treasuryMint, 1)
        }

        // then
        // this should not work, as actualBid should still be wrapped in a Result.Success
        // object here, whereas expectedBid is just a straight up Bid object **shrug**
        assertEquals(expectedBid, actualBid)
    }

    @Test
    fun testPurchaseReturnsExpectedPurchase() {
        // given
        val buyer = Account()
        val seller = Account()
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

        val asset = Asset(auctionHouse.treasuryMint,
            auctionHouse.tokenAccountPda(auctionHouse.authority), Account().publicKey)

        val listing = Listing(auctionHouse,
            mintAccount = auctionHouse.treasuryMint,
            seller = seller.publicKey,
            bookkeeper = seller.publicKey,
            price=1,
        )

        val bid = Bid(auctionHouse,
            mintAccount = auctionHouse.treasuryMint,
            buyer = buyer.publicKey,
            bookkeeper = buyer.publicKey,
            price=1,
        )

        val expectedPurchase = Purchase(auctionHouse, bid.bookkeeper, buyer.publicKey,
            seller.publicKey, asset, null, bid.buyerTradeState.address,
            listing.sellerTradeState.address, bid.price, bid.tokens)

        // when
        // there is a weird bug in the testing framework (or possibly in suspendCoroutine) that
        // is causing the Result.exceptionOrNull() (and similar Result methods) to crash due to
        // a cast exceptions (Result<Result<T>> cant be cast to Result<T>). For some reason,
        // using Any as the type here and removing the getOrNull() call allows the test to pass.
        // This is weird behavior, that will likely be fixed when we upgrade our Kotlin version.
        // There is a similar bug reported here: https://youtrack.jetbrains.com/issue/KT-41163
        // TODO: Revert the commented code here
        //var actualPurchase: Purchase?
        var actualPurchase: Any?
        runBlocking {
//            actualPurchase = client.executeSale(asset, listing, bid).getOrNull()
            actualPurchase = client.executeSale(asset, listing, bid)
        }

        // then
        // this should not work, as actualPurchase should still be wrapped in a Result.Success
        // object here, whereas expectedPurchase is just a straight up Purchase object **shrug**
        assertEquals(expectedPurchase, actualPurchase)
    }

    @Test
    fun testAuctioneerListingWithoutAuctioneerReturnsError() {
        // given
        val seller = Account()
        val rpcDriver = MockRpcDriver()
        val auctionHouse = TestDataProvider.auctionHouseWithAuctioneer

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

        val expectedError = Error("Auctioneer Authority Required")

        // when
        // there is a weird bug in the testing framework (or possibly in suspendCoroutine) that
        // is causing the Result.exceptionOrNull() (and similar Result methods) to crash due to
        // a cast exceptions (Result<Result<T>> cant be cast to Result<T>). For now we have to
        // manually wrap the result in another Result and extract the error. This is weird
        // behavior, that will likely be fixed when we upgrade our Kotlin version.
        // There is a similar bug reported here: https://youtrack.jetbrains.com/issue/KT-41163
        // TODO: Revert the commented code here
        var actualError: Throwable? = null
        runBlocking {
//            actualError = client.list(auctionHouse.treasuryMint, 1).exceptionOrNull()
            val result: Any? = client.list(auctionHouse.treasuryMint, 1)
            (Result.success(result)).apply {
                println("result = $this")
                this.onFailure {

                    println("failure = $this")
                    actualError = this.exceptionOrNull()
                }
            }
        }

        // then
        assertEquals(expectedError.message, actualError?.message)
    }

    @Test
    fun testAuctioneerBidWithoutAuctioneerReturnsError() {
        // given
        val buyer = Account()
        val rpcDriver = MockRpcDriver()
        val auctionHouse = TestDataProvider.auctionHouseWithAuctioneer

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

        val expectedError = Error("Auctioneer Authority Required")

        // when
        // there is a weird bug in the testing framework (or possibly in suspendCoroutine) that
        // is causing the Result.exceptionOrNull() (and similar Result methods) to crash due to
        // a cast exceptions (Result<Result<T>> cant be cast to Result<T>). For now we have to
        // manually wrap the result in another Result and extract the error. This is weird
        // behavior, that will likely be fixed when we upgrade our Kotlin version.
        // There is a similar bug reported here: https://youtrack.jetbrains.com/issue/KT-41163
        // TODO: Revert the commented code here
        var actualError: Throwable? = null
        runBlocking {
//            actualError = client.list(auctionHouse.treasuryMint, 1).exceptionOrNull()
            val result: Any? = client.bid(auctionHouse.treasuryMint, 1)
            (Result.success(result)).apply {
                println("result = $this")
                this.onFailure {

                    println("failure = $this")
                    actualError = this.exceptionOrNull()
                }
            }
        }

        // then
        assertEquals(expectedError.message, actualError?.message)
    }

    @Test
    fun testExecuteSaleWithoutAuctioneerReturnsError() {
        // given
        val buyer = Account()
        val seller = Account()
        val rpcDriver = MockRpcDriver()
        val auctionHouse = TestDataProvider.auctionHouse

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

        val asset = Asset(auctionHouse.treasuryMint,
            auctionHouse.tokenAccountPda(auctionHouse.authority), Account().publicKey)

        val listing = Listing(auctionHouse,
            mintAccount = auctionHouse.treasuryMint,
            seller = seller.publicKey,
            bookkeeper = seller.publicKey,
            price=1,
        )

        val bid = Bid(auctionHouse,
            mintAccount = auctionHouse.treasuryMint,
            buyer = buyer.publicKey,
            bookkeeper = buyer.publicKey,
            price=1,
        )

        val expectedError = Error("Auctioneer Authority Required")

        // when
        // there is a weird bug in the testing framework (or possibly in suspendCoroutine) that
        // is causing the Result.exceptionOrNull() (and similar Result methods) to crash due to
        // a cast exceptions (Result<Result<T>> cant be cast to Result<T>). For now we have to
        // manually wrap the result in another Result and extract the error. This is weird
        // behavior, that will likely be fixed when we upgrade our Kotlin version.
        // There is a similar bug reported here: https://youtrack.jetbrains.com/issue/KT-41163
        // TODO: Revert the commented code here
        var actualError: Throwable? = null
        runBlocking {
//            actualError = client.list(auctionHouse.treasuryMint, 1).exceptionOrNull()
            val result: Any? = client.executeSale(asset, listing, bid)
            (Result.success(result)).apply {
                println("result = $this")
                this.onFailure {

                    println("failure = $this")
                    actualError = this.exceptionOrNull()
                }
            }
        }

        // then
        assertEquals(expectedError.message, actualError?.message)
    }

    @Test
    fun testExecuteSaleWithDifferentMintsReturnsError() {
        // given
        val buyer = Account()
        val seller = Account()
        val rpcDriver = MockRpcDriver()
        val auctionHouse = TestDataProvider.auctionHouse

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

        val asset = Asset(auctionHouse.treasuryMint,
            auctionHouse.tokenAccountPda(auctionHouse.authority), Account().publicKey)

        val listing = Listing(auctionHouse,
            mintAccount = auctionHouse.treasuryMint,
            seller = seller.publicKey,
            bookkeeper = seller.publicKey,
            price=1,
        )

        val bid = Bid(auctionHouse,
            mintAccount = buyer.publicKey, // use the wrong mint address to trigger error
            buyer = buyer.publicKey,
            bookkeeper = buyer.publicKey,
            price=1,
        )

        val expectedError = Error("Bid And Listing Have Different Mints")

        // when
        // there is a weird bug in the testing framework (or possibly in suspendCoroutine) that
        // is causing the Result.exceptionOrNull() (and similar Result methods) to crash due to
        // a cast exceptions (Result<Result<T>> cant be cast to Result<T>). For now we have to
        // manually wrap the result in another Result and extract the error. This is weird
        // behavior, that will likely be fixed when we upgrade our Kotlin version.
        // There is a similar bug reported here: https://youtrack.jetbrains.com/issue/KT-41163
        // TODO: Revert the commented code here
        var actualError: Throwable? = null
        runBlocking {
//            actualError = client.list(auctionHouse.treasuryMint, 1).exceptionOrNull()
            val result: Any? = client.executeSale(asset, listing, bid)
            (Result.success(result)).apply {
                println("result = $this")
                this.onFailure {

                    println("failure = $this")
                    actualError = this.exceptionOrNull()
                }
            }
        }

        // then
        assertEquals(expectedError.message, actualError?.message)
    }

    @Test
    fun testExecuteSaleWithDifferentAuctionHousesReturnsError() {
        // given
        val buyer = Account()
        val seller = Account()
        val rpcDriver = MockRpcDriver()
        val auctionHouse1 = TestDataProvider.auctionHouse
        val auctionHouse2 = AuctionHouse(
            auctionHouseFeeAccount = Account().publicKey,
            auctionHouseTreasury = Account().publicKey,
            treasuryWithdrawalDestination = Account().publicKey,
            feeWithdrawalDestination = Account().publicKey,
            treasuryMint = PublicKey("So11111111111111111111111111111111111111112"),
            authority = buyer.publicKey,
            creator = buyer.publicKey,
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
            AuctionHouseClient(auctionHouse1, SolanaConnectionDriver(rpcDriver), mockIdentityDriver)

        val asset = Asset(auctionHouse1.treasuryMint,
            auctionHouse1.tokenAccountPda(auctionHouse1.authority), Account().publicKey)

        val listing = Listing(auctionHouse1,
            mintAccount = auctionHouse1.treasuryMint,
            seller = seller.publicKey,
            bookkeeper = seller.publicKey,
            price=1,
        )

        val bid = Bid(auctionHouse2,
            mintAccount = auctionHouse2.treasuryMint,
            buyer = buyer.publicKey,
            bookkeeper = buyer.publicKey,
            price=1,
        )

        val expectedError = Error("Bid And Listing Have Different Auction Houses")

        // when
        // there is a weird bug in the testing framework (or possibly in suspendCoroutine) that
        // is causing the Result.exceptionOrNull() (and similar Result methods) to crash due to
        // a cast exceptions (Result<Result<T>> cant be cast to Result<T>). For now we have to
        // manually wrap the result in another Result and extract the error. This is weird
        // behavior, that will likely be fixed when we upgrade our Kotlin version.
        // There is a similar bug reported here: https://youtrack.jetbrains.com/issue/KT-41163
        // TODO: Revert the commented code here
        var actualError: Throwable? = null
        runBlocking {
//            actualError = client.list(auctionHouse.treasuryMint, 1).exceptionOrNull()
            val result: Any? = client.executeSale(asset, listing, bid)
            (Result.success(result)).apply {
                println("result = $this")
                this.onFailure {

                    println("failure = $this")
                    actualError = this.exceptionOrNull()
                }
            }
        }

        // then
        assertEquals(expectedError.message, actualError?.message)
    }

}