@file:OptIn(ExperimentalCoroutinesApi::class)

package com.metaplex.lib.modules.nfts.operations

import com.metaplex.lib.Metaplex
import com.metaplex.lib.MetaplexTestUtils
import com.metaplex.lib.generateMetaplexInstance
import com.metaplex.lib.readOnlyMainnetMetaplexInstance
import com.solana.core.PublicKey
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class FindNftsByMintListOnChainOperationHandlerTests {

    val metaplex: Metaplex get() = MetaplexTestUtils.readOnlyMainnetMetaplexInstance

    @Test
    fun testFindNftsByMintListOnChainOperation() = runTest {
        // given
        val expectedMintKeys = listOf(
            "HG2gLyDxmYGUfNWnvf81bJQj38twnF2aQivpkxficJbn",
            "B3n4QMZWCfsTZxC6bgJh9YGWFjESmExSYp8NGfJ8DQvF"
        )
        val expectedNftNames = listOf("Aurorian #628", "Degen Ape #2031")
        val expectedNftCount = expectedMintKeys.size

        // when
        val result = FindNftsByMintListOnChainOperationHandler(metaplex)
            .handle(expectedMintKeys.map { PublicKey(it) }).getOrDefault(listOf())

        // then
        Assert.assertEquals(expectedNftCount, result.size)
        result.forEachIndexed { i, nft ->
            Assert.assertNotNull(nft)
            Assert.assertEquals(expectedNftNames[i], nft!!.metadataAccount.data.name)
            Assert.assertEquals(expectedMintKeys[i], nft.metadataAccount.mint.toBase58())
        }
    }
    @Test
    fun testFindNftsByMintListOnChainOperation_OneNull() = runTest {
        // given
        val expectedNftName = "Aurorian #628"
        val expectedMintKey = "HG2gLyDxmYGUfNWnvf81bJQj38twnF2aQivpkxficJbn"

        // when
        val result = FindNftsByMintListOnChainOperationHandler(metaplex)
            .handle(listOf(PublicKey(expectedMintKey), PublicKey(""))).getOrDefault(listOf())

        // then
        val nft = result.first()
        Assert.assertNotNull(nft)
        Assert.assertEquals(expectedNftName, nft!!.metadataAccount.data.name)
        Assert.assertEquals(expectedMintKey, nft.metadataAccount.mint.toBase58())

        val nft2 = result[1]
        Assert.assertNull(nft2)
    }
}