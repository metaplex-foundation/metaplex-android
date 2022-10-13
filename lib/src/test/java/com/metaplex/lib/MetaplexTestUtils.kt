package com.metaplex.lib

import com.metaplex.lib.drivers.indenty.ReadOnlyIdentityDriver
import com.metaplex.lib.drivers.solana.SolanaConnectionDriver
import com.metaplex.lib.drivers.storage.MemoryStorageDriver
import com.metaplex.lib.drivers.storage.StorageDriver
import com.solana.core.PublicKey
import com.solana.networking.RPCEndpoint

object MetaplexTestUtils {
    val TEST_ACCOUNT_PUBLICKEY = PublicKey(SolanaTestData.TEST_ACCOUNT_PUBLICKEY_STRING)
}

fun MetaplexTestUtils.generateMetaplexInstance(accountPublicKey: PublicKey = TEST_ACCOUNT_PUBLICKEY,
                                               storageDriver: StorageDriver = MemoryStorageDriver())
: Metaplex {
    val solanaConnection = SolanaConnectionDriver(RPCEndpoint.mainnetBetaSolana)
    val solanaIdentityDriver = ReadOnlyIdentityDriver(accountPublicKey, solanaConnection)
    return Metaplex(solanaConnection, solanaIdentityDriver, storageDriver)
}