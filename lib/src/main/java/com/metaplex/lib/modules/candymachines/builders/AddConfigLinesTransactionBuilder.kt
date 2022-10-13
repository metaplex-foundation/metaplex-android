/*
 * AddConfigLinesTransactionBuilder
 * metaplex-android
 * 
 * Created by Funkatronics on 10/6/2022
 */

package com.metaplex.lib.modules.candymachines.builders

import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.experimental.jen.candymachine.CandyMachineInstructions
import com.metaplex.lib.experimental.jen.candymachine.ConfigLine
import com.metaplex.lib.modules.candymachines.models.CandyMachine
import com.metaplex.lib.modules.candymachines.models.CandyMachineItem
import com.metaplex.lib.shared.builders.TransactionBuilder
import com.solana.core.PublicKey
import com.solana.core.Transaction
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddConfigLinesTransactionBuilder(val candyMachine: CandyMachine, payer: PublicKey,
                                       connection: Connection,
                                       dispatcher: CoroutineDispatcher = Dispatchers.IO)
    : TransactionBuilder(payer, connection, dispatcher) {

    val configLines = mutableListOf<ConfigLine>()

    fun addItems(items: List<ConfigLine>) = this.also { configLines.addAll(items) }

    override suspend fun build(): Result<Transaction> = withContext(dispatcher) {
        Result.success(Transaction().apply {
            candyMachine.apply {
                addInstruction(
                    CandyMachineInstructions.addConfigLines(
                        candyMachine = address,
                        authority= payer,
                        index = itemsLoaded.toUInt(),
                        configLines = configLines
                    ))
            }
        })
    }
}