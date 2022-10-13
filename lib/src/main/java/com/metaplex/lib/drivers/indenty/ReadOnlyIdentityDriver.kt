package com.metaplex.lib.drivers.indenty

import com.metaplex.lib.drivers.rpc.JdkRpcDriver
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.drivers.solana.SolanaConnectionDriver
import com.metaplex.lib.extensions.signAndSend
import com.solana.api.Api
import com.solana.core.PublicKey
import com.solana.core.Transaction
import kotlinx.coroutines.*

class ReadOnlyIdentityDriver(override val publicKey: PublicKey, val connection: Connection,
                             private val dispatcher: CoroutineDispatcher = Dispatchers.IO)
    : IdentityDriver {

    @Deprecated("")
    constructor(publicKey: PublicKey, solanaRPC: Api)
            : this(publicKey, SolanaConnectionDriver(JdkRpcDriver(solanaRPC.router.endpoint.url)))

    override fun sendTransaction(
        transaction: Transaction,
        recentBlockHash: String?,
        onComplete: (Result<String>) -> Unit
    ) {
        CoroutineScope(dispatcher).launch {
            onComplete(transaction.signAndSend(connection, listOf(), recentBlockHash))
        }
    }

    override fun signTransaction(
        transaction: Transaction,
        onComplete: (Result<Transaction>) -> Unit
    ) {
        onComplete(Result.failure(IdentityDriverError.MethodNotAvailable))
    }

    override fun signAllTransactions(
        transactions: List<Transaction>,
        onComplete: (Result<List<Transaction?>>) -> Unit
    ) {
        onComplete(Result.failure(IdentityDriverError.MethodNotAvailable))
    }
}