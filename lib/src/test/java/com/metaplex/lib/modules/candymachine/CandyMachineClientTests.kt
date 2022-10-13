/*
 * CandyMachineClientTests
 * Metaplex
 * 
 * Created by Funkatronics on 9/8/2022
 */

@file:OptIn(ExperimentalCoroutinesApi::class)

package com.metaplex.lib.modules.candymachine

import com.metaplex.lib.drivers.indenty.IdentityDriver
import com.metaplex.lib.drivers.indenty.KeypairIdentityDriver
import com.metaplex.lib.drivers.indenty.ReadOnlyIdentityDriver
import com.metaplex.lib.drivers.rpc.JdkRpcDriver
import com.metaplex.lib.drivers.solana.Commitment
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.drivers.solana.SolanaConnectionDriver
import com.metaplex.lib.drivers.solana.TransactionOptions
import com.metaplex.lib.modules.candymachines.CandyMachineClient
import com.metaplex.lib.modules.candymachines.models.CandyMachine
import com.metaplex.lib.modules.candymachines.models.CandyMachineItem
import com.metaplex.lib.modules.candymachines.refresh
import com.metaplex.lib.modules.nfts.NftClient
import com.metaplex.lib.modules.nfts.models.Metadata
import com.metaplex.mock.driver.rpc.MockErrorRpcDriver
import com.solana.core.Account
import com.solana.core.PublicKey
import com.solana.networking.RPCEndpoint
import com.util.airdrop
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import java.net.URL

class CandyMachineClientTests {

//    @Before
//    fun prepare() {
//        jenerateCandyMachine()
//        jenerateCandyGuard()
//    }

    //region UNIT
    @Test
    fun testFindCandyMachineV2ByAddressHandlesAndReturnsError() = runTest {
        // given
        val expectedErrorMessage = "An Error Occurred"
        val cmAddress = PublicKey(ByteArray(PublicKey.PUBLIC_KEY_LENGTH))
        val connection = SolanaConnectionDriver(MockErrorRpcDriver(expectedErrorMessage))

        val client = CandyMachineClient(connection,
            ReadOnlyIdentityDriver(Account().publicKey, connection)
        )

        // when
        val result = client.findByAddress(cmAddress)

        // then
        Assert.assertTrue(result.isFailure)
        Assert.assertEquals(expectedErrorMessage, result.exceptionOrNull()!!.message)
    }

    @Test
    fun testCandyMachineCreateHandlesAndReturnsError() = runTest {
        // given
        val signer = Account()
        val expectedErrorMessage = "An Error Occurred"
        val connection = SolanaConnectionDriver(MockErrorRpcDriver(expectedErrorMessage))
        val client = CandyMachineClient(connection, KeypairIdentityDriver(signer, connection))

        // when
        val result = client.create(
            333,
            1L,
            collection = signer.publicKey,
            collectionUpdateAuthority = signer.publicKey
        )

        // then
        Assert.assertTrue(result.isFailure)
        Assert.assertEquals(expectedErrorMessage, result.exceptionOrNull()?.message)
    }

    @Test
    fun testCandyMachineInsertItemsHandlesAndReturnsError() = runTest {
        // given
        val signer = Account()
        val expectedErrorMessage = "An Error Occurred"
        val connection = SolanaConnectionDriver(MockErrorRpcDriver(expectedErrorMessage))
        val client = CandyMachineClient(connection, KeypairIdentityDriver(signer, connection))

        // when
        val result = client.insertItems(
            CandyMachine(signer.publicKey, signer.publicKey, 333.toUShort(), 1L,
                collectionMintAddress = signer.publicKey, collectionUpdateAuthority = signer.publicKey),
            listOf(CandyMachineItem("An NFT", "test.com"))
        )

        // then
        Assert.assertTrue(result.isFailure)
        Assert.assertEquals(expectedErrorMessage, result.exceptionOrNull()?.message)
    }

