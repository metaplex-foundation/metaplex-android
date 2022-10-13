/*
 * MintNftTransactionBuilder
 * Metaplex
 * 
 * Created by Funkatronics on 9/28/2022
 */

package com.metaplex.lib.modules.candymachines.builders

import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.experimental.jen.candymachine.CandyMachineInstructions
import com.metaplex.lib.extensions.SYSVAR_SLOT_HASHES_PUBKEY
import com.metaplex.lib.modules.candymachines.models.CandyMachine
import com.metaplex.lib.modules.candymachines.models.authorityPda
import com.metaplex.lib.modules.token.builders.addMintWithTokenInstruction
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

class MintNftTransactionBuilder(val candyMachine: CandyMachine, val newMint: PublicKey, payer: PublicKey, connection: Connection,
                                dispatcher: CoroutineDispatcher = Dispatchers.IO)
    : TransactionBuilder(payer, connection, dispatcher) {

    override suspend fun build(): Result<Transaction> = withContext(dispatcher) {
        Result.success(Transaction().apply {
            candyMachine.apply {

                val authorityPda = authorityPda.address
                val collectionMetadata = MetadataAccount.pda(collectionMintAddress).getOrThrows()
                val collectionMasterEdition = MasterEditionAccount.pda(collectionMintAddress).getOrThrows()
                val collectionAuthorityRecord =
                    collectionAuthorityRecordPda(collectionMintAddress, authorityPda).address

                val newMetadata = MetadataAccount.pda(newMint).getOrThrows()
                val newEdition = MasterEditionAccount.pda(newMint).getOrThrows()

                addMintWithTokenInstruction(payer, newMint)

                addInstruction(
                    CandyMachineInstructions.mint(
                        candyMachine = address,
                        authorityPda = authorityPda,
                        mintAuthority = payer,
                        payer = payer,
                        nftMint = newMint,
                        nftMintAuthority = payer,
                        nftMetadata = newMetadata,
                        nftMasterEdition = newEdition,
                        collectionAuthorityRecord = collectionAuthorityRecord,
                        collectionMint = collectionMintAddress,
                        collectionMetadata = collectionMetadata,
                        collectionMasterEdition = collectionMasterEdition,
                        collectionUpdateAuthority = collectionUpdateAuthority,
                        tokenMetadataProgram = TokenMetadataProgram.publicKey,
                        tokenProgram = com.solana.programs.TokenProgram.PROGRAM_ID,
                        systemProgram = SystemProgram.PROGRAM_ID,
                        recentSlothashes = Sysvar.SYSVAR_SLOT_HASHES_PUBKEY
                    ))
            }
        })
    }
}