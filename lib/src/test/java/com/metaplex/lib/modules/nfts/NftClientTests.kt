/*
 * NftClientTests
 * metaplex-android
 * 
 * Created by Funkatronics on 10/12/2022
 */

package com.metaplex.lib.modules.nfts

import com.metaplex.lib.drivers.indenty.KeypairIdentityDriver
import com.metaplex.lib.drivers.rpc.JdkRpcDriver
import com.metaplex.lib.drivers.solana.Commitment
import com.metaplex.lib.drivers.solana.SolanaConnectionDriver
import com.metaplex.lib.drivers.solana.TransactionOptions
import com.metaplex.lib.modules.nfts.models.Metadata
import com.metaplex.lib.programs.token_metadata.accounts.MetaplexCollectionDetails
import com.metaplex.mock.driver.rpc.MockErrorRpcDriver
import com.solana.core.Account
import com.solana.networking.RPCEndpoint
import com.util.airdrop
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import java.net.URL

class NftClientTests {

    @Test
    fun testNftCreateHandlesAndReturnsError() = runTest {
        // given
        val signer = Account()
        val expectedErrorMessage = "An Error Occurred"
        val connection = SolanaConnectionDriver(MockErrorRpcDriver(expectedErrorMessage))
        val client = NftClient(connection, KeypairIdentityDriver(signer, connection))

        // when
        val result = client.create(
            Metadata("My NFT", uri = "http://example.com/sd8756fsuyvvbf37684",
                sellerFeeBasisPoints = 250)
        )

        // then
        Assert.assertTrue(result.isFailure)
        Assert.assertEquals(expectedErrorMessage, result.exceptionOrNull()?.message)
    }

    //region INTEGRATION
    // CREATE
    @Test
    fun testNftCreateCreatesValidNft() = runTest {
        // given
        val rpcUrl = URL("http://127.0.0.1:8899")
//        val rpcUrl = RPCEndpoint.devnetSolana.url
        val signer = Account()
        val connection = SolanaConnectionDriver(
            JdkRpcDriver(rpcUrl),
            transactionOptions = TransactionOptions(Commitment.CONFIRMED, skipPreflight = true)
        )
        val identityDriver = KeypairIdentityDriver(signer, connection)
        val client = NftClient(connection, identityDriver)

        val expectedMetadata = Metadata("My NFT",
            uri = "http://example.com/sd8756fsuyvvbf37684", sellerFeeBasisPoints = 250)

        // when
        connection.airdrop(signer.publicKey, 1f)
        val nft = client.create(expectedMetadata).getOrThrow()
        val actualNft = client.findByMint(nft.mint).getOrNull()

        // then
        Assert.assertNotNull(actualNft)
        Assert.assertNull(actualNft?.collectionDetails) // should not be a collection
        Assert.assertEquals(expectedMetadata.name, actualNft?.name)
        Assert.assertEquals(expectedMetadata.uri, actualNft?.uri)
        Assert.assertEquals(expectedMetadata.symbol, actualNft?.symbol)
        Assert.assertEquals(expectedMetadata.sellerFeeBasisPoints, actualNft?.sellerFeeBasisPoints)
    }

    @Test
    fun testNftCreateWithCollectionCreatesValidNft() = runTest {
        // given
        val rpcUrl = URL("http://127.0.0.1:8899")
//        val rpcUrl = RPCEndpoint.devnetSolana.url
        val signer = Account()
        val connection = SolanaConnectionDriver(
            JdkRpcDriver(rpcUrl),
            transactionOptions = TransactionOptions(Commitment.CONFIRMED, skipPreflight = true)
        )
        val identityDriver = KeypairIdentityDriver(signer, connection)
        val client = NftClient(connection, identityDriver)

        val expectedCollection = Account()
        val expectedMetadata = Metadata("My NFT",
            uri = "http://example.com/sd8756fsuyvvbf37684",
            sellerFeeBasisPoints = 250,
            collection = expectedCollection.publicKey)

        // when
        connection.airdrop(signer.publicKey, 1f)
        val nft = client.create(expectedMetadata).getOrThrow()
        val actualNft = client.findByMint(nft.mint).getOrNull()

        // then
        Assert.assertNotNull(actualNft)
        Assert.assertNull(actualNft?.collectionDetails) // should not be a collection
        Assert.assertEquals(expectedCollection.publicKey, actualNft?.collection?.key)
        Assert.assertEquals(expectedMetadata.name, actualNft?.name)
        Assert.assertEquals(expectedMetadata.uri, actualNft?.uri)
        Assert.assertEquals(expectedMetadata.symbol, actualNft?.symbol)
        Assert.assertEquals(expectedMetadata.sellerFeeBasisPoints, actualNft?.sellerFeeBasisPoints)
    }

    @Test
    fun testNftCreateCollectionCreatesValidCollectionNft() = runTest {
        // given
        val rpcUrl = URL("http://127.0.0.1:8899")
//        val rpcUrl = RPCEndpoint.devnetSolana.url
        val signer = Account()
        val connection = SolanaConnectionDriver(
            JdkRpcDriver(rpcUrl),
            transactionOptions = TransactionOptions(Commitment.CONFIRMED, skipPreflight = true)
        )
        val identityDriver = KeypairIdentityDriver(signer, connection)
        val client = NftClient(connection, identityDriver)

        val expectedCollectionDetails = MetaplexCollectionDetails.V1
        val expectedMetadata = Metadata("My NFT",
            uri = "http://example.com/sd8756fsuyvvbf37684", sellerFeeBasisPoints = 250)

        // when
        connection.airdrop(signer.publicKey, 1f)
        val nft = client.create(expectedMetadata, true).getOrThrow()
        val actualNft = client.findByMint(nft.mint).getOrNull()

        // then
        Assert.assertNotNull(actualNft)
        Assert.assertEquals(expectedCollectionDetails, actualNft?.collectionDetails)
        Assert.assertEquals(expectedMetadata.name, actualNft?.name)
        Assert.assertEquals(expectedMetadata.uri, actualNft?.uri)
        Assert.assertEquals(expectedMetadata.symbol, actualNft?.symbol)
        Assert.assertEquals(expectedMetadata.sellerFeeBasisPoints, actualNft?.sellerFeeBasisPoints)
    }
}