//
// Instructions
// Metaplex
//
// This code was generated locally by Funkatronics on 2022-09-30
//
package com.metaplex.lib.experimental.jen.candymachine

import com.metaplex.kborsh.Borsh
import com.metaplex.lib.serialization.serializers.solana.AnchorInstructionSerializer
import com.metaplex.lib.serialization.serializers.solana.PublicKeyAs32ByteSerializer
import com.solana.core.AccountMeta
import com.solana.core.PublicKey
import com.solana.core.Sysvar
import com.solana.core.TransactionInstruction
import kotlinx.serialization.Serializable

object CandyMachineInstructions {
    fun addConfigLines(
        candyMachine: PublicKey,
        authority: PublicKey,
        index: UInt,
        configLines: List<ConfigLine>
    ): TransactionInstruction =
        TransactionInstruction(
            PublicKey("CndyV3LdqHUfDLmE5naZjVN8rBZz4tqhdefbAnjHG3JR"),
            listOf(AccountMeta(candyMachine, false, true),
                AccountMeta(authority, true, false)),
            Borsh.encodeToByteArray(
                AnchorInstructionSerializer("add_config_lines"),
                Args_addConfigLines(index, configLines)
            )
        )

    fun initialize(
        candyMachine: PublicKey,
        authorityPda: PublicKey,
        authority: PublicKey,
        payer: PublicKey,
        collectionMetadata: PublicKey,
        collectionMint: PublicKey,
        collectionMasterEdition: PublicKey,
        collectionUpdateAuthority: PublicKey,
        collectionAuthorityRecord: PublicKey,
        tokenMetadataProgram: PublicKey,
        systemProgram: PublicKey,
        data: CandyMachineData
    ): TransactionInstruction =
        TransactionInstruction(
            PublicKey("CndyV3LdqHUfDLmE5naZjVN8rBZz4tqhdefbAnjHG3JR"),
            listOf(
                AccountMeta(candyMachine, false, true),
                AccountMeta(authorityPda, false, true),
                AccountMeta(authority, false, false),
                AccountMeta(payer, true, false),
                AccountMeta(collectionMetadata, false, false),
                AccountMeta(collectionMint, false, false),
                AccountMeta(collectionMasterEdition, false, false),
                AccountMeta(collectionUpdateAuthority, true, true),
                AccountMeta(collectionAuthorityRecord, false, true),
                AccountMeta(tokenMetadataProgram, false, false),
                AccountMeta(systemProgram, false, false),
                AccountMeta(Sysvar.SYSVAR_RENT_PUBKEY, false, false)
            ),
            Borsh.encodeToByteArray(
                AnchorInstructionSerializer("initialize"),
                Args_initialize(data)
            )
        )

    fun mint(
        candyMachine: PublicKey,
        authorityPda: PublicKey,
        mintAuthority: PublicKey,
        payer: PublicKey,
        nftMint: PublicKey,
        nftMintAuthority: PublicKey,
        nftMetadata: PublicKey,
        nftMasterEdition: PublicKey,
        collectionAuthorityRecord: PublicKey,
        collectionMint: PublicKey,
        collectionMetadata: PublicKey,
        collectionMasterEdition: PublicKey,
        collectionUpdateAuthority: PublicKey,
        tokenMetadataProgram: PublicKey,
        tokenProgram: PublicKey,
        systemProgram: PublicKey,
        recentSlothashes: PublicKey
    ): TransactionInstruction =
        TransactionInstruction(
            PublicKey("CndyV3LdqHUfDLmE5naZjVN8rBZz4tqhdefbAnjHG3JR"),
            listOf(
                AccountMeta(candyMachine, false, true), AccountMeta(authorityPda, false, true),
                AccountMeta(mintAuthority, true, false), AccountMeta(payer, true, true),
                AccountMeta(nftMint, false, true), AccountMeta(nftMintAuthority, true, false),
                AccountMeta(nftMetadata, false, true), AccountMeta(nftMasterEdition, false, true),
                AccountMeta(collectionAuthorityRecord, false, false), AccountMeta(
                    collectionMint, false,
                    false
                ), AccountMeta(collectionMetadata, false, true),
                AccountMeta(collectionMasterEdition, false, false),
                AccountMeta(collectionUpdateAuthority, false, false), AccountMeta(
                    tokenMetadataProgram,
                    false, false
                ), AccountMeta(tokenProgram, false, false), AccountMeta(
                    systemProgram,
                    false, false
                ), AccountMeta(recentSlothashes, false, false)
            ),
            Borsh.encodeToByteArray(AnchorInstructionSerializer("mint"), Args_mint())
        )

