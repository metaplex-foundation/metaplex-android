package com.metaplex.lib

import com.metaplex.lib.MetaplexTestUtils.TEST_ACCOUNT_PUBLICKEY
import com.solana.models.buffer.AccountInfo
import com.solana.models.buffer.BufferInfo
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class MetaplexTests {

    val metaplex: Metaplex get() = MetaplexTestUtils.generateMetaplexInstance()
    
    @Test
    fun testMetaplexSetUpReturnsValidInstance() {
        Assert.assertNotNull(metaplex)
    }

    @Test
    fun testGetAccountInfoReturnsNonNullBufferForValidAccount() {
        // given
        val accountKey = TEST_ACCOUNT_PUBLICKEY
        var bufferInfo: BufferInfo<AccountInfo>? = null

        // when
        val lock = CountDownLatch(1)
        metaplex.getAccountInfo(accountKey, AccountInfo::class.java) {
            bufferInfo = it.getOrNull()
            lock.countDown()
        }
        lock.await(2000, TimeUnit.MILLISECONDS)

        // then
        Assert.assertNotNull(bufferInfo)
    }

    @Test
    fun testGetMultipleAccountsReturnsNonNullBuffer() {
        // given
        val accountKeys = listOf(TEST_ACCOUNT_PUBLICKEY)
        var bufferInfo: List<BufferInfo<AccountInfo>?>? = null

        // when
        val lock = CountDownLatch(1)
        metaplex.getMultipleAccountsInfo(accountKeys, AccountInfo::class.java) {
            bufferInfo = it.getOrNull()
            lock.countDown()
        }
        lock.await(2000, TimeUnit.MILLISECONDS)

        // then
        Assert.assertNotNull(bufferInfo)
    }
}