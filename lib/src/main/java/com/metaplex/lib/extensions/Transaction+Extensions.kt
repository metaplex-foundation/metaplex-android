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
                // cant sign directly because Transaction.sign() does not account for previous signatures
//                signedTx.sign(additionalSigners)

                // need to modify the signing logic of Transaction to make this work
                // (signing payer with Identity Driver, then signing with list of Account)

                // have to use reflection (gross) because these are private to Transaction
                // this is temporary. I will make a change to SolanaKt, or use the partial signing
                val serializedMessage: ByteArray? =
                    Transaction::class.memberProperties.find { it.name == "serializedMessage" }
                        ?.let {
                            it.isAccessible = true
                            it.get(this) as ByteArray
                        }

                val signatures: MutableList<String> =
                    Transaction::class.memberProperties.find { it.name == "signatures" }?.let {
                        it.isAccessible = true
                        it.get(this) as MutableList<String>
                    } ?: mutableListOf()

                // require serializedMessage and signature list, safe to force unwrap after this
                require(serializedMessage != null && signatures.isNotEmpty())

                // taken from Transaction.sign()
                for (signer in additionalSigners) {
                    val signatureProvider = TweetNaclFast.Signature(ByteArray(0), signer.secretKey)
                    val signature = signatureProvider.detached(serializedMessage)
                    signatures.add(Base58.encode(signature))
                }

                // taken from Transaction.serialize()
                val signaturesSize = signatures.size
                val signaturesLength = ShortvecEncoding.encodeLength(signaturesSize)
                val out = ByteBuffer.allocate(signaturesLength.size
                        + signaturesSize * Transaction.SIGNATURE_LENGTH + serializedMessage.size)
                out.put(signaturesLength)
                for (signature in signatures) {
                    val rawSignature = Base58.decode(signature)
                    out.put(rawSignature)
                }
                out.put(serializedMessage)

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
): Result<String> =
    signAndSend(connection, payer, additionalSigners).also {
        withTimeout(transactionOptions.timeout.toMillis()) {

            suspend fun confirmationStatus() =
                connection.getSignatureStatuses(listOf(it.getOrThrow()), null)
                    .getOrNull()?.first()?.confirmationStatus

            // wait for desired transaction status
            while(confirmationStatus() != transactionOptions.commitment.toString()) {

                // wait a bit before retrying
                val millis = System.currentTimeMillis()
                var inc = 0
                while(System.currentTimeMillis() - millis < 300 && isActive) { inc++ }

                if (!isActive) break // breakout after timeout
            }
        }
    }
