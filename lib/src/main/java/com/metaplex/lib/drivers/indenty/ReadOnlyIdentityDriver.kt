package com.metaplex.lib.drivers.indenty

import com.solana.api.Api
import com.solana.api.sendTransaction
import com.solana.core.PublicKey
import com.solana.core.Transaction

class ReadOnlyIdentityDriver(override val publicKey: PublicKey, val solanaRPC: Api) : IdentityDriver {
    override fun sendTransaction(
        transaction: Transaction,
        recentBlockHash: String?,
        onComplete: (Result<String>) -> Unit
    ) {
        solanaRPC.sendTransaction(transaction, listOf(), recentBlockHash) { result ->
            result.onSuccess {
                onComplete(Result.success(it))
            }.onFailure {
                onComplete(Result.failure(it))
            }
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