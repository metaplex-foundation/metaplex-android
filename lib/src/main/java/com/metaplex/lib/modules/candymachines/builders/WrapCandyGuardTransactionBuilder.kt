/*
 * WrapCandyGuardTransactionBuilder
 * Metaplex
 * 
 * Created by Funkatronics on 11/1/2022
 */

package com.metaplex.lib.modules.candymachines.builders

import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.experimental.jen.candyguard.CandyGuardInstructions
import com.metaplex.lib.modules.candymachines.models.CandyGuard
import com.metaplex.lib.modules.candymachines.models.CandyMachine
import com.metaplex.lib.modules.candymachines.models.pda
import com.metaplex.lib.shared.builders.TransactionBuilder
import com.solana.core.PublicKey
import com.solana.core.Transaction
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class WrapCandyGuardTransactionBuilder(
    val candyGuardBase: PublicKey, val candyMachine: PublicKey, val authority: PublicKey,
    payer: PublicKey, connection: Connection, dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TransactionBuilder(payer, connection, dispatcher) {

    override suspend fun build(): Result<Transaction> = Result.success(Transaction().apply {
        addInstruction(
            CandyGuardInstructions.wrap(
                candyGuard = CandyGuard.pda(candyGuardBase).address,
                authority = authority,
                candyMachine = candyMachine,
                candyMachineProgram = PublicKey(CandyMachine.PROGRAM_ADDRESS),
                candyMachineAuthority = authority
        ))
    })
}