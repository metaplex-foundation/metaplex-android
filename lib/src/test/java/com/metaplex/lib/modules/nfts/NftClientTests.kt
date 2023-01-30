/*
 * NftClientTests
 * metaplex-android
 * 
 * Created by Funkatronics on 10/12/2022
 */

package com.metaplex.lib.modules.nfts

import com.metaplex.lib.MetaplexTestUtils
import com.metaplex.lib.drivers.indenty.KeypairIdentityDriver
import com.metaplex.lib.drivers.indenty.ReadOnlyIdentityDriver
import com.metaplex.lib.drivers.solana.SolanaConnectionDriver
import com.metaplex.lib.generateConnectionDriver
import com.metaplex.lib.modules.nfts.builders.TransferNftInput
import com.metaplex.lib.modules.nfts.models.Metadata
import com.metaplex.lib.programs.token_metadata.accounts.MetaplexCollectionDetails
import com.metaplex.mock.driver.rpc.MockErrorRpcDriver
import com.solana.core.DerivationPath
import com.solana.core.HotAccount
import com.solana.core.PublicKey
import com.solana.networking.RPCEndpoint
import com.solana.vendor.TweetNaclFast
import com.solana.vendor.bip32.wallet.SolanaBip44
import com.util.airdrop
import kotlinx.coroutines.test.runTest
import org.bitcoinj.crypto.MnemonicCode
import org.junit.Assert
import org.junit.Test

class NftClientTests {

    // UNIT
    @Test
    fun testNftCreateHandlesAndReturnsError() = runTest {
        // given
        val signer = HotAccount()
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
        val signer = HotAccount()
        val connection = MetaplexTestUtils.generateConnectionDriver()
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
        val signer = HotAccount()
        val connection = MetaplexTestUtils.generateConnectionDriver()
        val identityDriver = KeypairIdentityDriver(signer, connection)
        val client = NftClient(connection, identityDriver)

        val expectedCollection = HotAccount()
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
        val signer = HotAccount()
        val connection = MetaplexTestUtils.generateConnectionDriver()
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

    // TRANSFER
    // Remove the devnet test and do a proper NFT with HotAccounts. This should happen after
    // the new CREATE instruction is integrated.
    @Test
    fun testTransferNft() = runTest {

        // given
        val connection = SolanaConnectionDriver(RPCEndpoint.devnetSolana)
        val seed = MnemonicCode.toSeed(listOf(
            "unaware",
            "toy",
            "ride",
            "kiss",
            "magic",
            "weekend",
            "local",
            "exile",
            "gesture",
            "orange",
            "file",
            "quality"
        ), "")

        val privateKeyA = SolanaBip44().getPrivateKeyFromBip44SeedWithChange(seed, 0, 0)
        val signerA = HotAccount(TweetNaclFast.Signature.keyPair_fromSeed(privateKeyA).secretKey)

        val privateKeyB = SolanaBip44().getPrivateKeyFromBip44SeedWithChange(seed, 1, 0)
        val signerB = HotAccount(TweetNaclFast.Signature.keyPair_fromSeed(privateKeyB).secretKey)


        val identityDriver = KeypairIdentityDriver(signerA, connection)
        val client = NftClient(connection, identityDriver)

        val mintKey = PublicKey("22M7k8eevD4htyEfgMLSDyHdZfakXRrW7z4CavPPX9mY")

        // when
        val signature = client.transfer(
            TransferNftInput(
                mintKey= mintKey,
                authority= TokenMetadataAuthority.Signer(
                    signerA,
                ),
                fromOwner= signerA.publicKey,
                toOwner= signerB.publicKey
            )
        ).getOrThrow()
        println(signature)
        val signatureStatus = connection.getSignatureStatuses(listOf(signature), null).getOrThrow()

        // then
        Assert.assertNull(signatureStatus.first().err)

        // when
        val identityDriverB = KeypairIdentityDriver(signerB, connection)
        val clientB = NftClient(connection, identityDriverB)

        val signature2 = clientB.transfer(
            TransferNftInput(
                mintKey= mintKey,
                authority= TokenMetadataAuthority.Signer(
                    signerB,
                ),
                fromOwner= signerB.publicKey,
                toOwner= signerA.publicKey
            )
        ).getOrThrow()
        println(signature2)
        val signatureStatus2 = connection.getSignatureStatuses(listOf(signature2), null).getOrThrow()

        // then
        Assert.assertNull(signatureStatus2.first().err)
    }

}