    @Test
    fun testCandyMachineMintNftHandlesAndReturnsError() = runTest {
        // given
        val signer = Account()
        val expectedErrorMessage = "An Error Occurred"
        val connection = SolanaConnectionDriver(MockErrorRpcDriver(expectedErrorMessage))
        val client = CandyMachineClient(connection, KeypairIdentityDriver(signer, connection))

        // when
        val result = client.mintNft(
            CandyMachine(signer.publicKey, signer.publicKey, 333.toUShort(), 1L,
                collectionMintAddress = signer.publicKey, collectionUpdateAuthority = signer.publicKey)
        )

        // then
        Assert.assertTrue(result.isFailure)
        Assert.assertEquals(expectedErrorMessage, result.exceptionOrNull()?.message)
    }
    //endregion

    //region INTEGRATION
    // LOOKUP
    @Test
    fun testFindCandyMachineByAddressReturnsValidCandyMachine() = runTest {
        // given
        val cmAddress = PublicKey("4Add8hdxC44H3DcGfgWTvn2GNBfofk5uu2iatEW9LCYz")
        val connection = SolanaConnectionDriver(JdkRpcDriver(RPCEndpoint.devnetSolana.url))
        val client = CandyMachineClient(connection,
            ReadOnlyIdentityDriver(Account().publicKey, connection))

        // when
        val candyMachine: CandyMachine? = client.findByAddress(cmAddress).getOrNull()

        // then
        Assert.assertNotNull(candyMachine)
    }

    // CREATE
    @Test
    fun testCandyMachineCreateCreatesValidCandyMachine() = runTest {
        // given
        val rpcUrl = URL("http://127.0.0.1:8899")
//        val rpcUrl = RPCEndpoint.devnetSolana.url
        val signer = Account()
        val connection = SolanaConnectionDriver(JdkRpcDriver(rpcUrl),
            transactionOptions = TransactionOptions(Commitment.CONFIRMED, skipPreflight = true))
        val identityDriver = KeypairIdentityDriver(signer, connection)
        val client = CandyMachineClient(connection, identityDriver)

        // when
        connection.airdrop(signer.publicKey, 10f)
        val nft = createCollectionNft(connection, identityDriver).getOrThrow()
        val candyMachine = client.create(333, 5000, nft.mint, signer.publicKey).getOrThrow()
        val actualCandyMachine = client.findByAddress(candyMachine.address).getOrNull()

        // then
        Assert.assertNotNull(actualCandyMachine)
        Assert.assertEquals(candyMachine.configLineSettings, actualCandyMachine?.configLineSettings)
    }

    //region SET COLLECTION
    @Test
    fun testCandyMachineSetCollectionUpdatesCandyMachineCollection() = runTest {
        // given
        val rpcUrl = URL("http://127.0.0.1:8899")
//        val rpcUrl = RPCEndpoint.devnetSolana.url
        val signer = Account()
        val connection = SolanaConnectionDriver(JdkRpcDriver(rpcUrl),
            transactionOptions = TransactionOptions(Commitment.CONFIRMED, skipPreflight = true))
        val identityDriver = KeypairIdentityDriver(signer, connection)
        val client = CandyMachineClient(connection, identityDriver)

        // when
        connection.airdrop(signer.publicKey, 1f)
        val nft = createCollectionNft(connection, identityDriver).getOrThrow()
        val newCollection = createCollectionNft(connection, identityDriver).getOrThrow()
        val candyMachine =
            client.create(200, 2, nft.mint, signer.publicKey).map {
                client.setCollection(it, newCollection.mint)
                client.refresh(it).getOrNull()
            }.getOrThrow()

        // then
        Assert.assertEquals(newCollection.mint, candyMachine?.collectionMintAddress)
    }
    //endregion

