/*
 * CreateCandyMachineTransactionBuilder
 * Metaplex
 * 
 * Created by Funkatronics on 9/28/2022
 */

package com.metaplex.lib.modules.candymachines.builders

import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.experimental.jen.candyguard.CandyGuardInstructions
import com.metaplex.lib.experimental.jen.candymachine.*
import com.metaplex.lib.modules.candymachines.models.CandyGuard
import com.metaplex.lib.modules.candymachines.models.CandyMachine
import com.metaplex.lib.modules.candymachines.models.authorityPda
import com.metaplex.lib.modules.candymachines.models.pda
import com.metaplex.lib.programs.token_metadata.MasterEditionAccount
import com.metaplex.lib.programs.token_metadata.TokenMetadataProgram
import com.metaplex.lib.programs.token_metadata.accounts.MetadataAccount
import com.metaplex.lib.programs.token_metadata.collectionAuthorityRecordPda
import com.metaplex.lib.shared.builders.TransactionBuilder
import com.solana.core.PublicKey
import com.solana.core.Sysvar
import com.solana.core.Transaction
import com.solana.programs.SystemProgram
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CreateCandyMachineTransactionBuilder(
    val candyMachine: CandyMachine, val withoutCandyGuard: Boolean = false, payer: PublicKey,
    connection: Connection, dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TransactionBuilder(payer, connection, dispatcher) {

    override suspend fun build(): Result<Transaction> = withContext(dispatcher) {
        Result.success(Transaction().apply {
            candyMachine.apply {

                val lamports: Long = getRentExceptionLimit(accountSize).getOrThrow()

                val authorityPda = authorityPda.address
                val collectionMetadata = MetadataAccount.pda(collectionMintAddress).getOrThrows()
                val collectionMasterEdition = MasterEditionAccount.pda(collectionMintAddress).getOrThrows()
                val collectionAuthorityRecord =
                    collectionAuthorityRecordPda(collectionMintAddress, authorityPda).address

                // Create an empty account for the candy machine.
                addInstruction(
                    SystemProgram.createAccount(
                        fromPublicKey = payer,
                        newAccountPublickey = address,
                        lamports = lamports,
                        space = accountSize,
                        PublicKey(CandyMachine.PROGRAM_ADDRESS)
                    ))

                // Initialize the candy machine account.
                addInstruction(
                    CandyMachineInstructions.initialize(
                        candyMachine = address,
                        authorityPda = authorityPda,
                        authority = authority,
                        payer = payer,
                        collectionMetadata = collectionMetadata,
                        collectionMint = collectionMintAddress,
                        collectionMasterEdition = collectionMasterEdition,
                        collectionUpdateAuthority = collectionUpdateAuthority,
                        collectionAuthorityRecord = collectionAuthorityRecord,
                        tokenMetadataProgram = TokenMetadataProgram.publicKey,
                        systemProgram = SystemProgram.PROGRAM_ID,
                        data = CandyMachineData(
                            itemsAvailable = itemsAvailable.toULong(),
                            symbol = symbol ?: String(),
                            sellerFeeBasisPoints = sellerFeeBasisPoints,
                            maxSupply = maxEditionSupply.toULong(),
                            isMutable = isMutable,
                            creators = creators ?: listOf(Creator(payer, false, 100.toUByte())),
                            configLineSettings = configLineSettings,
                            hiddenSettings = hiddenSettings,
                        )
                    ))

                if (withoutCandyGuard) return@apply

                val candyGuard = CandyGuard(payer, authority)

                CreateCandyGuardTransactionBuilder(candyGuard, payer, connection, dispatcher)
                    .build()
                    .getOrThrow()
                    .instructions
                    .forEach { ix -> addInstruction(ix) }

                WrapCandyGuardTransactionBuilder(payer, address, authority, payer, connection, dispatcher)
                    .build()
                    .getOrThrow()
                    .instructions
                    .forEach { ix -> addInstruction(ix) }
            }
        })
    }
}