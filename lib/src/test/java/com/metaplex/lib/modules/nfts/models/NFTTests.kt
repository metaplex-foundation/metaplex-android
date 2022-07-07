package com.metaplex.lib.modules.nfts.models

import com.metaplex.lib.Metaplex
import com.metaplex.lib.MetaplexTestUtils
import com.metaplex.lib.drivers.storage.OkHttpSharedStorageDriver
import com.metaplex.lib.generateMetaplexInstance
import com.metaplex.lib.modules.nfts.operations.FindNftByMintOnChainOperationHandler
import com.metaplex.lib.modules.nfts.operations.FindNftByMintOperation
import com.metaplex.lib.programs.token_metadata.accounts.MetaplexTokenStandard
import com.metaplex.lib.shared.ResultWithCustomError
import com.metaplex.lib.shared.getOrDefault
import com.solana.core.PublicKey
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class NFTTests {

    val metaplex: Metaplex get() =
        MetaplexTestUtils.generateMetaplexInstance(storageDriver = OkHttpSharedStorageDriver())

    @Test
    fun testsNFTonChain() {
        // given
        val mintOperationPublicKey = PublicKey("7A1R6HLKVEnu74kCM7qb59TpmJGyUX8f5t7p3FCrFTVR")
        val expectedNftName = "DeGod #5116"
        val expectedCreatorAddress = "9MynErYQ5Qi6obp4YwwdoDmXkZ1hYVtPUqYmJJ3rZ9Kn"
        val expectedCollectionKey = "6XxjKYFbcndh2gDcsUrmZgVEsoDxXMnfsaGY6fpTJzNr"
        val expectedEditionNonce = 254
        val expectedTokenStandard = MetaplexTokenStandard.NonFungible

        // when
        var nft: NFT? = null
        val lock = CountDownLatch(1)
        val operation = FindNftByMintOnChainOperationHandler(metaplex)
        operation.handle(
            FindNftByMintOperation.pure(
                ResultWithCustomError.success(mintOperationPublicKey)
            )
        )
            .run {
                nft = it.getOrDefault(null)
                lock.countDown()
            }
        lock.await(20000, TimeUnit.MILLISECONDS)

        // then
        Assert.assertNotNull(nft) // safe to force unwrap after this check
        Assert.assertEquals(expectedNftName, nft!!.name)
        Assert.assertEquals(expectedCreatorAddress, nft!!.creators.first().address.toBase58())
        Assert.assertEquals(expectedCollectionKey, nft!!.collection?.key?.toBase58())
        Assert.assertEquals(expectedEditionNonce, nft!!.editionNonce)
        Assert.assertEquals(expectedTokenStandard, nft!!.tokenStandard)
        Assert.assertTrue(nft!!.primarySaleHappened)
        Assert.assertTrue(nft!!.isMutable)
    }

    @Test
    fun testsNFTonChain2() {
        // given
        val mintOperationPublicKey = PublicKey("GU6wXYYyCiXA2KMBd1e1eXttpK1mzjRPjK6rA2QD1fN2")
        val expectedNftName = "Degen Ape #6125"
        val expectedCreatorAddress = "9BKWqDHfHZh9j39xakYVMdr6hXmCLHH5VfCpeq2idU9L"
        val expectedCollectionKey = "DSwfRF1jhhu6HpSuzaig1G19kzP73PfLZBPLofkw6fLD"
        val expectedEditionNonce = 255
        val expectedTokenStandard = MetaplexTokenStandard.NonFungible

        // when
        var nft: NFT? = null
        val lock = CountDownLatch(1)
        val operation = FindNftByMintOnChainOperationHandler(metaplex)
        operation.handle(
            FindNftByMintOperation.pure(
                ResultWithCustomError.success(mintOperationPublicKey)
            )
        )
            .run {
                nft = it.getOrDefault(null)
                lock.countDown()
            }
        lock.await(20000, TimeUnit.MILLISECONDS)

        // then
        Assert.assertNotNull(nft) // safe to force unwrap after this check
        Assert.assertEquals(expectedNftName, nft!!.name)
        Assert.assertEquals(expectedCreatorAddress, nft!!.creators.first().address.toBase58())
        Assert.assertEquals(expectedCollectionKey, nft!!.collection?.key?.toBase58())
        Assert.assertEquals(expectedEditionNonce, nft!!.editionNonce)
        Assert.assertEquals(expectedTokenStandard, nft!!.tokenStandard)
        Assert.assertTrue(nft!!.primarySaleHappened)
        Assert.assertFalse(nft!!.isMutable)
    }

    @Test
    fun testsNFTMetadata() {
        // given
        val expectedName = "Aurorian #628"
        val expectedSymbol = "AUROR"
        val expectedSellerFeeBasisPoints = 500.0
        val expectedCreatorAddress = "8Kag8CqNdCX55s4A5W4iraS71h6mv6uTHqsJbexdrrZm"

        // when
        var metadata: JsonMetadata? = null
        var nft: NFT? = null
        val lock = CountDownLatch(1)
        val operation = FindNftByMintOnChainOperationHandler(metaplex)
        operation.handle(FindNftByMintOperation.pure(ResultWithCustomError.success(PublicKey("HG2gLyDxmYGUfNWnvf81bJQj38twnF2aQivpkxficJbn"))))
            .run {
                nft = it.getOrThrows()
                nft!!.metadata(metaplex) { result ->
                    metadata = result.getOrThrows()
                    lock.countDown()
                }
            }
        lock.await(20000, TimeUnit.MILLISECONDS)

        // then
        Assert.assertNotNull(metadata)  // safe to force unwrap after this check
        Assert.assertEquals(expectedName, metadata!!.name)
        Assert.assertEquals(expectedSymbol, metadata!!.symbol)
        Assert.assertEquals(expectedSellerFeeBasisPoints, metadata!!.seller_fee_basis_points)
        Assert.assertNotNull(
            metadata!!.properties?.creators?.find { it.address == expectedCreatorAddress }
        )

        val value = (metadata!!.attributes!![10].value as Value.number).value
        Assert.assertEquals(1.0, value, 0.0)
    }
}