/*
 * CreateNftTransactionBuilder
 * metaplex-android
 * 
 * Created by Funkatronics on 10/11/2022
 */

package com.metaplex.lib.modules.nfts.builders

import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.experimental.jen.tokenmetadata.*
import com.metaplex.lib.modules.nfts.models.Metadata
import com.metaplex.lib.modules.token.builders.addMintWithTokenInstruction
import com.metaplex.lib.programs.token_metadata.MasterEditionAccount
import com.metaplex.lib.programs.token_metadata.accounts.MetadataAccount
import com.metaplex.lib.shared.builders.TransactionBuilder
import com.solana.core.PublicKey
import com.solana.core.Sysvar
import com.solana.core.Transaction
import com.solana.programs.SystemProgram
import com.solana.programs.TokenProgram
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CreateNftTransactionBuilder(
    val newMint: PublicKey, val metadata: Metadata, val isCollection: Boolean = false,
    payer: PublicKey, connection: Connection, dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TransactionBuilder(payer, connection, dispatcher) {

    override suspend fun build(): Result<Transaction> = withContext(dispatcher) {
        Result.success(Transaction().apply {

            val newMetadata = MetadataAccount.pda(newMint).getOrThrows()
            val newEdition = MasterEditionAccount.pda(newMint).getOrThrows()

            addMintWithTokenInstruction(payer, newMint)

            addInstruction(
                TokenMetadataInstructions.CreateMetadataAccountV3(
                    metadata = newMetadata,
                    mint = newMint,
                    mintAuthority = payer,
                    payer = payer,
                    updateAuthority = payer,
                    systemProgram = SystemProgram.PROGRAM_ID,
                    rent = Sysvar.SYSVAR_RENT_PUBKEY,
                    createMetadataAccountArgsV3 = CreateMetadataAccountArgsV3(
                        DataV2(
                            name = metadata.name,
                            symbol = metadata.symbol,
                            uri = metadata.uri,
                            sellerFeeBasisPoints = metadata.sellerFeeBasisPoints.toUShort(),
                            creators = metadata.creators ?: listOf(
                                Creator(payer, true, 100.toUByte())
                            ),
                            collection = metadata.collection?.let { Collection(false, it) },
                            uses = null
                        ),
                        metadata.isMutable,
                        if (isCollection) CollectionDetails.V1 else null
                    )
                ))

            addInstruction(
                TokenMetadataInstructions.CreateMasterEditionV3(
                    edition = newEdition,
                    mint = newMint,
                    updateAuthority = payer,
                    mintAuthority = payer,
                    payer = payer,
                    metadata = newMetadata,
                    tokenProgram = TokenProgram.PROGRAM_ID,
                    systemProgram = SystemProgram.PROGRAM_ID,
                    rent = Sysvar.SYSVAR_RENT_PUBKEY,
                    createMasterEditionArgs = CreateMasterEditionArgs(0.toULong())
                ))
        })
    }
}