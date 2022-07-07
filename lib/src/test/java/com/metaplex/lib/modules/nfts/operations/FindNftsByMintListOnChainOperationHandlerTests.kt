package com.metaplex.lib.modules.nfts.operations

import com.metaplex.lib.Metaplex
import com.metaplex.lib.MetaplexTestUtils
import com.metaplex.lib.generateMetaplexInstance
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.shared.ResultWithCustomError
import com.metaplex.lib.shared.getOrDefault
import com.solana.core.PublicKey
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class FindNftsByMintListOnChainOperationHandlerTests {

    val metaplex: Metaplex get() = MetaplexTestUtils.generateMetaplexInstance()

    @Test
    fun testFindNftsByMintListOnChainOperation() {
        // given
        val expectedMintKeys = listOf(
            "HG2gLyDxmYGUfNWnvf81bJQj38twnF2aQivpkxficJbn",
            "B3n4QMZWCfsTZxC6bgJh9YGWFjESmExSYp8NGfJ8DQvF"
        )
        val expectedNftNames = listOf("Aurorian #628", "Degen Ape #2031")
        val expectedNftCount = expectedMintKeys.size

        // when
        val lock = CountDownLatch(1)
        var result = listOf<NFT?>()
        val operation = FindNftsByMintListOnChainOperationHandler(metaplex)
        operation.handle(
            FindNftsByMintListOperation.pure(
                ResultWithCustomError.success( expectedMintKeys.map { PublicKey(it) })
            )
        ).run {
            result = it.getOrDefault(listOf())
            lock.countDown()
        }
        lock.await(2000, TimeUnit.MILLISECONDS)

        // then
        Assert.assertEquals(expectedNftCount, result.size)
        result.forEachIndexed { i, nft ->
            Assert.assertNotNull(nft)
            Assert.assertEquals(expectedNftNames[i], nft!!.metadataAccount.data.name)
            Assert.assertEquals(expectedMintKeys[i], nft.metadataAccount.mint.toBase58())
        }
    }
    @Test
    fun testFindNftsByMintListOnChainOperation_OneNull() {
        // given
        val expectedNftName = "Aurorian #628"
        val expectedMintKey = "HG2gLyDxmYGUfNWnvf81bJQj38twnF2aQivpkxficJbn"

        // when
        var result: List<NFT?> = listOf()
        val lock = CountDownLatch(1)
        val operation = FindNftsByMintListOnChainOperationHandler(metaplex)
        operation.handle(
            FindNftsByMintListOperation.pure(
                ResultWithCustomError.success(
                    listOf(PublicKey(expectedMintKey), PublicKey(""))
                )
            )
        ).run {
            result = it.getOrDefault(listOf())
            lock.countDown()
        }
        lock.await(2000, TimeUnit.MILLISECONDS)

        // then
        val nft = result.first()
        Assert.assertNotNull(nft)
        Assert.assertEquals(expectedNftName, nft!!.metadataAccount.data.name)
        Assert.assertEquals(expectedMintKey, nft.metadataAccount.mint.toBase58())

        val nft2 = result[1]
        Assert.assertNull(nft2)
    }
}