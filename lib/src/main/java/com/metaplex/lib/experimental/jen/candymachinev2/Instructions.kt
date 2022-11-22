//
// Instructions
// Metaplex
//
// This code was generated locally by Funkatronics on 2022-09-27
//
package com.metaplex.lib.experimental.jen.candymachinev2

import com.metaplex.kborsh.Borsh
import com.metaplex.lib.serialization.serializers.solana.AnchorInstructionSerializer
import com.metaplex.lib.serialization.serializers.solana.PublicKeyAs32ByteSerializer
import com.solana.core.AccountMeta
import com.solana.core.PublicKey
import com.solana.core.TransactionInstruction
import kotlin.Long
import kotlin.UByte
import kotlin.UInt
import kotlin.collections.List
import kotlinx.serialization.Serializable

object CandyMachineV2Instructions {
    fun initializeCandyMachine(
        candyMachine: PublicKey,
        wallet: PublicKey,
        authority: PublicKey,
        payer: PublicKey,
        systemProgram: PublicKey,
        rent: PublicKey,
        data: CandyMachineData
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("cndy3Z4yapfJBmL3ShUp5exZKqR3z33thTzeNMm2gRZ"),
            listOf(AccountMeta(candyMachine, false, true), AccountMeta(wallet, false, false),
            AccountMeta(authority, false, false), AccountMeta(payer, true, false),
            AccountMeta(systemProgram, false, false), AccountMeta(rent, false, false)),
            Borsh.encodeToByteArray(AnchorInstructionSerializer("initialize_candy_machine"),
            Args_initializeCandyMachine(data)))

    fun updateCandyMachine(
        candyMachine: PublicKey,
        authority: PublicKey,
        wallet: PublicKey,
        data: CandyMachineData
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("cndy3Z4yapfJBmL3ShUp5exZKqR3z33thTzeNMm2gRZ"),
            listOf(AccountMeta(candyMachine, false, true), AccountMeta(authority, true, false),
            AccountMeta(wallet, false, false)),
            Borsh.encodeToByteArray(AnchorInstructionSerializer("update_candy_machine"),
            Args_updateCandyMachine(data)))

    fun updateAuthority(
        candyMachine: PublicKey,
        authority: PublicKey,
        wallet: PublicKey,
        newAuthority: PublicKey?
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("cndy3Z4yapfJBmL3ShUp5exZKqR3z33thTzeNMm2gRZ"),
            listOf(AccountMeta(candyMachine, false, true), AccountMeta(authority, true, false),
            AccountMeta(wallet, false, false)),
            Borsh.encodeToByteArray(AnchorInstructionSerializer("update_authority"),
            Args_updateAuthority(newAuthority)))

    fun addConfigLines(
        candyMachine: PublicKey,
        authority: PublicKey,
        index: UInt,
        configLines: List<ConfigLine>
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("cndy3Z4yapfJBmL3ShUp5exZKqR3z33thTzeNMm2gRZ"),
            listOf(AccountMeta(candyMachine, false, true), AccountMeta(authority, true, false)),
            Borsh.encodeToByteArray(AnchorInstructionSerializer("add_config_lines"),
            Args_addConfigLines(index, configLines)))

    fun setCollection(
        candyMachine: PublicKey,
        authority: PublicKey,
        collectionPda: PublicKey,
        payer: PublicKey,
        systemProgram: PublicKey,
        rent: PublicKey,
        metadata: PublicKey,
        mint: PublicKey,
        edition: PublicKey,
        collectionAuthorityRecord: PublicKey,
        tokenMetadataProgram: PublicKey
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("cndy3Z4yapfJBmL3ShUp5exZKqR3z33thTzeNMm2gRZ"),
            listOf(AccountMeta(candyMachine, false, true), AccountMeta(authority, true, false),
            AccountMeta(collectionPda, false, true), AccountMeta(payer, true, false),
            AccountMeta(systemProgram, false, false), AccountMeta(rent, false, false),
            AccountMeta(metadata, false, false), AccountMeta(mint, false, false),
            AccountMeta(edition, false, false), AccountMeta(collectionAuthorityRecord, false, true),
            AccountMeta(tokenMetadataProgram, false, false)),
            Borsh.encodeToByteArray(AnchorInstructionSerializer("set_collection"),
            Args_setCollection()))

