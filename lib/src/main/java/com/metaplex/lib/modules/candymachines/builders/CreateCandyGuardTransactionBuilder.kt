/*
 * CreateCandyGuardTransactionBuilder
 * metaplex-android
 * 
 * Created by Funkatronics on 10/13/2022
 */

package com.metaplex.lib.modules.candymachines.builders

import com.metaplex.kborsh.Borsh
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.experimental.jen.candyguard.*
import com.metaplex.lib.modules.candymachines.CANDY_GUARD_DATA
import com.metaplex.lib.modules.candymachines.models.*
import com.metaplex.lib.modules.candymachines.models.CandyGuard
import com.metaplex.lib.shared.builders.TransactionBuilder
import com.solana.core.PublicKey
import com.solana.core.Transaction
import com.solana.programs.SystemProgram
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class CreateCandyGuardTransactionBuilder(
    val candyGuard: CandyGuard, payer: PublicKey,
    connection: Connection, dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TransactionBuilder(payer, connection, dispatcher) {

    override suspend fun build(): Result<Transaction> = Result.success(Transaction().apply {

        val data = Borsh.encodeToByteArray(CandyGuardSerializer, candyGuard)
            .run { slice(CANDY_GUARD_DATA until size) }

        addInstruction(CandyGuardInstructions.initialize(
            candyGuard = CandyGuard.pda(candyGuard.base).address,
            base = candyGuard.base,
            authority = candyGuard.authority,
            payer = payer,
            systemProgram = SystemProgram.PROGRAM_ID,
            data = data.toByteArray()
        ))
    })
}