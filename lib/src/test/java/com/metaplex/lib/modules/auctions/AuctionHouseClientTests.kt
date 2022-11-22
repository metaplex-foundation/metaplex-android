/*
 * AuctionHouseClientTests
 * metaplex-android
 * 
 * Created by Funkatronics on 8/15/2022
 */

@file:OptIn(ExperimentalCoroutinesApi::class)

package com.metaplex.lib.modules.auctions

import com.metaplex.data.TestDataProvider
import com.metaplex.lib.MetaplexTestUtils
import com.metaplex.lib.drivers.indenty.IdentityDriver
import com.metaplex.lib.drivers.indenty.KeypairIdentityDriver
import com.metaplex.lib.drivers.solana.AccountInfo
import com.metaplex.lib.drivers.solana.AccountRequest
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.drivers.solana.SolanaConnectionDriver
import com.metaplex.lib.generateConnectionDriver
import com.metaplex.lib.modules.auctions.models.*
import com.metaplex.lib.modules.nfts.NftClient
import com.metaplex.lib.modules.nfts.models.Metadata
import com.metaplex.lib.programs.token_metadata.MetadataKey
import com.metaplex.lib.programs.token_metadata.accounts.MetadataAccount
import com.metaplex.lib.programs.token_metadata.accounts.MetaplexData
import com.metaplex.mock.driver.rpc.MockRpcDriver
import com.solana.core.HotAccount
import com.util.airdrop
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class AuctionHouseClientTests {

//    @Before
//    fun prepare() {
//        jenerateAuctionHouse()
//    }

    //region UNIT
    @Test
    fun testListingReturnsExpectedListing() = runTest {
        // given
        val seller = HotAccount()
        val auctionHouse = TestDataProvider.auctionHouse
        val rpcDriver = MockRpcDriver(autoConfirmTransactions = true)
        val connection = SolanaConnectionDriver(rpcDriver)
        val client =
            AuctionHouseClient(auctionHouse, connection, KeypairIdentityDriver(seller, connection))

        val expectedListing = Listing(auctionHouse,
            mintAccount = auctionHouse.treasuryMint,
            seller = seller.publicKey,
            bookkeeper = seller.publicKey,
            price=1,
        )

        // when
        val actualListing: Listing? = client.list(auctionHouse.treasuryMint, 1).getOrNull()

        // then
        Assert.assertEquals(expectedListing, actualListing)
    }

    @Test
    fun testBidReturnsExpectedBid() = runTest {
        // given
        val buyer = HotAccount()
        val auctionHouse = TestDataProvider.auctionHouse
        val rpcDriver = MockRpcDriver(autoConfirmTransactions = true)
        val connection = SolanaConnectionDriver(rpcDriver)
        val client =
            AuctionHouseClient(auctionHouse, connection, KeypairIdentityDriver(buyer, connection))

        val expectedBid = Bid(auctionHouse,
            mintAccount = auctionHouse.treasuryMint,
            buyer = buyer.publicKey,
            bookkeeper = buyer.publicKey,
            price=1,
        )

        // when
        val actualBid: Bid? = client.bid(auctionHouse.treasuryMint, 1).getOrNull()

        // then
        Assert.assertEquals(expectedBid, actualBid)
    }

    @Test
    fun testPurchaseReturnsExpectedPurchase() = runTest {
        // given
        val buyer = HotAccount()
        val seller = HotAccount()
        val asset = HotAccount()
        val auctionHouse = TestDataProvider.auctionHouse
        val rpcDriver = MockRpcDriver(autoConfirmTransactions = true).apply {
            // this mocking is annoying, need to find a cleaner way to set this up
            willReturn(AccountRequest(MetadataAccount.pda(asset.publicKey).getOrThrows().toBase58()),
                AccountInfo(MetadataAccount(MetadataKey.MetadataV1.ordinal.toByte(),
                    seller.publicKey, asset.publicKey,
                    MetaplexData("", "", "", 250, arrayOf()),
                    true, false, null, null, null)
                , false, 0, null, 0))
        }

        val connection = SolanaConnectionDriver(rpcDriver)
        val client =
            AuctionHouseClient(auctionHouse, connection, KeypairIdentityDriver(buyer, connection))

        val listing = Listing(auctionHouse,
            mintAccount = asset.publicKey,
            seller = seller.publicKey,
            bookkeeper = seller.publicKey,
            price=1,
        )

        val bid = Bid(auctionHouse,
            mintAccount = asset.publicKey,
            buyer = buyer.publicKey,
            bookkeeper = buyer.publicKey,
            price=1,
        )

        val expectedPurchase = Purchase(auctionHouse, bid.bookkeeper, buyer.publicKey,
            seller.publicKey, asset.publicKey, null,
            bid.buyerTradeState.address, listing.sellerTradeState.address, bid.price, bid.tokens)

        // when
        val actualPurchase: Purchase? = client.executeSale(listing, bid).getOrNull()

        // then
        Assert.assertEquals(expectedPurchase, actualPurchase)
    }

    @Test
    fun testAuctioneerListingWithoutAuctioneerReturnsError() = runTest {
        // given
        val seller = HotAccount()
        val auctionHouse = TestDataProvider.auctionHouseWithAuctioneer
        val rpcDriver = MockRpcDriver(autoConfirmTransactions = true)
        val connection = SolanaConnectionDriver(rpcDriver)
        val client =
            AuctionHouseClient(auctionHouse, connection, KeypairIdentityDriver(seller, connection))

        val expectedError = Error("Auctioneer Authority Required")

        // when
        val actualError = client.list(auctionHouse.treasuryMint, 1).exceptionOrNull()

        // then
        Assert.assertEquals(expectedError.message, actualError?.message)
    }

    @Test
    fun testAuctioneerBidWithoutAuctioneerReturnsError() = runTest {
        // given
        val buyer = HotAccount()
        val auctionHouse = TestDataProvider.auctionHouseWithAuctioneer
        val rpcDriver = MockRpcDriver(autoConfirmTransactions = true)
        val connection = SolanaConnectionDriver(rpcDriver)
        val client =
            AuctionHouseClient(auctionHouse, connection, KeypairIdentityDriver(buyer, connection))

        val expectedError = Error("Auctioneer Authority Required")

        // when
        val actualError = client.bid(auctionHouse.treasuryMint, 1).exceptionOrNull()

        // then
        Assert.assertEquals(expectedError.message, actualError?.message)
    }

    @Test
    fun testExecuteSaleWithoutAuctioneerReturnsError() = runTest {
        // given
        val buyer = HotAccount()
        val seller = HotAccount()
        val auctionHouse = TestDataProvider.auctionHouseWithAuctioneer
        val rpcDriver = MockRpcDriver(autoConfirmTransactions = true)
        val connection = SolanaConnectionDriver(rpcDriver)
        val client =
            AuctionHouseClient(auctionHouse, connection, KeypairIdentityDriver(buyer, connection))

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
        val actualError = client.executeSale(listing, bid).exceptionOrNull()

        // then
        Assert.assertEquals(expectedError.message, actualError?.message)
    }

    @Test
    fun testExecuteSaleWithDifferentMintsReturnsError() = runTest {
        // given
        val buyer = HotAccount()
        val seller = HotAccount()
        val auctionHouse = TestDataProvider.auctionHouse
        val rpcDriver = MockRpcDriver(autoConfirmTransactions = true)
        val connection = SolanaConnectionDriver(rpcDriver)
        val client =
            AuctionHouseClient(auctionHouse, connection, KeypairIdentityDriver(seller, connection))

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
        Assert.assertEquals(expectedError.message, actualError?.message)
    }

    @Test
    fun testExecuteSaleWithDifferentAuctionHousesReturnsError() = runTest {
        // given
        val buyer = HotAccount()
        val seller = HotAccount()
        val auctionHouse1 = TestDataProvider.auctionHouse
        val rpcDriver = MockRpcDriver(autoConfirmTransactions = true)
        val connection = SolanaConnectionDriver(rpcDriver)
        val client =
            AuctionHouseClient(auctionHouse1, connection, KeypairIdentityDriver(seller, connection))

        val auctionHouse2 = AuctionHouse(
            treasuryWithdrawalDestinationOwner = seller.publicKey,
            feeWithdrawalDestination = seller.publicKey,
            authority = seller.publicKey,
            creator = seller.publicKey,
            sellerFeeBasisPoints = 200u,
        )

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
        Assert.assertEquals(expectedError.message, actualError?.message)
    }
    //endregion

    //region INTEGRATION
    @Test
    fun testCreatePublicListingOnAuctionHouse() = runTest {
        // given
        val seller = HotAccount()
        val connection = MetaplexTestUtils.generateConnectionDriver()
        val identityDriver = KeypairIdentityDriver(seller, connection)
        val auctionsClient = AuctionsClient(connection, identityDriver)

        // when
        connection.airdrop(seller.publicKey, 1f)

        // create nft to list
        val nft = createNft(connection, identityDriver).getOrThrow()

        // create auction house
        val auctionHouse = auctionsClient.createAuctionHouse(250).getOrThrow()

        // fund AH fee account (should this be added to the create auction house client operation?)
        connection.airdrop(auctionHouse.auctionHouseFeeAccount, 1f)

        val createdListing = AuctionHouseClient(auctionHouse, connection, identityDriver)
            .list(nft.mint, 100000L).getOrThrow()

        val onChainListing =
            auctionsClient.findListingByReceipt(createdListing.receiptAddress.address).getOrThrow()

        // then
        Assert.assertEquals(createdListing, onChainListing)
    }

    @Test
    fun testCancelPublicListingOnAuctionHouse() = runTest {
        // given
        val seller = HotAccount()
        val connection = MetaplexTestUtils.generateConnectionDriver()
        val identityDriver = KeypairIdentityDriver(seller, connection)
        val auctionsClient = AuctionsClient(connection, identityDriver)

        // when
        connection.airdrop(seller.publicKey, 1f)

        // create nft to list
        val nft = createNft(connection, identityDriver).getOrThrow()

        // create auction house
        val auctionHouse = auctionsClient.createAuctionHouse(250).getOrThrow()

        // fund AH fee account (should this be added to the create auction house client operation?)
        connection.airdrop(auctionHouse.auctionHouseFeeAccount, 1f)

        val client = AuctionHouseClient(auctionHouse, connection, identityDriver)
        val createdListing = client.list(nft.mint, 100000L).getOrThrow()

        client.cancelListing(createdListing, createdListing.mintAccount).getOrThrow()

        val cancelledListing =
            auctionsClient.findListingByReceipt(createdListing.receiptAddress.address).getOrThrow()

        // then
        Assert.assertNotNull(createdListing)
        Assert.assertTrue(cancelledListing.canceledAt!! > 0)
        Assert.assertEquals(createdListing.auctionHouse, cancelledListing.auctionHouse)
        Assert.assertEquals(createdListing.mintAccount, cancelledListing.mintAccount)
        Assert.assertEquals(createdListing.tokenAccount, cancelledListing.tokenAccount)
        Assert.assertEquals(createdListing.price, cancelledListing.price)
        Assert.assertEquals(createdListing.tokens, cancelledListing.tokens)
    }

    @Test
    fun testCreatePublicBidOnAuctionHouse() = runTest {
        // given
        val buyer = HotAccount()
        val seller = HotAccount()
        val connection = MetaplexTestUtils.generateConnectionDriver()
        val buyerIdentityDriver = KeypairIdentityDriver(buyer, connection)
        val sellerIdentityDriver = KeypairIdentityDriver(seller, connection)
        val auctionsClient = AuctionsClient(connection, buyerIdentityDriver)

        // when
        connection.airdrop(buyer.publicKey, 1f)
        connection.airdrop(seller.publicKey, 1f)

        // create nft to bid on
        val nft = createNft(connection, sellerIdentityDriver).getOrThrow()

        // create auction house
        val auctionHouse = auctionsClient.createAuctionHouse(250).getOrThrow()

        // fund AH fee account (should this be added to the create auction house client operation?)
        connection.airdrop(auctionHouse.auctionHouseFeeAccount, 1f)

        val createdBid = AuctionHouseClient(auctionHouse, connection, buyerIdentityDriver)
            .bid(nft.mint, 100000L).getOrThrow()

        val onChainBid =
            auctionsClient.findBidByReceipt(createdBid.receiptAddress.address).getOrThrow()

        // then
        Assert.assertNotNull(createdBid)
        Assert.assertEquals(createdBid, onChainBid)
    }

    @Test
    fun testCancelPublicBidOnAuctionHouse() = runTest {
        // given
        val buyer = HotAccount()
        val seller = HotAccount()
        val connection = MetaplexTestUtils.generateConnectionDriver()
        val buyerIdentityDriver = KeypairIdentityDriver(buyer, connection)
        val sellerIdentityDriver = KeypairIdentityDriver(seller, connection)
        val auctionsClient = AuctionsClient(connection, buyerIdentityDriver)

        // when
        connection.airdrop(buyer.publicKey, 1f)
        connection.airdrop(seller.publicKey, 1f)

        // create nft to bid on
        val nft = createNft(connection, sellerIdentityDriver).getOrThrow()

        // create auction house
        val auctionHouse = auctionsClient.createAuctionHouse(250).getOrThrow()

        // fund AH fee account (should this be added to the create auction house client operation?)
        connection.airdrop(auctionHouse.auctionHouseFeeAccount, 1f)

        val client = AuctionHouseClient(auctionHouse, connection, buyerIdentityDriver)
        val createdBid = client.bid(nft.mint, 100000L).getOrThrow()

        client.cancelBid(createdBid.mintAccount, createdBid).getOrThrow()

        val canceledBid =
            auctionsClient.findBidByReceipt(createdBid.receiptAddress.address).getOrThrow()

        // then
        Assert.assertNotNull(createdBid)
        Assert.assertTrue(canceledBid.canceledAt!! > 0)
        Assert.assertEquals(createdBid.auctionHouse, canceledBid.auctionHouse)
        Assert.assertEquals(createdBid.mintAccount, canceledBid.mintAccount)
        Assert.assertEquals(createdBid.tokenAccount, canceledBid.tokenAccount)
        Assert.assertEquals(createdBid.price, canceledBid.price)
        Assert.assertEquals(createdBid.tokens, canceledBid.tokens)
    }

    @Test
    fun testExecuteSaleOnPublicListingBidPair() = runTest {
        // given
        val buyer = HotAccount()
        val seller = HotAccount()
        val connection = MetaplexTestUtils.generateConnectionDriver()
        val buyerIdentityDriver = KeypairIdentityDriver(buyer, connection)
        val sellerIdentityDriver = KeypairIdentityDriver(seller, connection)
        val auctionsClient = AuctionsClient(connection, buyerIdentityDriver)

        // when
        connection.airdrop(buyer.publicKey, 1f)
        connection.airdrop(seller.publicKey, 1f)

        // create nft to bid on
        val nft = createNft(connection, sellerIdentityDriver).getOrThrow()

        // create auction house
        val auctionHouse = auctionsClient.createAuctionHouse(250).getOrThrow()

        // fund AH fee account (should this be added to the create auction house client operation?)
        connection.airdrop(auctionHouse.auctionHouseFeeAccount, 1f)

        val listing = AuctionHouseClient(auctionHouse, connection, sellerIdentityDriver)
            .list(nft.mint, 1L).getOrThrow()

        val bid = AuctionHouseClient(auctionHouse, connection, buyerIdentityDriver)
            .bid(nft.mint, 1L).getOrThrow()

        val purchase = AuctionHouseClient(auctionHouse, connection, sellerIdentityDriver)
            .executeSale(listing, bid).getOrThrow()

        // then
        Assert.assertNotNull(purchase)
    }
    //endregion

    private suspend fun createNft(connection: Connection, identityDriver: IdentityDriver) =
        NftClient(connection, identityDriver).create(
            Metadata("My NFT", uri = "http://example.com/sd8756fsuyvvbf37684",
                sellerFeeBasisPoints = 250)
        )
}