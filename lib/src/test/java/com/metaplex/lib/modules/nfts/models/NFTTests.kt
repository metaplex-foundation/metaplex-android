@file:OptIn(ExperimentalCoroutinesApi::class)

package com.metaplex.lib.modules.nfts.models

import com.metaplex.lib.Metaplex
import com.metaplex.lib.MetaplexTestUtils
import com.metaplex.lib.drivers.storage.OkHttpSharedStorageDriver
import com.metaplex.lib.generateMetaplexInstance
import com.metaplex.lib.modules.nfts.operations.FindNftByMintOnChainOperationHandler
import com.metaplex.lib.modules.token.models.metadata
import com.metaplex.lib.programs.token_metadata.accounts.MetaplexTokenStandard
import com.metaplex.lib.readOnlyMainnetMetaplexInstance
import com.solana.core.PublicKey
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class NFTTests {

    val metaplex: Metaplex get() =
        MetaplexTestUtils.readOnlyMainnetMetaplexInstance

    @Test
    fun testsNFTonChain() = runTest {
        // given
        val mintOperationPublicKey = PublicKey("7A1R6HLKVEnu74kCM7qb59TpmJGyUX8f5t7p3FCrFTVR")
        val expectedNftName = "DeGod #5116"
        val expectedCreatorAddress = "9MynErYQ5Qi6obp4YwwdoDmXkZ1hYVtPUqYmJJ3rZ9Kn"
        val expectedCollectionKey = "6XxjKYFbcndh2gDcsUrmZgVEsoDxXMnfsaGY6fpTJzNr"
        val expectedEditionNonce = 254
        val expectedTokenStandard = MetaplexTokenStandard.NonFungible

        // when
        val nft: NFT? = FindNftByMintOnChainOperationHandler(metaplex)
            .handle(mintOperationPublicKey).getOrNull()

        // then
        Assert.assertNotNull(nft) // safe to force unwrap after this check
        Assert.assertEquals(expectedNftName, nft!!.name)
        Assert.assertEquals(expectedCreatorAddress, nft.creators.first().address.toBase58())
        Assert.assertEquals(expectedCollectionKey, nft.collection?.key?.toBase58())
        Assert.assertEquals(expectedEditionNonce, nft.editionNonce)
        Assert.assertEquals(expectedTokenStandard, nft.tokenStandard)
        Assert.assertTrue(nft.primarySaleHappened)
        Assert.assertTrue(nft.isMutable)
    }

    @Test
    fun testsNFTonChain2() = runTest {
        // given
        val mintOperationPublicKey = PublicKey("GU6wXYYyCiXA2KMBd1e1eXttpK1mzjRPjK6rA2QD1fN2")
        val expectedNftName = "Degen Ape #6125"
        val expectedCreatorAddress = "9BKWqDHfHZh9j39xakYVMdr6hXmCLHH5VfCpeq2idU9L"
        val expectedCollectionKey = "DSwfRF1jhhu6HpSuzaig1G19kzP73PfLZBPLofkw6fLD"
        val expectedEditionNonce = 255
        val expectedTokenStandard = MetaplexTokenStandard.NonFungible

        // when
        val nft: NFT? = FindNftByMintOnChainOperationHandler(metaplex)
            .handle(mintOperationPublicKey).getOrNull()

        // then
        Assert.assertNotNull(nft) // safe to force unwrap after this check
        Assert.assertEquals(expectedNftName, nft!!.name)
        Assert.assertEquals(expectedCreatorAddress, nft.creators.first().address.toBase58())
        Assert.assertEquals(expectedCollectionKey, nft.collection?.key?.toBase58())
        Assert.assertEquals(expectedEditionNonce, nft.editionNonce)
        Assert.assertEquals(expectedTokenStandard, nft.tokenStandard)
        Assert.assertTrue(nft.primarySaleHappened)
        Assert.assertFalse(nft.isMutable)
    }

    @Test
    fun testsNFTMetadata() = runTest {
        // given
        val expectedName = "Aurorian #628"
        val expectedSymbol = "AUROR"
        val expectedSellerFeeBasisPoints = 500.0
        val expectedCreatorAddress = "8Kag8CqNdCX55s4A5W4iraS71h6mv6uTHqsJbexdrrZm"

        // when
        val metadata: JsonMetadata? = FindNftByMintOnChainOperationHandler(metaplex)
            .handle(PublicKey("HG2gLyDxmYGUfNWnvf81bJQj38twnF2aQivpkxficJbn"))
            .getOrThrow().metadata(OkHttpSharedStorageDriver()).getOrNull()

        // then
        Assert.assertNotNull(metadata)  // safe to force unwrap after this check
        Assert.assertEquals(expectedName, metadata!!.name)
        Assert.assertEquals(expectedSymbol, metadata.symbol)
        Assert.assertEquals(expectedSellerFeeBasisPoints, metadata.seller_fee_basis_points)
        Assert.assertNotNull(
            metadata.properties?.creators?.find { it.address == expectedCreatorAddress }
        )

        val value = (metadata.attributes!![10].value as Value.number).value
        Assert.assertEquals(1.0, value, 0.0)
    }
}