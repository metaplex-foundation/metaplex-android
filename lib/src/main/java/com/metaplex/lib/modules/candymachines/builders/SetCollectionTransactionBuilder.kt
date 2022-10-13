/*
 * SetCollectionTransactionBuilder
 * metaplex-android
 * 
 * Created by Funkatronics on 10/11/2022
 */

package com.metaplex.lib.modules.candymachines.builders

import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.experimental.jen.candymachine.CandyMachineInstructions
import com.metaplex.lib.modules.candymachines.models.CandyMachine
import com.metaplex.lib.modules.candymachines.models.authorityPda
import com.metaplex.lib.programs.token_metadata.MasterEditionAccount
import com.metaplex.lib.programs.token_metadata.TokenMetadataProgram
import com.metaplex.lib.programs.token_metadata.accounts.MetadataAccount
import com.metaplex.lib.programs.token_metadata.collectionAuthorityRecordPda
import com.metaplex.lib.shared.builders.TransactionBuilder
import com.solana.core.PublicKey
import com.solana.core.Transaction
import com.solana.programs.SystemProgram
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SetCollectionTransactionBuilder(
    val candyMachine: CandyMachine, val newCollectionMint: PublicKey, payer: PublicKey,
    connection: Connection, dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TransactionBuilder(payer, connection, dispatcher) {

    override suspend fun build(): Result<Transaction> = withContext(dispatcher) {
        Result.success(Transaction().apply {
            candyMachine.apply {

                val authorityPda = authorityPda.address
                val collectionMetadata = MetadataAccount.pda(collectionMintAddress).getOrThrows()
                val collectionAuthorityRecord =
                    collectionAuthorityRecordPda(collectionMintAddress, authorityPda).address

                val newCollectionMetadata = MetadataAccount.pda(newCollectionMint).getOrThrows()
                val newCollectionMasterEdition = MasterEditionAccount.pda(newCollectionMint).getOrThrows()
                val newCollectionAuthorityRecord =
                    collectionAuthorityRecordPda(newCollectionMint, authorityPda).address

                addInstruction(
                    CandyMachineInstructions.setCollection(
                        candyMachine = address,
                        authority = authority,
                        authorityPda = authorityPda, payer,
                        collectionMint = collectionMintAddress,
                        collectionMetadata = collectionMetadata,
                        collectionAuthorityRecord = collectionAuthorityRecord,
                        newCollectionUpdateAuthority = payer,
                        newCollectionMetadata = newCollectionMetadata,
                        newCollectionMint = newCollectionMint,
                        newCollectionMasterEdition = newCollectionMasterEdition,
                        newCollectionAuthorityRecord = newCollectionAuthorityRecord,
                        tokenMetadataProgram = TokenMetadataProgram.publicKey,
                        systemProgram = SystemProgram.PROGRAM_ID,
                    ))
            }
        })
    }
}