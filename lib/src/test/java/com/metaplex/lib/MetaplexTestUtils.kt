package com.metaplex.lib

import com.metaplex.lib.drivers.indenty.ReadOnlyIdentityDriver
import com.metaplex.lib.drivers.storage.MemoryStorageDriver
import com.metaplex.lib.drivers.storage.StorageDriver
import com.metaplex.lib.solana.SolanaConnectionDriver
import com.solana.core.PublicKey
import com.solana.networking.RPCEndpoint

object MetaplexTestUtils {

    val TEST_ACCOUNT_PUBLICKEY =
        PublicKey("CN87nZuhnFdz74S9zn3bxCcd5ZxW55nwvgAv5C2Tz3K7")
}

fun MetaplexTestUtils.generateMetaplexInstance(storageDriver: StorageDriver = MemoryStorageDriver()): Metaplex {
    val solanaConnection = SolanaConnectionDriver(RPCEndpoint.mainnetBetaSolana)
    val solanaIdentityDriver = ReadOnlyIdentityDriver(TEST_ACCOUNT_PUBLICKEY, solanaConnection.solanaRPC)
    return Metaplex(solanaConnection, solanaIdentityDriver, storageDriver)
}