    fun setAuthority(
        candyMachine: PublicKey,
        authority: PublicKey,
        newAuthority: PublicKey
    ): TransactionInstruction =
        TransactionInstruction(
            PublicKey("CndyV3LdqHUfDLmE5naZjVN8rBZz4tqhdefbAnjHG3JR"),
            listOf(AccountMeta(candyMachine, false, true), AccountMeta(authority, true, false)),
            Borsh.encodeToByteArray(
                AnchorInstructionSerializer("set_authority"),
                Args_setAuthority(newAuthority)
            )
        )

    fun setCollection(
        candyMachine: PublicKey,
        authority: PublicKey,
        authorityPda: PublicKey,
        payer: PublicKey,
        collectionMint: PublicKey,
        collectionMetadata: PublicKey,
        collectionAuthorityRecord: PublicKey,
        newCollectionUpdateAuthority: PublicKey,
        newCollectionMetadata: PublicKey,
        newCollectionMint: PublicKey,
        newCollectionMasterEdition: PublicKey,
        newCollectionAuthorityRecord: PublicKey,
        tokenMetadataProgram: PublicKey,
        systemProgram: PublicKey
    ): TransactionInstruction =
        TransactionInstruction(
            PublicKey("CndyV3LdqHUfDLmE5naZjVN8rBZz4tqhdefbAnjHG3JR"),
            listOf(
                AccountMeta(candyMachine, false, true), AccountMeta(authority, true, false),
                AccountMeta(authorityPda, false, true), AccountMeta(payer, true, false),
                AccountMeta(collectionMint, false, false), AccountMeta(
                    collectionMetadata, false,
                    false
                ), AccountMeta(collectionAuthorityRecord, false, true),
                AccountMeta(newCollectionUpdateAuthority, true, true),
                AccountMeta(newCollectionMetadata, false, false), AccountMeta(
                    newCollectionMint, false,
                    false
                ), AccountMeta(newCollectionMasterEdition, false, false),
                AccountMeta(newCollectionAuthorityRecord, false, true),
                AccountMeta(tokenMetadataProgram, false, false), AccountMeta(
                    systemProgram, false,
                    false
                )
            ), Borsh.encodeToByteArray(
                AnchorInstructionSerializer("set_collection"),
                Args_setCollection()
            )
        )

    fun setMintAuthority(
        candyMachine: PublicKey,
        authority: PublicKey,
        mintAuthority: PublicKey
    ): TransactionInstruction =
        TransactionInstruction(
            PublicKey("CndyV3LdqHUfDLmE5naZjVN8rBZz4tqhdefbAnjHG3JR"),
            listOf(
                AccountMeta(candyMachine, false, true), AccountMeta(authority, true, false),
                AccountMeta(mintAuthority, true, false)
            ),
            Borsh.encodeToByteArray(
                AnchorInstructionSerializer("set_mint_authority"),
                Args_setMintAuthority()
            )
        )

    fun update(
        candyMachine: PublicKey,
        authority: PublicKey,
        data: CandyMachineData
    ): TransactionInstruction =
        TransactionInstruction(
            PublicKey("CndyV3LdqHUfDLmE5naZjVN8rBZz4tqhdefbAnjHG3JR"),
            listOf(AccountMeta(candyMachine, false, true), AccountMeta(authority, true, false)),
            Borsh.encodeToByteArray(AnchorInstructionSerializer("update"), Args_update(data))
        )

    fun withdraw(candyMachine: PublicKey, authority: PublicKey): TransactionInstruction =
        TransactionInstruction(
            PublicKey("CndyV3LdqHUfDLmE5naZjVN8rBZz4tqhdefbAnjHG3JR"),
            listOf(AccountMeta(candyMachine, false, true), AccountMeta(authority, true, true)),
            Borsh.encodeToByteArray(AnchorInstructionSerializer("withdraw"), Args_withdraw())
        )

    @Serializable
    class Args_addConfigLines(val index: UInt, val configLines: List<ConfigLine>)

    @Serializable
    class Args_initialize(val data: CandyMachineData)

    @Serializable
    class Args_mint()

    @Serializable
    class Args_setAuthority(
        @Serializable(with = PublicKeyAs32ByteSerializer::class) val
        newAuthority: PublicKey
    )

    @Serializable
    class Args_setCollection()

    @Serializable
    class Args_setMintAuthority()

    @Serializable
    class Args_update(val data: CandyMachineData)

    @Serializable
    class Args_withdraw()
}
