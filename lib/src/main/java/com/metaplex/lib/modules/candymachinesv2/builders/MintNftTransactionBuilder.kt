/*
 * MintNftTransactionBuilder
 * Metaplex
 * 
 * Created by Funkatronics on 9/28/2022
 */

package com.metaplex.lib.modules.candymachinesv2.builders

import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.experimental.jen.candymachinev2.CandyMachineV2Instructions
import com.metaplex.lib.extensions.*
import com.metaplex.lib.modules.candymachinesv2.models.CandyMachineV2
import com.metaplex.lib.modules.candymachinesv2.models.creatorPda
import com.metaplex.lib.modules.token.builders.addMintWithTokenInstruction
import com.metaplex.lib.programs.token_metadata.MasterEditionAccount
import com.metaplex.lib.programs.token_metadata.TokenMetadataProgram
import com.metaplex.lib.programs.token_metadata.accounts.MetadataAccount
import com.metaplex.lib.programs.tokens.TokenProgram
import com.metaplex.lib.shared.builders.TransactionBuilder
import com.solana.core.PublicKey
import com.solana.core.Sysvar
import com.solana.core.Transaction
import com.solana.programs.SystemProgram
import kotlinx.coroutines.*

class MintNftTransactionBuilder(val candyMachine: CandyMachineV2, val newMint: PublicKey, payer: PublicKey, connection: Connection,
                                dispatcher: CoroutineDispatcher = Dispatchers.IO)
    : TransactionBuilder(payer, connection, dispatcher) {

    override suspend fun build(): Result<Transaction> = withContext(dispatcher) {
        Result.success(Transaction().apply {
            candyMachine.apply {

                val newMetadata = MetadataAccount.pda(newMint).getOrThrows()
                val newEdition = MasterEditionAccount.pda(newMint).getOrThrows()

                addMintWithTokenInstruction(payer, newMint)

                // Initialize the candy machine account.
                addInstruction(CandyMachineV2Instructions.mintNft(
                    candyMachine = address,
                    candyMachineCreator = creatorPda.address,
                    payer = payer,
                    wallet = wallet,
                    metadata = newMetadata,
                    mint = newMint,
                    mintAuthority = payer,
                    updateAuthority = payer,
                    masterEdition = newEdition,
                    tokenMetadataProgram = TokenMetadataProgram.publicKey,
                    tokenProgram = TokenProgram.publicKey,
                    systemProgram = SystemProgram.PROGRAM_ID,
                    rent = Sysvar.SYSVAR_RENT_PUBKEY,
                    clock = Sysvar.SYSVAR_CLOCK_PUBKEY,
                    recentBlockhashes = Sysvar.SYSVAR_SLOT_HASHES_PUBKEY,
                    instructionSysvarAccount = Sysvar.SYSVAR_INSTRUCTIONS_PUBKEY,
                    creatorBump = creatorPda.nonce.toUByte(),
                ))
            }
        })
    }
}