    fun removeCollection(
        candyMachine: PublicKey,
        authority: PublicKey,
        collectionPda: PublicKey,
        metadata: PublicKey,
        mint: PublicKey,
        collectionAuthorityRecord: PublicKey,
        tokenMetadataProgram: PublicKey
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("cndy3Z4yapfJBmL3ShUp5exZKqR3z33thTzeNMm2gRZ"),
            listOf(AccountMeta(candyMachine, false, true), AccountMeta(authority, true, false),
            AccountMeta(collectionPda, false, true), AccountMeta(metadata, false, false),
            AccountMeta(mint, false, false), AccountMeta(collectionAuthorityRecord, false, true),
            AccountMeta(tokenMetadataProgram, false, false)),
            Borsh.encodeToByteArray(AnchorInstructionSerializer("remove_collection"),
            Args_removeCollection()))

    fun mintNft(
        candyMachine: PublicKey,
        candyMachineCreator: PublicKey,
        payer: PublicKey,
        wallet: PublicKey,
        metadata: PublicKey,
        mint: PublicKey,
        mintAuthority: PublicKey,
        updateAuthority: PublicKey,
        masterEdition: PublicKey,
        tokenMetadataProgram: PublicKey,
        tokenProgram: PublicKey,
        systemProgram: PublicKey,
        rent: PublicKey,
        clock: PublicKey,
        recentBlockhashes: PublicKey,
        instructionSysvarAccount: PublicKey,
        creatorBump: UByte
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("cndy3Z4yapfJBmL3ShUp5exZKqR3z33thTzeNMm2gRZ"),
            listOf(AccountMeta(candyMachine, false, true), AccountMeta(candyMachineCreator, false,
            false), AccountMeta(payer, true, false), AccountMeta(wallet, false, true),
            AccountMeta(metadata, false, true), AccountMeta(mint, false, true),
            AccountMeta(mintAuthority, true, false), AccountMeta(updateAuthority, true, false),
            AccountMeta(masterEdition, false, true), AccountMeta(tokenMetadataProgram, false,
            false), AccountMeta(tokenProgram, false, false), AccountMeta(systemProgram, false,
            false), AccountMeta(rent, false, false), AccountMeta(clock, false, false),
            AccountMeta(recentBlockhashes, false, false), AccountMeta(instructionSysvarAccount,
            false, false)), Borsh.encodeToByteArray(AnchorInstructionSerializer("mint_nft"),
            Args_mintNft(creatorBump)))

    fun setCollectionDuringMint(
        candyMachine: PublicKey,
        metadata: PublicKey,
        payer: PublicKey,
        collectionPda: PublicKey,
        tokenMetadataProgram: PublicKey,
        instructions: PublicKey,
        collectionMint: PublicKey,
        collectionMetadata: PublicKey,
        collectionMasterEdition: PublicKey,
        authority: PublicKey,
        collectionAuthorityRecord: PublicKey
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("cndy3Z4yapfJBmL3ShUp5exZKqR3z33thTzeNMm2gRZ"),
            listOf(AccountMeta(candyMachine, false, false), AccountMeta(metadata, false, false),
            AccountMeta(payer, true, false), AccountMeta(collectionPda, false, true),
            AccountMeta(tokenMetadataProgram, false, false), AccountMeta(instructions, false,
            false), AccountMeta(collectionMint, false, false), AccountMeta(collectionMetadata,
            false, true), AccountMeta(collectionMasterEdition, false, false), AccountMeta(authority,
            false, false), AccountMeta(collectionAuthorityRecord, false, false)),
            Borsh.encodeToByteArray(AnchorInstructionSerializer("set_collection_during_mint"),
            Args_setCollectionDuringMint()))

