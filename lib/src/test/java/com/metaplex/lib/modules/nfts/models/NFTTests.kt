package com.metaplex.lib.modules.nfts.models

import com.metaplex.lib.Metaplex
import com.metaplex.lib.MetaplexTestUtils
import com.metaplex.lib.drivers.storage.OkHttpSharedStorageDriver
import com.metaplex.lib.generateMetaplexInstance
import com.metaplex.lib.modules.nfts.operations.FindNftByMintOnChainOperationHandler
import com.metaplex.lib.modules.nfts.operations.FindNftByMintOperation
import com.metaplex.lib.programs.token_metadata.accounts.MetaplexTokenStandard
import com.metaplex.lib.shared.ResultWithCustomError
import com.solana.core.PublicKey
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class NFTTests {

    val metaplex: Metaplex get() =
        MetaplexTestUtils.generateMetaplexInstance(OkHttpSharedStorageDriver())

    @Test
    fun testsNFTonChain() {
        var nft: NFT? = null
        val lock = CountDownLatch(1)
        val operation = FindNftByMintOnChainOperationHandler(metaplex)
        operation.handle(
            FindNftByMintOperation.pure(
                ResultWithCustomError.success(PublicKey("7A1R6HLKVEnu74kCM7qb59TpmJGyUX8f5t7p3FCrFTVR"))
            )
        )
            .run {
                nft = it.getOrThrows()
                lock.countDown()
            }
        lock.await(20000, TimeUnit.MILLISECONDS)
        Assert.assertNotNull(nft)

        Assert.assertEquals(nft!!.name, "DeGod #5116")
        Assert.assertEquals(nft!!.creators.first().address.toBase58(), "9MynErYQ5Qi6obp4YwwdoDmXkZ1hYVtPUqYmJJ3rZ9Kn")
        Assert.assertEquals(nft!!.primarySaleHappened, true)
        Assert.assertEquals(nft!!.isMutable, true)
        Assert.assertEquals(nft!!.editionNonce, 254)
        Assert.assertEquals(nft!!.tokenStandard, MetaplexTokenStandard.NonFungible)
        Assert.assertNotNull(nft!!.collection)
        Assert.assertEquals(nft!!.collection!!.key.toBase58(), "6XxjKYFbcndh2gDcsUrmZgVEsoDxXMnfsaGY6fpTJzNr")

    }

    @Test
    fun testsNFTonChain2() {
        var nft: NFT? = null
        val lock = CountDownLatch(1)
        val operation = FindNftByMintOnChainOperationHandler(metaplex)
        operation.handle(
            FindNftByMintOperation.pure(
                ResultWithCustomError.success(PublicKey("GU6wXYYyCiXA2KMBd1e1eXttpK1mzjRPjK6rA2QD1fN2"))
            )
        )
            .run {
                nft = it.getOrThrows()
                lock.countDown()
            }
        lock.await(20000, TimeUnit.MILLISECONDS)
        Assert.assertNotNull(nft)
        Assert.assertEquals(nft!!.name, "Degen Ape #6125")
        Assert.assertEquals(nft!!.creators.first().address.toBase58(), "9BKWqDHfHZh9j39xakYVMdr6hXmCLHH5VfCpeq2idU9L")
        Assert.assertEquals(nft!!.primarySaleHappened, true)
        Assert.assertEquals(nft!!.isMutable, false)
        Assert.assertEquals(nft!!.editionNonce, 255)
        Assert.assertEquals(nft!!.tokenStandard, MetaplexTokenStandard.NonFungible)
        Assert.assertNotNull(nft!!.collection)
        Assert.assertEquals(nft!!.collection?.key!!.toBase58(), "DSwfRF1jhhu6HpSuzaig1G19kzP73PfLZBPLofkw6fLD")
    }

    @Test
    fun testsNFTMetadata() {
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
        Assert.assertNotNull(metadata)
        Assert.assertEquals("Aurorian #628", metadata!!.name)
        Assert.assertEquals("AUROR", metadata!!.symbol)
        Assert.assertEquals(500.0, metadata!!.seller_fee_basis_points)
        val value = (metadata!!.attributes!![10].value as Value.number).value
        Assert.assertTrue(1.0 == value)
        Assert.assertEquals(
            "8Kag8CqNdCX55s4A5W4iraS71h6mv6uTHqsJbexdrrZm",
            metadata!!.properties!!.creators!!.first().address
        )
    }
}