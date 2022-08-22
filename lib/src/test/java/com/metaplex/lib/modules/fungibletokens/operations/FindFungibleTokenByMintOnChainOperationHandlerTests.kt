package com.metaplex.lib.modules.fungibletokens.operations

import com.metaplex.lib.Metaplex
import com.metaplex.lib.MetaplexTestUtils
import com.metaplex.lib.generateMetaplexInstance
import com.metaplex.lib.modules.fungibletokens.models.FungibleToken
import com.metaplex.lib.shared.ResultWithCustomError
import com.metaplex.lib.shared.getOrDefault
import com.solana.core.PublicKey
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class FindFungibleTokenByMintOnChainOperationHandlerTests {
    val metaplex: Metaplex get() =
        MetaplexTestUtils.generateMetaplexInstance()

    @Test
    fun testFindFungibleTokenByMintOnChainOperation() {
        // given
        val expectedFungibleTokenName = "USD Coin"
        val expectedFungibleTokenSymbol = "USDC"
        val expectedNftMintKey = "EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v"
        val operation = FindFungibleTokenByMintOnChainOperationHandler(metaplex)
        var result: FungibleToken? = null

        // when
        val lock = CountDownLatch(1)
        operation.handle(FindFungibleTokenByMintOperation.pure(ResultWithCustomError.success(PublicKey(expectedNftMintKey)))).run {
            result = it.getOrDefault(null)
            lock.countDown()
        }
        lock.await(2000, TimeUnit.MILLISECONDS)

        // then
        Assert.assertNotNull(result) // safe to force unwrap after this check
        Assert.assertEquals(expectedFungibleTokenName, result!!.metadataAccount.data.name)
        Assert.assertEquals(expectedFungibleTokenSymbol, result!!.metadataAccount.data.symbol)
        Assert.assertEquals(expectedNftMintKey, result!!.metadataAccount.mint.toBase58())
    }
}