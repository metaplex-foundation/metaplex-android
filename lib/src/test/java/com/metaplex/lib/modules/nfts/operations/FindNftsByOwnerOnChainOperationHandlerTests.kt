package com.metaplex.lib.modules.nfts.operations

import com.metaplex.lib.Metaplex
import com.metaplex.lib.MetaplexTestUtils
import com.metaplex.lib.MetaplexTestUtils.TEST_ACCOUNT_PUBLICKEY
import com.metaplex.lib.generateMetaplexInstance
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.shared.ResultWithCustomError
import com.metaplex.lib.shared.getOrDefault
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class FindNftsByOwnerOnChainOperationHandlerTests {

    val metaplex: Metaplex get() = MetaplexTestUtils.generateMetaplexInstance()

    @Test
    fun testFindNftsByOwnerOnChainOperation() {
        // given
        val accountKey = TEST_ACCOUNT_PUBLICKEY
        val expectedNftMintKey = "71PdnsexRQG92ZcSpEV8nn5XFqPzfK5j86yUWRF5NLyp"
        val expectedNftAuthKey = "AVgijgTG4WKDycFwYhqioMreXJpz14no8dbE8EF5roZV"
        val operation = FindNftsByOwnerOnChainOperationHandler(metaplex)
        var resultList: List<NFT?>? = null

        //when
        val lock = CountDownLatch(1)
        operation.handle(
            FindNftsByOwnerOperation.pure(ResultWithCustomError.success(accountKey))
        ).run {
            resultList = it.getOrDefault(null)
            lock.countDown()
        }
        lock.await(2000, TimeUnit.MILLISECONDS)

        val actualNftAuthKey = resultList?.find {
            it?.metadataAccount?.mint?.toBase58() == expectedNftMintKey
        }?.metadataAccount?.update_authority?.toBase58()

        // then
        Assert.assertNotNull(resultList)
        Assert.assertEquals(expectedNftAuthKey, actualNftAuthKey)
    }
}