/*
 * Helpers
 * Metaplex
 * 
 * Created by Funkatronics on 10/4/2022
 */

package com.metaplex.unit.lib.modules.candymachine

import com.metaplex.lib.drivers.solana.Commitment
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.drivers.solana.TransactionOptions
import com.metaplex.lib.experimental.jen.candymachine.CandyMachineInstructions
import com.metaplex.lib.experimental.jen.candymachine.ConfigLine
import com.metaplex.lib.experimental.jen.tokenmetadata.*
import com.metaplex.lib.extensions.signSendAndConfirm
import com.metaplex.lib.modules.candymachines.CandyMachineClient
import com.metaplex.lib.modules.candymachines.models.CandyMachine
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.modules.nfts.operations.FindNftByMintOnChainOperationHandler
import com.metaplex.lib.modules.token.builders.addMintWithTokenInstruction
import com.metaplex.lib.programs.token_metadata.MasterEditionAccount
import com.metaplex.lib.programs.token_metadata.accounts.MetadataAccount
import com.metaplex.lib.shared.builders.TransactionBuilder
import com.solana.core.Account
import com.solana.core.PublicKey
import com.solana.core.Sysvar
import com.solana.core.Transaction
import com.solana.programs.SystemProgram
import com.solana.programs.TokenProgram
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// will eventually move these into appropriate clients, but need to keep things in scope for now

suspend fun CandyMachineClient.createNft(transactionOptions: TransactionOptions =
        TransactionOptions(commitment = Commitment.CONFIRMED, skipPreflight = true))
: Result<NFT> = runCatching {

    val newMintAccount = Account()

    CreateNftTransactionBuilder(newMintAccount.publicKey, signer.publicKey, connection)//, dispatcher)
        .build().getOrThrow()
        .signSendAndConfirm(connection, signer, listOf(newMintAccount), transactionOptions)

    return FindNftByMintOnChainOperationHandler(connection)//, dispatcher)
        .handle(newMintAccount.publicKey)
}

class CreateNftTransactionBuilder(val newMint: PublicKey, payer: PublicKey,
                                  connection: Connection,
                                  dispatcher: CoroutineDispatcher = Dispatchers.IO)
    : TransactionBuilder(payer, connection, dispatcher) {

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
                rent = Sysvar.SYSVAR_RENT_ADDRESS,
                createMetadataAccountArgsV3 = CreateMetadataAccountArgsV3(
                    DataV2(
                        name = "My NFT",
                        symbol = "",
                        uri = "https://mockstorage.example.com/NSJTGKyev6YwYyEwxgLS",
                        sellerFeeBasisPoints = 333.toUShort(),
                        creators = listOf(Creator(payer, true, 100.toUByte())),
                        collection = null,
                        uses = null),true, CollectionDetails.V1
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
                rent = Sysvar.SYSVAR_RENT_ADDRESS,
                createMasterEditionArgs = CreateMasterEditionArgs(0.toULong())
            ))
        })
    }
}

suspend fun CandyMachineClient.insertItems(
    candyMachine: CandyMachine, transactionOptions: TransactionOptions =
        TransactionOptions(commitment = Commitment.CONFIRMED, skipPreflight = true)
): Result<String> =
    AddConfigLinesTransactionBuilder(candyMachine, signer.publicKey, connection)//, dispatcher)
        .build().mapCatching {
            it.signSendAndConfirm(connection, signer, transactionOptions = transactionOptions)
                .getOrThrow()
        }

class AddConfigLinesTransactionBuilder(val candyMachine: CandyMachine, payer: PublicKey,
                                       connection: Connection,
                                       dispatcher: CoroutineDispatcher = Dispatchers.IO)
    : TransactionBuilder(payer, connection, dispatcher) {

    val configLines = mutableListOf<ConfigLine>()

    override suspend fun build(): Result<Transaction> = withContext(dispatcher) {
        Result.success(Transaction().apply {
            candyMachine.apply {

                addInstruction(
                    CandyMachineInstructions.addConfigLines(
                    candyMachine = address,
                    authority= payer,
                    index = 0.toUInt(),
                    configLines = listOf(ConfigLine("My Nft", "http://example.com/mynft"))
                ))
            }
        })
    }
}