    //region INSERT ITEMS
    @Test
    fun testCandyMachineInsertItemsCanAddItemsToCandyMachine() = runTest {
        // given
        val rpcUrl = URL("http://127.0.0.1:8899")
//        val rpcUrl = RPCEndpoint.devnetSolana.url
        val signer = Account()
        val connection = SolanaConnectionDriver(JdkRpcDriver(rpcUrl),
            transactionOptions = TransactionOptions(Commitment.CONFIRMED, skipPreflight = true))
        val identityDriver = KeypairIdentityDriver(signer, connection)
        val client = CandyMachineClient(connection, identityDriver)

        val expectedItems = listOf(
            CandyMachineItem("Degen #1", "https://example.com/degen/1"),
            CandyMachineItem("Degen #2", "https://example.com/degen/2")
        )

        // when
        connection.airdrop(signer.publicKey, 1f)
        val nft = createCollectionNft(connection, identityDriver).getOrThrow()
        val candyMachine =
            client.create(200, 2, nft.mint, signer.publicKey).map {
                client.insertItems(it, expectedItems)
                client.refresh(it).getOrNull()
            }.getOrThrow()

        // then
        Assert.assertEquals(expectedItems, candyMachine?.items)
    }

    @Test
    fun testCandyMachineInsertItemsSequentiallyAddsItemsToCandyMachine() = runTest {
        // given
        val rpcUrl = URL("http://127.0.0.1:8899")
//        val rpcUrl = RPCEndpoint.devnetSolana.url
        val signer = Account()
        val connection = SolanaConnectionDriver(JdkRpcDriver(rpcUrl),
            transactionOptions = TransactionOptions(Commitment.CONFIRMED, skipPreflight = true))
        val identityDriver = KeypairIdentityDriver(signer, connection)
        val client = CandyMachineClient(connection, identityDriver)

        val expectedItems = listOf(
            CandyMachineItem("Degen #1", "https://example.com/degen/1"),
            CandyMachineItem("Degen #2", "https://example.com/degen/2")
        )

        // when
        connection.airdrop(signer.publicKey, 1f)
        val nft = createCollectionNft(connection, identityDriver).getOrThrow()
        val candyMachine = client.create(200, 2, nft.mint, signer.publicKey).map {
            client.insertItems(it, listOf(expectedItems[0])) // insert first item
            client.refresh(it).map { // refresh cm (so we have updated data for insertion)
                client.insertItems(it, listOf(expectedItems[1])) // insert second item
            }
            client.refresh(it).getOrThrow()
        }.getOrThrow()

        // then
        Assert.assertEquals(expectedItems, candyMachine.items)
    }
    //endregion

    // MINT
    @Test
    fun testCandyMachineMintNftMintsAndReturnsNft() = runTest {
        // given
        val rpcUrl = URL("http://127.0.0.1:8899")
//        val rpcUrl = RPCEndpoint.devnetSolana.url
        val signer = Account()
        val connection = SolanaConnectionDriver(JdkRpcDriver(rpcUrl),
            transactionOptions = TransactionOptions(Commitment.CONFIRMED, skipPreflight = true))
        val identityDriver = KeypairIdentityDriver(signer, connection)
        val client = CandyMachineClient(connection, identityDriver)

        // when
        connection.airdrop(signer.publicKey, 1f)
        val nft = createCollectionNft(connection, identityDriver).getOrThrow()
        val mintResult = client.create(250, 1, nft.mint, signer.publicKey).map {
            client.insertItems(it, listOf(
                CandyMachineItem("My NFT", "https://example.com/mynft"),
            ))
            client.mintNft(it)//.getOrThrow()
        }.getOrThrow()

        // then
        Assert.assertNotNull(mintResult)
    }
    //endregion

    private suspend fun createCollectionNft(connection: Connection, identityDriver: IdentityDriver) =
        NftClient(connection, identityDriver).create(
            Metadata("My NFT", uri = "http://example.com/sd8756fsuyvvbf37684",
                sellerFeeBasisPoints = 250), true
        )
}