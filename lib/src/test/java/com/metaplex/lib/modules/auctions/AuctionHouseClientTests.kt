/*
 * AuctionHouseClientTests
 * metaplex-android
 * 
 * Created by Funkatronics on 8/15/2022
 */

@file:OptIn(ExperimentalCoroutinesApi::class)

package com.metaplex.lib.modules.auctions

import com.metaplex.data.TestDataProvider
import com.metaplex.lib.drivers.indenty.IdentityDriver
import com.metaplex.lib.drivers.solana.BlockhashResponse
import com.metaplex.lib.drivers.solana.RecentBlockhashRequest
import com.metaplex.lib.drivers.solana.SolanaConnectionDriver
import com.metaplex.lib.modules.auctions.models.*
import com.metaplex.lib.programs.token_metadata.accounts.MetadataAccount
import com.metaplex.mock.driver.rpc.MockRpcDriver
import com.solana.core.Account
import com.solana.core.PublicKey
import com.solana.core.Transaction
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.buildJsonObject
import org.junit.Assert.assertEquals
import org.junit.Test

class AuctionHouseClientTests {

//    @Before
//    fun prepare() {
//        jenerate()
//    }

    @Test
    fun testListingReturnsExpectedListing() = runTest {
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
        val actualListing: Listing? = client.list(auctionHouse.treasuryMint, 1).getOrNull()

        // then
        assertEquals(expectedListing, actualListing)
    }

    @Test
    fun testBidReturnsExpectedBid() = runTest {
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
        val actualBid: Bid? = client.bid(auctionHouse.treasuryMint, 1).getOrNull()

        // then
        assertEquals(expectedBid, actualBid)
    }

    @Test
    fun testPurchaseReturnsExpectedPurchase() = runTest {
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
            seller.publicKey, auctionHouse.treasuryMint, null,
            bid.buyerTradeState.address, listing.sellerTradeState.address, bid.price, bid.tokens)

        // when
        val actualPurchase: Purchase? = client.executeSale(listing, bid).getOrNull()

        // then
        assertEquals(expectedPurchase, actualPurchase)
    }

    @Test
    fun testAuctioneerListingWithoutAuctioneerReturnsError() = runTest {
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
        val actualError = client.list(auctionHouse.treasuryMint, 1).exceptionOrNull()

        // then
        assertEquals(expectedError.message, actualError?.message)
    }

    @Test
    fun testAuctioneerBidWithoutAuctioneerReturnsError() = runTest {
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
        val actualError = client.list(auctionHouse.treasuryMint, 1).exceptionOrNull()

        // then
        assertEquals(expectedError.message, actualError?.message)
    }

    @Test
    fun testExecuteSaleWithoutAuctioneerReturnsError() = runTest {
        // given
        val buyer = Account()
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
        val actualError = client.list(auctionHouse.treasuryMint, 1).exceptionOrNull()

        // then
        assertEquals(expectedError.message, actualError?.message)
    }

    @Test
    fun testExecuteSaleWithDifferentMintsReturnsError() = runTest {
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
        val actualError = client.executeSale(listing, bid).exceptionOrNull()

        // then
        assertEquals(expectedError.message, actualError?.message)
    }

    @Test
    fun testExecuteSaleWithDifferentAuctionHousesReturnsError() = runTest {
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
        val actualError = client.executeSale(listing, bid).exceptionOrNull()

        // then
        assertEquals(expectedError.message, actualError?.message)
    }

}