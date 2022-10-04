package com.metaplex.lib.drivers.indenty

import com.metaplex.lib.drivers.rpc.JdkRpcDriver
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.drivers.solana.SolanaConnectionDriver
import com.metaplex.lib.extensions.signAndSend
import com.solana.api.Api
import com.solana.api.sendTransaction
import com.solana.core.Account
import com.solana.core.PublicKey
import com.solana.core.Transaction
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class KeypairIdentityDriver(private val account: Account, val connection: Connection,
                            private val dispatcher: CoroutineDispatcher = Dispatchers.IO):
    IdentityDriver {

    @Deprecated("")
    constructor(solanaRPC: Api, account: Account)
            : this(account, SolanaConnectionDriver(JdkRpcDriver(solanaRPC.router.endpoint.url)))

    override val publicKey: PublicKey = account.publicKey
//    private val secretKey: ByteArray = account.secretKey

    override fun sendTransaction(
        transaction: Transaction,
        recentBlockHash: String?,
        onComplete: ((Result<String>) -> Unit)
    ) {
        CoroutineScope(dispatcher).launch {
            onComplete(transaction.signAndSend(connection, listOf(), recentBlockHash))
        }
    }

    override fun signTransaction(
        transaction: Transaction,
        onComplete: (Result<Transaction>) -> Unit
    ) {
        transaction.sign(account)
        onComplete(Result.success(transaction))
    }

    override fun signAllTransactions(
        transactions: List<Transaction>,
        onComplete: (Result<List<Transaction?>>) -> Unit
    ) {
        val signedTransactions = arrayListOf<Transaction>()
        transactions.forEach {
            it.sign(account)
            signedTransactions.add(it)
        }
        onComplete(Result.success(signedTransactions))
    }
}