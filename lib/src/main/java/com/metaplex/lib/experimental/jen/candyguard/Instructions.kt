//
// Instructions
// Metaplex
//
// This code was generated locally by Funkatronics on 2022-09-28
//
package com.metaplex.lib.experimental.jen.candyguard

import com.metaplex.lib.serialization.format.Borsh
import com.metaplex.lib.serialization.serializers.solana.AnchorInstructionSerializer
import com.solana.core.AccountMeta
import com.solana.core.PublicKey
import com.solana.core.TransactionInstruction
import kotlin.ByteArray
import kotlin.String
import kotlinx.serialization.Serializable

object CandyGuardInstructions {
    fun initialize(
        candyGuard: PublicKey,
        base: PublicKey,
        authority: PublicKey,
        payer: PublicKey,
        systemProgram: PublicKey,
        data: CandyGuardData
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("CnDYGRdU51FsSyLnVgSd19MCFxA4YHT5h3nacvCKMPUJ"),
            listOf(AccountMeta(candyGuard, false, true), AccountMeta(base, true, true),
            AccountMeta(authority, false, false), AccountMeta(payer, true, true),
            AccountMeta(systemProgram, false, false)),
            Borsh.encodeToByteArray(AnchorInstructionSerializer("initialize"),
            Args_initialize(data)))

    fun mint(
        candyGuard: PublicKey,
        candyMachineProgram: PublicKey,
        candyMachine: PublicKey,
        candyMachineAuthorityPda: PublicKey,
        payer: PublicKey,
        nftMetadata: PublicKey,
        nftMint: PublicKey,
        nftMintAuthority: PublicKey,
        nftMasterEdition: PublicKey,
        collectionAuthorityRecord: PublicKey,
        collectionMint: PublicKey,
        collectionMetadata: PublicKey,
        collectionMasterEdition: PublicKey,
        collectionUpdateAuthority: PublicKey,
        tokenMetadataProgram: PublicKey,
        tokenProgram: PublicKey,
        systemProgram: PublicKey,
        rent: PublicKey,
        recentSlothashes: PublicKey,
        instructionSysvarAccount: PublicKey,
        mintArgs: ByteArray,
        label: String?
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("CnDYGRdU51FsSyLnVgSd19MCFxA4YHT5h3nacvCKMPUJ"),
            listOf(AccountMeta(candyGuard, false, false), AccountMeta(candyMachineProgram, false,
            false), AccountMeta(candyMachine, false, true), AccountMeta(candyMachineAuthorityPda,
            false, true), AccountMeta(payer, true, true), AccountMeta(nftMetadata, false, true),
            AccountMeta(nftMint, false, true), AccountMeta(nftMintAuthority, true, false),
            AccountMeta(nftMasterEdition, false, true), AccountMeta(collectionAuthorityRecord,
            false, false), AccountMeta(collectionMint, false, false),
            AccountMeta(collectionMetadata, false, true), AccountMeta(collectionMasterEdition,
            false, false), AccountMeta(collectionUpdateAuthority, false, false),
            AccountMeta(tokenMetadataProgram, false, false), AccountMeta(tokenProgram, false,
            false), AccountMeta(systemProgram, false, false), AccountMeta(rent, false, false),
            AccountMeta(recentSlothashes, false, false), AccountMeta(instructionSysvarAccount,
            false, false)), Borsh.encodeToByteArray(AnchorInstructionSerializer("mint"),
            Args_mint(mintArgs, label)))

    fun unwrap(
        candyGuard: PublicKey,
        authority: PublicKey,
        candyMachine: PublicKey,
        candyMachineProgram: PublicKey,
        candyMachineAuthority: PublicKey
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("CnDYGRdU51FsSyLnVgSd19MCFxA4YHT5h3nacvCKMPUJ"),
            listOf(AccountMeta(candyGuard, false, false), AccountMeta(authority, true, false),
            AccountMeta(candyMachine, false, true), AccountMeta(candyMachineProgram, false, false),
            AccountMeta(candyMachineAuthority, true, false)),
            Borsh.encodeToByteArray(AnchorInstructionSerializer("unwrap"), Args_unwrap()))

    fun update(
        candyGuard: PublicKey,
        authority: PublicKey,
        payer: PublicKey,
        systemProgram: PublicKey,
        data: CandyGuardData
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("CnDYGRdU51FsSyLnVgSd19MCFxA4YHT5h3nacvCKMPUJ"),
            listOf(AccountMeta(candyGuard, false, true), AccountMeta(authority, true, false),
            AccountMeta(payer, true, false), AccountMeta(systemProgram, false, false)),
            Borsh.encodeToByteArray(AnchorInstructionSerializer("update"), Args_update(data)))

    fun withdraw(candyGuard: PublicKey, authority: PublicKey): TransactionInstruction =
            TransactionInstruction(PublicKey("CnDYGRdU51FsSyLnVgSd19MCFxA4YHT5h3nacvCKMPUJ"),
            listOf(AccountMeta(candyGuard, false, true), AccountMeta(authority, true, true)),
            Borsh.encodeToByteArray(AnchorInstructionSerializer("withdraw"), Args_withdraw()))

    fun wrap(
        candyGuard: PublicKey,
        authority: PublicKey,
        candyMachine: PublicKey,
        candyMachineProgram: PublicKey,
        candyMachineAuthority: PublicKey
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("CnDYGRdU51FsSyLnVgSd19MCFxA4YHT5h3nacvCKMPUJ"),
            listOf(AccountMeta(candyGuard, false, false), AccountMeta(authority, true, false),
            AccountMeta(candyMachine, false, true), AccountMeta(candyMachineProgram, false, false),
            AccountMeta(candyMachineAuthority, true, false)),
            Borsh.encodeToByteArray(AnchorInstructionSerializer("wrap"), Args_wrap()))

    @Serializable
    class Args_initialize(val data: CandyGuardData)

    @Serializable
    class Args_mint(val mintArgs: ByteArray, val label: String?)

    @Serializable
    class Args_unwrap()

    @Serializable
    class Args_update(val data: CandyGuardData)

    @Serializable
    class Args_withdraw()

    @Serializable
    class Args_wrap()
}