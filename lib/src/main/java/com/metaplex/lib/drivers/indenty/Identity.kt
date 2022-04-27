package com.metaplex.lib.drivers.indenty

import com.solana.core.PublicKey
import com.solana.core.Transaction
import java.lang.Error

interface IdentityDriver {
    val publicKey: PublicKey
    fun sendTransaction(transaction: Transaction, recentBlockHash: String? = null, onComplete: ((Result<String>) -> Unit))
    fun signTransaction(transaction: Transaction, onComplete: (Result<Transaction>) -> Unit)
    fun signAllTransactions(transactions: List<Transaction>, onComplete: (Result<List<Transaction?>>) -> Unit)

    fun `is`(other: IdentityDriver): Boolean {
        return publicKey == other.publicKey
    }
}

sealed class IdentityDriverError: Error() {
    object MethodNotAvailable: IdentityDriverError()
    data class SendTransactionError(val error: Error): IdentityDriverError()
}