/*
 * Connection+Extensions
 * Metaplex
 * 
 * Created by Funkatronics on 10/19/2022
 */

package com.metaplex.lib.extensions

import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.drivers.solana.TransactionOptions
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withTimeout

suspend fun Result<String>.confirmTransaction(
    connection: Connection, transactionOptions: TransactionOptions = connection.transactionOptions
): Result<String> = withTimeout(transactionOptions.timeout.toMillis()) {

    suspend fun confirmationStatus() =
        connection.getSignatureStatuses(listOf(getOrThrow()), null)
            .getOrNull()?.first()?.confirmationStatus

    // wait for desired transaction status
    while(confirmationStatus() != transactionOptions.commitment.toString()) {

        // wait a bit before retrying
        val millis = System.currentTimeMillis()
        var inc = 0
        while(System.currentTimeMillis() - millis < 300 && isActive) { inc++ }

        if (!isActive) break // breakout after timeout
    }

    this@confirmTransaction // return result
}