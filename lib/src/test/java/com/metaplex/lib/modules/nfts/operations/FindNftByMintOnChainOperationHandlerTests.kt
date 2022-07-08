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

class FindNftByMintOnChainOperationHandlerTests {

    val metaplex: Metaplex get() = MetaplexTestUtils.generateMetaplexInstance()

    @Test
    fun testFindNftByMintOnChainOperation() {
        // given
        val expectedNFTName = "Aurorian #628"
        val expectedNftMintKey = "HG2gLyDxmYGUfNWnvf81bJQj38twnF2aQivpkxficJbn"
        val expectedMasterAccountType = 6
        val operation = FindNftByMintOnChainOperationHandler(metaplex)
        var result: NFT? = null

        // when
        val lock = CountDownLatch(1)
        operation.handle(FindNftByMintOperation.pure(ResultWithCustomError.success(PublicKey(expectedNftMintKey)))).run {
            result = it.getOrDefault(null)
            lock.countDown()
        }
        lock.await(2000, TimeUnit.MILLISECONDS)

        // then
        Assert.assertNotNull(result) // safe to force unwrap after this check
        Assert.assertEquals(expectedNFTName, result!!.metadataAccount.data.name)
        Assert.assertEquals(expectedNftMintKey, result!!.metadataAccount.mint.toBase58())
        Assert.assertEquals(expectedMasterAccountType, result!!.masterEditionAccount?.type)
    }
}