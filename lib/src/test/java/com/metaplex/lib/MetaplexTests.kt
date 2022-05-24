package com.metaplex.lib

import com.metaplex.lib.drivers.indenty.ReadOnlyIdentityDriver
import com.metaplex.lib.drivers.storage.MemoryStorageDriver
import com.metaplex.lib.drivers.storage.StorageDriver
import com.metaplex.lib.solana.SolanaConnectionDriver
import com.solana.core.PublicKey
import com.solana.models.buffer.AccountInfo
import com.solana.models.buffer.BufferInfo
import com.solana.networking.RPCEndpoint
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

val TEST_PUBLICKEY = PublicKey("CN87nZuhnFdz74S9zn3bxCcd5ZxW55nwvgAv5C2Tz3K7")

class MetaplexTests {
    
    lateinit var metaplex: Metaplex

    @Before
    fun setUp() {
        val solanaConnection = SolanaConnectionDriver(RPCEndpoint.mainnetBetaSolana)
        val solanaIdentityDriver = ReadOnlyIdentityDriver(TEST_PUBLICKEY, solanaConnection.solanaRPC)
        val storageDriver = MemoryStorageDriver()
        this.metaplex = Metaplex(solanaConnection, solanaIdentityDriver, storageDriver)
    }
    
    @Test
    fun testSetUpMetaplex() {
        val solanaConnection = SolanaConnectionDriver(RPCEndpoint.mainnetBetaSolana)
        val solanaIdentityDriver = ReadOnlyIdentityDriver(TEST_PUBLICKEY, solanaConnection.solanaRPC)
        val storageDriver = MemoryStorageDriver()
        val metaplex = Metaplex(solanaConnection, solanaIdentityDriver, storageDriver)
        Assert.assertNotNull(metaplex)
    }

    @Test
    fun testGetAccountInfo() {
        val lock = CountDownLatch(1)
        var bufferInfo: Result<BufferInfo<AccountInfo>>? = null
        metaplex.getAccountInfo(TEST_PUBLICKEY, AccountInfo::class.java) {
            bufferInfo = it
            lock.countDown()
        }
        lock.await(2000, TimeUnit.MILLISECONDS)
        Assert.assertNotNull(bufferInfo!!.getOrThrow())
    }

    @Test
    fun testGetMultipleAccounts() {
        val lock = CountDownLatch(1)
        var bufferInfo: Result<List<BufferInfo<AccountInfo>?>>? = null
        metaplex.getMultipleAccountsInfo(listOf(TEST_PUBLICKEY), AccountInfo::class.java) {
            bufferInfo = it
            lock.countDown()
        }
        lock.await(2000, TimeUnit.MILLISECONDS)
        Assert.assertNotNull(bufferInfo!!.getOrThrow())
    }
}