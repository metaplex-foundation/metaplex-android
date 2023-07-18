package com.metaplex.lib

import com.metaplex.lib.drivers.indenty.ReadOnlyIdentityDriver
import com.metaplex.lib.drivers.storage.MemoryStorageDriver
import com.solana.core.HotAccount
import org.junit.Assert
import org.junit.Test

class MetaplexTests {

    @Test
    fun testMetaplexSetUpReturnsValidInstance() {
        // gven
        val connection = MetaplexTestUtils.generateConnectionDriver()
        val identityDriver = ReadOnlyIdentityDriver(HotAccount().publicKey, connection)
        val storageDriver = MemoryStorageDriver()

        // when
        val metaplex = Metaplex(connection, identityDriver, storageDriver)

        // then
        Assert.assertNotNull(metaplex)
    }
}