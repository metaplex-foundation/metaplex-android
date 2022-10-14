@file:OptIn(ExperimentalCoroutinesApi::class)

package com.metaplex.lib.modules.fungibletokens.operations

import com.metaplex.lib.Metaplex
import com.metaplex.lib.MetaplexTestUtils
import com.metaplex.lib.generateMetaplexInstance
import com.metaplex.lib.modules.token.models.FungibleToken
import com.metaplex.lib.modules.token.operations.FindFungibleTokenByMintOnChainOperationHandler
import com.metaplex.lib.programs.token_metadata.accounts.MetaplexCreator
import com.metaplex.lib.programs.token_metadata.accounts.MetaplexTokenStandard
import com.metaplex.lib.readOnlyMainnetMetaplexInstance
import com.solana.core.PublicKey
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class FindFungibleTokenByMintOnChainOperationHandlerTests {

    val metaplex: Metaplex get() = MetaplexTestUtils.readOnlyMainnetMetaplexInstance

    @Test
    fun testFindFungibleTokenByMintOnChainOperation() = runTest {
        // given
        val expectedFungibleTokenName = "USD Coin"
        val expectedFungibleTokenSymbol = "USDC"
        val expectedNftMintKey = "EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v"

        // when
        val result = FindFungibleTokenByMintOnChainOperationHandler(metaplex)
            .handle(PublicKey(expectedNftMintKey)).getOrNull()

        // then
        Assert.assertNotNull(result) // safe to force unwrap after this check
        Assert.assertEquals(expectedFungibleTokenName, result!!.metadataAccount.data.name)
        Assert.assertEquals(expectedFungibleTokenSymbol, result.metadataAccount.data.symbol)
        Assert.assertEquals(expectedNftMintKey, result.metadataAccount.mint.toBase58())
    }

    @Test
    fun testFungibleTokenOnChain() = runTest {
        // given
        val mintOperationPublicKey = PublicKey("4k3Dyjzvzp8eMZWUXbBCjEvwSkkk59S5iCNLY3QrkX6R")
        val expectedFungibleTokenName = "Raydium"
        val expectedCreators: Array<MetaplexCreator> = arrayOf()
        val expectedCollection = null
        val expectedEditionNonce = 254
        val expectedTokenStandard = MetaplexTokenStandard.NonFungible

        // when
        val fungibleToken: FungibleToken? =
            FindFungibleTokenByMintOnChainOperationHandler(metaplex)
                .handle(mintOperationPublicKey).getOrNull()

        // then
        Assert.assertNotNull(fungibleToken) // safe to force unwrap after this check
        Assert.assertEquals(expectedFungibleTokenName, fungibleToken!!.name)
        Assert.assertArrayEquals(expectedCreators, fungibleToken.creators)
        Assert.assertNull(expectedCollection, fungibleToken.collection)
        Assert.assertEquals(expectedEditionNonce, fungibleToken.editionNonce)
        Assert.assertEquals(expectedTokenStandard, fungibleToken.tokenStandard)
        Assert.assertFalse(fungibleToken.primarySaleHappened)
        Assert.assertTrue(fungibleToken.isMutable)
    }

    @Test
    fun testsFungibleTokenonChain2() = runTest {
        // given
        val mintOperationPublicKey = PublicKey("Saber2gLauYim4Mvftnrasomsv6NvAuncvMEZwcLpD1")
        val expectedFungibleTokenName = "Saber Protocol Token"
        val expectedCreators: Array<MetaplexCreator> = arrayOf()
        val expectedCollection = null
        val expectedEditionNonce = 255
        val expectedTokenStandard = MetaplexTokenStandard.NonFungible

        // when
        val fungibleToken: FungibleToken? =
            FindFungibleTokenByMintOnChainOperationHandler(metaplex)
                .handle(mintOperationPublicKey).getOrNull()

        // then
        Assert.assertNotNull(fungibleToken) // safe to force unwrap after this check
        Assert.assertEquals(expectedFungibleTokenName, fungibleToken!!.name)
        Assert.assertArrayEquals(expectedCreators, fungibleToken.creators)
        Assert.assertNull(expectedCollection, fungibleToken.collection)
        Assert.assertEquals(expectedEditionNonce, fungibleToken.editionNonce)
        Assert.assertEquals(expectedTokenStandard, fungibleToken.tokenStandard)
        Assert.assertFalse(fungibleToken.primarySaleHappened)
        Assert.assertTrue(fungibleToken.isMutable)
    }
}