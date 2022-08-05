package com.metaplex.lib.modules.fungibletokens.models

import com.metaplex.lib.Metaplex
import com.metaplex.lib.MetaplexTestUtils
import com.metaplex.lib.drivers.storage.OkHttpSharedStorageDriver
import com.metaplex.lib.generateMetaplexInstance
import com.metaplex.lib.modules.fungibletokens.operations.FindFungibleTokenByMintOnChainOperationHandler
import com.metaplex.lib.modules.fungibletokens.operations.FindFungibleTokenByMintOperation
import com.metaplex.lib.programs.token_metadata.accounts.MetaplexCreator
import com.metaplex.lib.programs.token_metadata.accounts.MetaplexTokenStandard
import com.metaplex.lib.shared.ResultWithCustomError
import com.metaplex.lib.shared.getOrDefault
import com.solana.core.PublicKey
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class FungibleTokenTests {
    val metaplex: Metaplex get() =
        MetaplexTestUtils.generateMetaplexInstance(storageDriver = OkHttpSharedStorageDriver())

    @Test
    fun testsFungibleTokenonChain() {
        // given
        val mintOperationPublicKey = PublicKey("4k3Dyjzvzp8eMZWUXbBCjEvwSkkk59S5iCNLY3QrkX6R")
        val expectedFungibleTokenName = "Raydium"
        val expectedCreators: Array<MetaplexCreator> = arrayOf()
        val expectedCollection = null
        val expectedEditionNonce = 254
        val expectedTokenStandard = MetaplexTokenStandard.Fungible

        // when
        var fungibleToken: FungibleToken? = null
        val lock = CountDownLatch(1)
        val operation = FindFungibleTokenByMintOnChainOperationHandler(metaplex)
        operation.handle(
            FindFungibleTokenByMintOperation.pure(
                ResultWithCustomError.success(mintOperationPublicKey)
            )
        )
            .run {
                fungibleToken = it.getOrDefault(null)
                lock.countDown()
            }
        lock.await(20000, TimeUnit.MILLISECONDS)

        // then
        Assert.assertNotNull(fungibleToken) // safe to force unwrap after this check
        Assert.assertEquals(expectedFungibleTokenName, fungibleToken!!.name)
        Assert.assertArrayEquals(expectedCreators, fungibleToken!!.creators)
        Assert.assertNull(expectedCollection, fungibleToken!!.collection)
        Assert.assertEquals(expectedEditionNonce, fungibleToken!!.editionNonce)
        Assert.assertEquals(expectedTokenStandard, fungibleToken!!.tokenStandard)
        Assert.assertFalse(fungibleToken!!.primarySaleHappened)
        Assert.assertTrue(fungibleToken!!.isMutable)
    }

    @Test
    fun testsFungibleTokenonChain2() {
        // given
        val mintOperationPublicKey = PublicKey("Saber2gLauYim4Mvftnrasomsv6NvAuncvMEZwcLpD1")
        val expectedFungibleTokenName = "Saber Protocol Token"
        val expectedCreators: Array<MetaplexCreator> = arrayOf()
        val expectedCollection = null
        val expectedEditionNonce = 255
        val expectedTokenStandard = MetaplexTokenStandard.Fungible

        // when
        var fungibleToken: FungibleToken? = null
        val lock = CountDownLatch(1)
        val operation = FindFungibleTokenByMintOnChainOperationHandler(metaplex)
        operation.handle(
            FindFungibleTokenByMintOperation.pure(
                ResultWithCustomError.success(mintOperationPublicKey)
            )
        )
            .run {
                fungibleToken = it.getOrDefault(null)
                lock.countDown()
            }
        lock.await(20000, TimeUnit.MILLISECONDS)

        // then
        Assert.assertNotNull(fungibleToken) // safe to force unwrap after this check
        Assert.assertEquals(expectedFungibleTokenName, fungibleToken!!.name)
        Assert.assertArrayEquals(expectedCreators, fungibleToken!!.creators)
        Assert.assertNull(expectedCollection, fungibleToken!!.collection)
        Assert.assertEquals(expectedEditionNonce, fungibleToken!!.editionNonce)
        Assert.assertEquals(expectedTokenStandard, fungibleToken!!.tokenStandard)
        Assert.assertFalse(fungibleToken!!.primarySaleHappened)
        Assert.assertTrue(fungibleToken!!.isMutable)
    }
}