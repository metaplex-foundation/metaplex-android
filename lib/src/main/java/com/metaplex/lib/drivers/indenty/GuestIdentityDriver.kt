package com.metaplex.lib.drivers.indenty

import com.solana.api.Api
import com.solana.api.sendTransaction
import com.solana.core.Account
import com.solana.core.PublicKey
import com.solana.core.Transaction

class GuestIdentityDriver(val solanaRPC: Api) : IdentityDriver {
    override val publicKey: PublicKey = PublicKey("11111111111111111111111111111111")
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