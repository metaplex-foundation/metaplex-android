/*
 * Transaction+Extensions
 * Metaplex
 * 
 * Created by Funkatronics on 9/21/2022
 */

package com.metaplex.lib.extensions

import android.util.Base64
import com.metaplex.lib.drivers.indenty.IdentityDriver
import com.metaplex.lib.drivers.solana.*
import com.solana.core.Account
import com.solana.core.HotAccount
import com.solana.core.Transaction
import com.solana.vendor.ShortvecEncoding
import com.solana.vendor.TweetNaclFast
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.builtins.serializer
import org.bitcoinj.core.Base58
import java.nio.ByteBuffer
import kotlin.coroutines.suspendCoroutine
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

suspend fun Transaction.sign(connection: Connection, payer: IdentityDriver,
                             additionalSigners: List<Account> = listOf()): Result<Transaction> {

    // set block hash
    setRecentBlockHash(connection.getRecentBlockhash().getOrElse {
        return Result.failure(it) // we cant proceed further, return the error
    })

    // sign transaction
    val result: Result<Transaction> =  suspendCoroutine { continuation ->

        // first sign with payer (identity driver)
        payer.signTransaction(this) { result ->
            result.onSuccess { signedTx ->

                // sign with additional signers
                additionalSigners.forEach { signedTx.partialSign(it) }

                // resume
                continuation.resumeWith(Result.success(Result.success(signedTx)))
            }.onFailure { continuation.resumeWith(Result.failure(it)) }
        }
    }

    return result
}

suspend fun Transaction.signAndSend(connection: Connection, payer: IdentityDriver,
                                    additionalSigners: List<Account> = listOf()): Result<String> {

    val signedTxn: Transaction = sign(connection, payer, additionalSigners).getOrElse {
        return Result.failure(it)
    }

    val serializedTxn = Base64.encodeToString(signedTxn.serialize(), Base64.DEFAULT)

    // sign and send transaction
    return connection.get(
        SendTransactionRequest(serializedTxn, connection.transactionOptions), String.serializer()
    )
}

suspend fun Transaction.signAndSend(connection: Connection, signers: List<Account> = listOf(),
                                    recentBlockhash: String? = null): Result<String> {

    // set block hash
    setRecentBlockHash(recentBlockhash ?: connection.getRecentBlockhash().getOrElse {
        return Result.failure(it) // we cant proceed further, return the error
    })

    // sign transaction
    sign(signers)

    // send transaction
    return connection.sendTransaction(Base64.encodeToString(serialize(), Base64.DEFAULT))
}

suspend fun Transaction.signSendAndConfirm(
    connection: Connection, payer: IdentityDriver, additionalSigners: List<Account> = listOf(),
    transactionOptions: TransactionOptions = connection.transactionOptions
): Result<String> = signAndSend(connection, payer, additionalSigners)
    .confirmTransaction(connection, transactionOptions)
