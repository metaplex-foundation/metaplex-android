@file:OptIn(ExperimentalCoroutinesApi::class)

package com.metaplex.lib.modules.nfts.operations

import com.metaplex.lib.Metaplex
import com.metaplex.lib.MetaplexTestUtils
import com.metaplex.lib.MetaplexTestUtils.TEST_ACCOUNT_PUBLICKEY
import com.metaplex.lib.generateMetaplexInstance
import com.metaplex.lib.modules.nfts.models.NFT
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class FindNftsByOwnerOnChainOperationHandlerTests {

    val metaplex: Metaplex get() = MetaplexTestUtils.generateMetaplexInstance()

    @Test
    fun testFindNftsByOwnerOnChainOperation() = runTest {
        // given
        val accountKey = TEST_ACCOUNT_PUBLICKEY
        val expectedNftMintKey = "71PdnsexRQG92ZcSpEV8nn5XFqPzfK5j86yUWRF5NLyp"
        val expectedNftAuthKey = "AVgijgTG4WKDycFwYhqioMreXJpz14no8dbE8EF5roZV"

        //when
        val resultList: List<NFT?>? = FindNftsByOwnerOnChainOperationHandler(metaplex)
            .handle(accountKey).getOrNull()

        val actualNftAuthKey = resultList?.find {
            it?.metadataAccount?.mint?.toBase58() == expectedNftMintKey
        }?.metadataAccount?.update_authority?.toBase58()

        // then
        Assert.assertNotNull(resultList)
        Assert.assertEquals(expectedNftAuthKey, actualNftAuthKey)
    }
}