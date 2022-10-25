package com.metaplex.lib

import com.metaplex.lib.drivers.indenty.KeypairIdentityDriver
import com.metaplex.lib.drivers.indenty.ReadOnlyIdentityDriver
import com.metaplex.lib.drivers.rpc.JdkRpcDriver
import com.metaplex.lib.drivers.solana.Commitment
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.drivers.solana.SolanaConnectionDriver
import com.metaplex.lib.drivers.solana.TransactionOptions
import com.metaplex.lib.drivers.storage.MemoryStorageDriver
import com.metaplex.lib.drivers.storage.StorageDriver
import com.solana.core.Account
import com.solana.core.HotAccount
import com.solana.core.PublicKey
import com.solana.networking.RPCEndpoint
import java.net.URL

object MetaplexTestUtils {
    const val RPC_URL = BuildConfig.RPC_URL

    val DEFAULT_TRANSACTION_OPTIONS = TransactionOptions(Commitment.CONFIRMED, skipPreflight = true)

    val TEST_ACCOUNT_PUBLICKEY = PublicKey(SolanaTestData.TEST_ACCOUNT_PUBLICKEY_STRING)
}

fun MetaplexTestUtils.generateConnectionDriver(
    rpcURL: URL = URL(RPC_URL), txOptions: TransactionOptions = DEFAULT_TRANSACTION_OPTIONS
) = SolanaConnectionDriver(JdkRpcDriver(rpcURL), txOptions)

fun MetaplexTestUtils.generateMetaplexInstance(
    account: Account = HotAccount(), connectionDriver: Connection = generateConnectionDriver(),
    storageDriver: StorageDriver = MemoryStorageDriver()
): Metaplex {
    val identityDriver = KeypairIdentityDriver(account, connectionDriver)
    return Metaplex(connectionDriver, identityDriver, storageDriver)
}

val MetaplexTestUtils.readOnlyMainnetMetaplexInstance: Metaplex get() {
    val solanaConnection = SolanaConnectionDriver(RPCEndpoint.mainnetBetaSolana)
    val solanaIdentityDriver = ReadOnlyIdentityDriver(TEST_ACCOUNT_PUBLICKEY, solanaConnection)
    return Metaplex(solanaConnection, solanaIdentityDriver, MemoryStorageDriver())
}