    fun withdrawFunds(candyMachine: PublicKey, authority: PublicKey): TransactionInstruction =
            TransactionInstruction(PublicKey("cndy3Z4yapfJBmL3ShUp5exZKqR3z33thTzeNMm2gRZ"),
            listOf(AccountMeta(candyMachine, false, true), AccountMeta(authority, true, true)),
            Borsh.encodeToByteArray(AnchorInstructionSerializer("withdraw_funds"),
            Args_withdrawFunds()))

    fun setFreeze(
        candyMachine: PublicKey,
        authority: PublicKey,
        freezePda: PublicKey,
        systemProgram: PublicKey,
        freezeTime: Long
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("cndy3Z4yapfJBmL3ShUp5exZKqR3z33thTzeNMm2gRZ"),
            listOf(AccountMeta(candyMachine, false, true), AccountMeta(authority, true, true),
            AccountMeta(freezePda, false, true), AccountMeta(systemProgram, false, false)),
            Borsh.encodeToByteArray(AnchorInstructionSerializer("set_freeze"),
            Args_setFreeze(freezeTime)))

    fun removeFreeze(
        candyMachine: PublicKey,
        authority: PublicKey,
        freezePda: PublicKey
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("cndy3Z4yapfJBmL3ShUp5exZKqR3z33thTzeNMm2gRZ"),
            listOf(AccountMeta(candyMachine, false, true), AccountMeta(authority, true, true),
            AccountMeta(freezePda, false, true)),
            Borsh.encodeToByteArray(AnchorInstructionSerializer("remove_freeze"),
            Args_removeFreeze()))

    fun thawNft(
        freezePda: PublicKey,
        candyMachine: PublicKey,
        tokenAccount: PublicKey,
        owner: PublicKey,
        mint: PublicKey,
        edition: PublicKey,
        payer: PublicKey,
        tokenProgram: PublicKey,
        tokenMetadataProgram: PublicKey,
        systemProgram: PublicKey
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("cndy3Z4yapfJBmL3ShUp5exZKqR3z33thTzeNMm2gRZ"),
            listOf(AccountMeta(freezePda, false, true), AccountMeta(candyMachine, false, true),
            AccountMeta(tokenAccount, false, true), AccountMeta(owner, false, false),
            AccountMeta(mint, false, false), AccountMeta(edition, false, false), AccountMeta(payer,
            true, true), AccountMeta(tokenProgram, false, false), AccountMeta(tokenMetadataProgram,
            false, false), AccountMeta(systemProgram, false, false)),
            Borsh.encodeToByteArray(AnchorInstructionSerializer("thaw_nft"), Args_thawNft()))

    fun unlockFunds(
        candyMachine: PublicKey,
        authority: PublicKey,
        freezePda: PublicKey,
        systemProgram: PublicKey
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("cndy3Z4yapfJBmL3ShUp5exZKqR3z33thTzeNMm2gRZ"),
            listOf(AccountMeta(candyMachine, false, true), AccountMeta(authority, true, true),
            AccountMeta(freezePda, false, true), AccountMeta(systemProgram, false, false)),
            Borsh.encodeToByteArray(AnchorInstructionSerializer("unlock_funds"),
            Args_unlockFunds()))

    @Serializable
    class Args_initializeCandyMachine(val data: CandyMachineData)

    @Serializable
    class Args_updateCandyMachine(val data: CandyMachineData)

    @Serializable
    class Args_updateAuthority(@Serializable(with = PublicKeyAs32ByteSerializer::class) val
            newAuthority: PublicKey?)

    @Serializable
    class Args_addConfigLines(val index: UInt, val configLines: List<ConfigLine>)

    @Serializable
    class Args_setCollection()

    @Serializable
    class Args_removeCollection()

    @Serializable
    class Args_mintNft(val creatorBump: UByte)

    @Serializable
    class Args_setCollectionDuringMint()

    @Serializable
    class Args_withdrawFunds()

    @Serializable
    class Args_setFreeze(val freezeTime: Long)

    @Serializable
    class Args_removeFreeze()

    @Serializable
    class Args_thawNft()

    @Serializable
    class Args_unlockFunds()
}
