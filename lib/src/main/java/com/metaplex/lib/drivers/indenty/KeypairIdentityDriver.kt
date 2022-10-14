package com.metaplex.lib.drivers.indenty

import com.solana.api.Api
import com.solana.api.sendTransaction
import com.solana.core.Account
import com.solana.core.PublicKey
import com.solana.core.Transaction

class KeypairIdentityDriver(private val solanaRPC: Api, private val account: Account) :
    IdentityDriver {
    override val publicKey: PublicKey = account.publicKey

    override fun sendTransaction(
        transaction: Transaction,
        recentBlockHash: String?,
        onComplete: ((Result<String>) -> Unit)
    ) {
        this.solanaRPC.sendTransaction(transaction, listOf(account), recentBlockHash, onComplete)
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