/*
 * CreateCandyMachineTransactionBuilder
 * Metaplex
 * 
 * Created by Funkatronics on 9/28/2022
 */

package com.metaplex.lib.modules.candymachinesv2.builders

import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.experimental.jen.candymachinev2.CandyMachineData
import com.metaplex.lib.experimental.jen.candymachinev2.CandyMachineV2Instructions
import com.metaplex.lib.experimental.jen.candymachinev2.Creator
import com.metaplex.lib.modules.candymachinesv2.models.CandyMachineV2
import com.metaplex.lib.shared.builders.TransactionBuilder
import com.solana.core.PublicKey
import com.solana.core.Sysvar
import com.solana.core.Transaction
import com.solana.programs.SystemProgram
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CreateCandyMachineV2TransactionBuilder(val candyMachine: CandyMachineV2, payer: PublicKey,
                                             connection: Connection,
                                             dispatcher: CoroutineDispatcher = Dispatchers.IO)
    : TransactionBuilder(payer, connection, dispatcher) {

    override suspend fun build(): Result<Transaction> = withContext(dispatcher) {
        Result.success(Transaction().apply {
            candyMachine.apply {

                val lamports: Long = getRentExceptionLimit(accountSize).getOrThrow()

                // Create an empty account for the candy machine.
                addInstruction(
                    SystemProgram.createAccount(
                        fromPublicKey = payer,
                        newAccountPublickey = address,
                        lamports = lamports,
                        space = accountSize,
                        PublicKey(CandyMachineV2.PROGRAM_ADDRESS)
                    ))

                // Initialize the candy machine account.
                addInstruction(
                    CandyMachineV2Instructions.initializeCandyMachine(
                        candyMachine = address,
                        wallet = wallet,
                        authority = authority,
                        payer = payer,
                        systemProgram = SystemProgram.PROGRAM_ID,
                        rent = Sysvar.SYSVAR_RENT_PUBKEY,
                        data = CandyMachineData(
                            uuid = uuid,
                            price = price.toULong(),
                            symbol = symbol ?: String(),
                            sellerFeeBasisPoints = sellerFeeBasisPoints,
                            maxSupply = maxEditionSupply.toULong(),
                            isMutable = isMutable,
                            retainAuthority = retainAuthority,
                            goLiveDate = System.currentTimeMillis(),//goLiveDate?.epochMillis(),
                            endSettings = endSettings,
                            creators = listOf(Creator(payer, false, 100.toUByte())),
                            hiddenSettings = hiddenSettings, // not supported in v0.1
                            whitelistMintSettings = whitelistMintSettings, // not supported in v0.1
                            itemsAvailable = itemsAvailable.toULong(),
                            gatekeeper = null // not supported in v0.1
                        )
                    ))
            }

        })
    }
}