package com.metaplex.lib.shared.builders

import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.drivers.solana.MinAccountBalanceRequest
import com.solana.core.PublicKey
import com.solana.core.Transaction
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.builtins.serializer

abstract class TransactionBuilder(internal val payer: PublicKey, internal val connection: Connection,
                                  internal val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    abstract suspend fun build(): Result<Transaction>

    internal suspend fun getRentExceptionLimit(accountSize: Long): Result<Long> =
        connection.get(MinAccountBalanceRequest(accountSize), Long.serializer())
}