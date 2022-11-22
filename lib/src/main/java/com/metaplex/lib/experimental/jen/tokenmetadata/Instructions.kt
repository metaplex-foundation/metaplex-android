//
// Instructions
// Metaplex
//
// This code was generated locally by Funkatronics on 2022-10-03
//
package com.metaplex.lib.experimental.jen.tokenmetadata

import com.metaplex.kborsh.Borsh
import com.metaplex.lib.serialization.serializers.solana.ByteDiscriminatorSerializer
import com.solana.core.AccountMeta
import com.solana.core.PublicKey
import com.solana.core.TransactionInstruction
import kotlinx.serialization.Serializable

object TokenMetadataInstructions {
    fun CreateMetadataAccount(
        metadata: PublicKey,
        mint: PublicKey,
        mintAuthority: PublicKey,
        payer: PublicKey,
        updateAuthority: PublicKey,
        systemProgram: PublicKey,
        rent: PublicKey,
        createMetadataAccountArgs: CreateMetadataAccountArgs
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(metadata, false, true), AccountMeta(mint, false, false),
            AccountMeta(mintAuthority, true, false), AccountMeta(payer, true, true),
            AccountMeta(updateAuthority, false, false), AccountMeta(systemProgram, false, false),
            AccountMeta(rent, false, false)),
            Borsh.encodeToByteArray(ByteDiscriminatorSerializer(0),
            Args_CreateMetadataAccount(createMetadataAccountArgs)))

    fun UpdateMetadataAccount(
        metadata: PublicKey,
        updateAuthority: PublicKey,
        updateMetadataAccountArgs: UpdateMetadataAccountArgs
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(metadata, false, true), AccountMeta(updateAuthority, true, false)),
            Borsh.encodeToByteArray(ByteDiscriminatorSerializer(1),
            Args_UpdateMetadataAccount(updateMetadataAccountArgs)))

    fun DeprecatedCreateMasterEdition(
        edition: PublicKey,
        mint: PublicKey,
        printingMint: PublicKey,
        oneTimePrintingAuthorizationMint: PublicKey,
        updateAuthority: PublicKey,
        printingMintAuthority: PublicKey,
        mintAuthority: PublicKey,
        metadata: PublicKey,
        payer: PublicKey,
        tokenProgram: PublicKey,
        systemProgram: PublicKey,
        rent: PublicKey,
        oneTimePrintingAuthorizationMintAuthority: PublicKey,
        createMasterEditionArgs: CreateMasterEditionArgs
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(edition, false, true), AccountMeta(mint, false, true),
            AccountMeta(printingMint, false, true), AccountMeta(oneTimePrintingAuthorizationMint,
            false, true), AccountMeta(updateAuthority, true, false),
            AccountMeta(printingMintAuthority, true, false), AccountMeta(mintAuthority, true,
            false), AccountMeta(metadata, false, false), AccountMeta(payer, true, false),
            AccountMeta(tokenProgram, false, false), AccountMeta(systemProgram, false, false),
            AccountMeta(rent, false, false), AccountMeta(oneTimePrintingAuthorizationMintAuthority,
            true, false)), Borsh.encodeToByteArray(ByteDiscriminatorSerializer(2),
            Args_DeprecatedCreateMasterEdition(createMasterEditionArgs)))

    fun DeprecatedMintNewEditionFromMasterEditionViaPrintingToken(
        metadata: PublicKey,
        edition: PublicKey,
        masterEdition: PublicKey,
        mint: PublicKey,
        mintAuthority: PublicKey,
        printingMint: PublicKey,
        masterTokenAccount: PublicKey,
        editionMarker: PublicKey,
        burnAuthority: PublicKey,
        payer: PublicKey,
        masterUpdateAuthority: PublicKey,
        masterMetadata: PublicKey,
        tokenProgram: PublicKey,
        systemProgram: PublicKey,
        rent: PublicKey,
        reservationList: PublicKey
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(metadata, false, true), AccountMeta(edition, false, true),
            AccountMeta(masterEdition, false, true), AccountMeta(mint, false, true),
            AccountMeta(mintAuthority, true, false), AccountMeta(printingMint, false, true),
            AccountMeta(masterTokenAccount, false, true), AccountMeta(editionMarker, false, true),
            AccountMeta(burnAuthority, true, false), AccountMeta(payer, true, false),
            AccountMeta(masterUpdateAuthority, false, false), AccountMeta(masterMetadata, false,
            false), AccountMeta(tokenProgram, false, false), AccountMeta(systemProgram, false,
            false), AccountMeta(rent, false, false), AccountMeta(reservationList, false, true)),
            Borsh.encodeToByteArray(ByteDiscriminatorSerializer(3),
            Args_DeprecatedMintNewEditionFromMasterEditionViaPrintingToken()))

    fun UpdatePrimarySaleHappenedViaToken(
        metadata: PublicKey,
        owner: PublicKey,
        token: PublicKey
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(metadata, false, true), AccountMeta(owner, true, false),
            AccountMeta(token, false, false)),
            Borsh.encodeToByteArray(ByteDiscriminatorSerializer(4),
            Args_UpdatePrimarySaleHappenedViaToken()))

    fun DeprecatedSetReservationList(
        masterEdition: PublicKey,
        reservationList: PublicKey,
        resource: PublicKey,
        setReservationListArgs: SetReservationListArgs
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(masterEdition, false, true), AccountMeta(reservationList, false,
            true), AccountMeta(resource, true, false)),
            Borsh.encodeToByteArray(ByteDiscriminatorSerializer(5),
            Args_DeprecatedSetReservationList(setReservationListArgs)))

    fun DeprecatedCreateReservationList(
        reservationList: PublicKey,
        payer: PublicKey,
        updateAuthority: PublicKey,
        masterEdition: PublicKey,
        resource: PublicKey,
        metadata: PublicKey,
        systemProgram: PublicKey,
        rent: PublicKey
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(reservationList, false, true), AccountMeta(payer, true, false),
            AccountMeta(updateAuthority, true, false), AccountMeta(masterEdition, false, false),
            AccountMeta(resource, false, false), AccountMeta(metadata, false, false),
            AccountMeta(systemProgram, false, false), AccountMeta(rent, false, false)),
            Borsh.encodeToByteArray(ByteDiscriminatorSerializer(6),
            Args_DeprecatedCreateReservationList()))

    fun SignMetadata(metadata: PublicKey, creator: PublicKey): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(metadata, false, true), AccountMeta(creator, true, false)),
            Borsh.encodeToByteArray(ByteDiscriminatorSerializer(7), Args_SignMetadata()))

    fun DeprecatedMintPrintingTokensViaToken(
        destination: PublicKey,
        token: PublicKey,
        oneTimePrintingAuthorizationMint: PublicKey,
        printingMint: PublicKey,
        burnAuthority: PublicKey,
        metadata: PublicKey,
        masterEdition: PublicKey,
        tokenProgram: PublicKey,
        rent: PublicKey,
        mintPrintingTokensViaTokenArgs: MintPrintingTokensViaTokenArgs
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(destination, false, true), AccountMeta(token, false, true),
            AccountMeta(oneTimePrintingAuthorizationMint, false, true), AccountMeta(printingMint,
            false, true), AccountMeta(burnAuthority, true, false), AccountMeta(metadata, false,
            false), AccountMeta(masterEdition, false, false), AccountMeta(tokenProgram, false,
            false), AccountMeta(rent, false, false)),
            Borsh.encodeToByteArray(ByteDiscriminatorSerializer(8),
            Args_DeprecatedMintPrintingTokensViaToken(mintPrintingTokensViaTokenArgs)))

    fun DeprecatedMintPrintingTokens(
        destination: PublicKey,
        printingMint: PublicKey,
        updateAuthority: PublicKey,
        metadata: PublicKey,
        masterEdition: PublicKey,
        tokenProgram: PublicKey,
        rent: PublicKey,
        mintPrintingTokensViaTokenArgs: MintPrintingTokensViaTokenArgs
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(destination, false, true), AccountMeta(printingMint, false, true),
            AccountMeta(updateAuthority, true, false), AccountMeta(metadata, false, false),
            AccountMeta(masterEdition, false, false), AccountMeta(tokenProgram, false, false),
            AccountMeta(rent, false, false)),
            Borsh.encodeToByteArray(ByteDiscriminatorSerializer(9),
            Args_DeprecatedMintPrintingTokens(mintPrintingTokensViaTokenArgs)))

    fun CreateMasterEdition(
        edition: PublicKey,
        mint: PublicKey,
        updateAuthority: PublicKey,
        mintAuthority: PublicKey,
        payer: PublicKey,
        metadata: PublicKey,
        tokenProgram: PublicKey,
        systemProgram: PublicKey,
        rent: PublicKey,
        createMasterEditionArgs: CreateMasterEditionArgs
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(edition, false, true), AccountMeta(mint, false, true),
            AccountMeta(updateAuthority, true, false), AccountMeta(mintAuthority, true, false),
            AccountMeta(payer, true, true), AccountMeta(metadata, false, false),
            AccountMeta(tokenProgram, false, false), AccountMeta(systemProgram, false, false),
            AccountMeta(rent, false, false)),
            Borsh.encodeToByteArray(ByteDiscriminatorSerializer(10),
            Args_CreateMasterEdition(createMasterEditionArgs)))

    fun MintNewEditionFromMasterEditionViaToken(
        newMetadata: PublicKey,
        newEdition: PublicKey,
        masterEdition: PublicKey,
        newMint: PublicKey,
        editionMarkPda: PublicKey,
        newMintAuthority: PublicKey,
        payer: PublicKey,
        tokenAccountOwner: PublicKey,
        tokenAccount: PublicKey,
        newMetadataUpdateAuthority: PublicKey,
        metadata: PublicKey,
        tokenProgram: PublicKey,
        systemProgram: PublicKey,
        rent: PublicKey,
        mintNewEditionFromMasterEditionViaTokenArgs: MintNewEditionFromMasterEditionViaTokenArgs
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(newMetadata, false, true), AccountMeta(newEdition, false, true),
            AccountMeta(masterEdition, false, true), AccountMeta(newMint, false, true),
            AccountMeta(editionMarkPda, false, true), AccountMeta(newMintAuthority, true, false),
            AccountMeta(payer, true, true), AccountMeta(tokenAccountOwner, true, false),
            AccountMeta(tokenAccount, false, false), AccountMeta(newMetadataUpdateAuthority, false,
            false), AccountMeta(metadata, false, false), AccountMeta(tokenProgram, false, false),
            AccountMeta(systemProgram, false, false), AccountMeta(rent, false, false)),
            Borsh.encodeToByteArray(ByteDiscriminatorSerializer(11),
            Args_MintNewEditionFromMasterEditionViaToken(mintNewEditionFromMasterEditionViaTokenArgs)))

    fun ConvertMasterEditionV1ToV2(
        masterEdition: PublicKey,
        oneTimeAuth: PublicKey,
        printingMint: PublicKey
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(masterEdition, false, true), AccountMeta(oneTimeAuth, false, true),
            AccountMeta(printingMint, false, true)),
            Borsh.encodeToByteArray(ByteDiscriminatorSerializer(12),
            Args_ConvertMasterEditionV1ToV2()))

    fun MintNewEditionFromMasterEditionViaVaultProxy(
        newMetadata: PublicKey,
        newEdition: PublicKey,
        masterEdition: PublicKey,
        newMint: PublicKey,
        editionMarkPda: PublicKey,
        newMintAuthority: PublicKey,
        payer: PublicKey,
        vaultAuthority: PublicKey,
        safetyDepositStore: PublicKey,
        safetyDepositBox: PublicKey,
        vault: PublicKey,
        newMetadataUpdateAuthority: PublicKey,
        metadata: PublicKey,
        tokenProgram: PublicKey,
        tokenVaultProgram: PublicKey,
        systemProgram: PublicKey,
        rent: PublicKey,
        mintNewEditionFromMasterEditionViaTokenArgs: MintNewEditionFromMasterEditionViaTokenArgs
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(newMetadata, false, true), AccountMeta(newEdition, false, true),
            AccountMeta(masterEdition, false, true), AccountMeta(newMint, false, true),
            AccountMeta(editionMarkPda, false, true), AccountMeta(newMintAuthority, true, false),
            AccountMeta(payer, true, true), AccountMeta(vaultAuthority, true, false),
            AccountMeta(safetyDepositStore, false, false), AccountMeta(safetyDepositBox, false,
            false), AccountMeta(vault, false, false), AccountMeta(newMetadataUpdateAuthority, false,
            false), AccountMeta(metadata, false, false), AccountMeta(tokenProgram, false, false),
            AccountMeta(tokenVaultProgram, false, false), AccountMeta(systemProgram, false, false),
            AccountMeta(rent, false, false)),
            Borsh.encodeToByteArray(ByteDiscriminatorSerializer(13),
            Args_MintNewEditionFromMasterEditionViaVaultProxy(mintNewEditionFromMasterEditionViaTokenArgs)))

    fun PuffMetadata(metadata: PublicKey): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(metadata, false, true)),
            Borsh.encodeToByteArray(ByteDiscriminatorSerializer(14), Args_PuffMetadata()))

    fun UpdateMetadataAccountV2(
        metadata: PublicKey,
        updateAuthority: PublicKey,
        updateMetadataAccountArgsV2: UpdateMetadataAccountArgsV2
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(metadata, false, true), AccountMeta(updateAuthority, true, false)),
            Borsh.encodeToByteArray(ByteDiscriminatorSerializer(15),
            Args_UpdateMetadataAccountV2(updateMetadataAccountArgsV2)))

    fun CreateMetadataAccountV2(
        metadata: PublicKey,
        mint: PublicKey,
        mintAuthority: PublicKey,
        payer: PublicKey,
        updateAuthority: PublicKey,
        systemProgram: PublicKey,
        rent: PublicKey,
        createMetadataAccountArgsV2: CreateMetadataAccountArgsV2
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(metadata, false, true), AccountMeta(mint, false, false),
            AccountMeta(mintAuthority, true, false), AccountMeta(payer, true, true),
            AccountMeta(updateAuthority, false, false), AccountMeta(systemProgram, false, false),
            AccountMeta(rent, false, false)),
            Borsh.encodeToByteArray(ByteDiscriminatorSerializer(16),
            Args_CreateMetadataAccountV2(createMetadataAccountArgsV2)))

    fun CreateMasterEditionV3(
        edition: PublicKey,
        mint: PublicKey,
        updateAuthority: PublicKey,
        mintAuthority: PublicKey,
        payer: PublicKey,
        metadata: PublicKey,
        tokenProgram: PublicKey,
        systemProgram: PublicKey,
        rent: PublicKey,
        createMasterEditionArgs: CreateMasterEditionArgs
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(edition, false, true), AccountMeta(mint, false, true),
            AccountMeta(updateAuthority, true, false), AccountMeta(mintAuthority, true, false),
            AccountMeta(payer, true, true), AccountMeta(metadata, false, true),
            AccountMeta(tokenProgram, false, false), AccountMeta(systemProgram, false, false),
            AccountMeta(rent, false, false)),
            Borsh.encodeToByteArray(ByteDiscriminatorSerializer(17),
            Args_CreateMasterEditionV3(createMasterEditionArgs)))

    fun VerifyCollection(
        metadata: PublicKey,
        collectionAuthority: PublicKey,
        payer: PublicKey,
        collectionMint: PublicKey,
        collection: PublicKey,
        collectionMasterEditionAccount: PublicKey
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(metadata, false, true), AccountMeta(collectionAuthority, true, true),
            AccountMeta(payer, true, true), AccountMeta(collectionMint, false, false),
            AccountMeta(collection, false, false), AccountMeta(collectionMasterEditionAccount,
            false, false)), Borsh.encodeToByteArray(ByteDiscriminatorSerializer(18),
            Args_VerifyCollection()))

    fun Utilize(
        metadata: PublicKey,
        tokenAccount: PublicKey,
        mint: PublicKey,
        useAuthority: PublicKey,
        owner: PublicKey,
        tokenProgram: PublicKey,
        ataProgram: PublicKey,
        systemProgram: PublicKey,
        rent: PublicKey,
        useAuthorityRecord: PublicKey,
        burner: PublicKey,
        utilizeArgs: UtilizeArgs
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(metadata, false, true), AccountMeta(tokenAccount, false, true),
            AccountMeta(mint, false, true), AccountMeta(useAuthority, true, true),
            AccountMeta(owner, false, false), AccountMeta(tokenProgram, false, false),
            AccountMeta(ataProgram, false, false), AccountMeta(systemProgram, false, false),
            AccountMeta(rent, false, false), AccountMeta(useAuthorityRecord, false, true),
            AccountMeta(burner, false, false)),
            Borsh.encodeToByteArray(ByteDiscriminatorSerializer(19), Args_Utilize(utilizeArgs)))

    fun ApproveUseAuthority(
        useAuthorityRecord: PublicKey,
        owner: PublicKey,
        payer: PublicKey,
        user: PublicKey,
        ownerTokenAccount: PublicKey,
        metadata: PublicKey,
        mint: PublicKey,
        burner: PublicKey,
        tokenProgram: PublicKey,
        systemProgram: PublicKey,
        rent: PublicKey,
        approveUseAuthorityArgs: ApproveUseAuthorityArgs
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(useAuthorityRecord, false, true), AccountMeta(owner, true, true),
            AccountMeta(payer, true, true), AccountMeta(user, false, false),
            AccountMeta(ownerTokenAccount, false, true), AccountMeta(metadata, false, false),
            AccountMeta(mint, false, false), AccountMeta(burner, false, false),
            AccountMeta(tokenProgram, false, false), AccountMeta(systemProgram, false, false),
            AccountMeta(rent, false, false)),
            Borsh.encodeToByteArray(ByteDiscriminatorSerializer(20),
            Args_ApproveUseAuthority(approveUseAuthorityArgs)))

    fun RevokeUseAuthority(
        useAuthorityRecord: PublicKey,
        owner: PublicKey,
        user: PublicKey,
        ownerTokenAccount: PublicKey,
        mint: PublicKey,
        metadata: PublicKey,
        tokenProgram: PublicKey,
        systemProgram: PublicKey,
        rent: PublicKey
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(useAuthorityRecord, false, true), AccountMeta(owner, true, true),
            AccountMeta(user, false, false), AccountMeta(ownerTokenAccount, false, true),
            AccountMeta(mint, false, false), AccountMeta(metadata, false, false),
            AccountMeta(tokenProgram, false, false), AccountMeta(systemProgram, false, false),
            AccountMeta(rent, false, false)),
            Borsh.encodeToByteArray(ByteDiscriminatorSerializer(21), Args_RevokeUseAuthority()))

    fun UnverifyCollection(
        metadata: PublicKey,
        collectionAuthority: PublicKey,
        collectionMint: PublicKey,
        collection: PublicKey,
        collectionMasterEditionAccount: PublicKey,
        collectionAuthorityRecord: PublicKey
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(metadata, false, true), AccountMeta(collectionAuthority, true, true),
            AccountMeta(collectionMint, false, false), AccountMeta(collection, false, false),
            AccountMeta(collectionMasterEditionAccount, false, false),
            AccountMeta(collectionAuthorityRecord, false, false)),
            Borsh.encodeToByteArray(ByteDiscriminatorSerializer(22), Args_UnverifyCollection()))

    fun ApproveCollectionAuthority(
        collectionAuthorityRecord: PublicKey,
        newCollectionAuthority: PublicKey,
        updateAuthority: PublicKey,
        payer: PublicKey,
        metadata: PublicKey,
        mint: PublicKey,
        systemProgram: PublicKey,
        rent: PublicKey
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(collectionAuthorityRecord, false, true),
            AccountMeta(newCollectionAuthority, false, false), AccountMeta(updateAuthority, true,
            true), AccountMeta(payer, true, true), AccountMeta(metadata, false, false),
            AccountMeta(mint, false, false), AccountMeta(systemProgram, false, false),
            AccountMeta(rent, false, false)),
            Borsh.encodeToByteArray(ByteDiscriminatorSerializer(23),
            Args_ApproveCollectionAuthority()))

    fun RevokeCollectionAuthority(
        collectionAuthorityRecord: PublicKey,
        delegateAuthority: PublicKey,
        revokeAuthority: PublicKey,
        metadata: PublicKey,
        mint: PublicKey
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(collectionAuthorityRecord, false, true),
            AccountMeta(delegateAuthority, false, true), AccountMeta(revokeAuthority, true, true),
            AccountMeta(metadata, false, false), AccountMeta(mint, false, false)),
            Borsh.encodeToByteArray(ByteDiscriminatorSerializer(24),
            Args_RevokeCollectionAuthority()))

    fun SetAndVerifyCollection(
        metadata: PublicKey,
        collectionAuthority: PublicKey,
        payer: PublicKey,
        updateAuthority: PublicKey,
        collectionMint: PublicKey,
        collection: PublicKey,
        collectionMasterEditionAccount: PublicKey,
        collectionAuthorityRecord: PublicKey
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(metadata, false, true), AccountMeta(collectionAuthority, true, true),
            AccountMeta(payer, true, true), AccountMeta(updateAuthority, false, false),
            AccountMeta(collectionMint, false, false), AccountMeta(collection, false, false),
            AccountMeta(collectionMasterEditionAccount, false, false),
            AccountMeta(collectionAuthorityRecord, false, false)),
            Borsh.encodeToByteArray(ByteDiscriminatorSerializer(25), Args_SetAndVerifyCollection()))

    fun FreezeDelegatedAccount(
        delegate: PublicKey,
        tokenAccount: PublicKey,
        edition: PublicKey,
        mint: PublicKey,
        tokenProgram: PublicKey
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(delegate, true, true), AccountMeta(tokenAccount, false, true),
            AccountMeta(edition, false, false), AccountMeta(mint, false, false),
            AccountMeta(tokenProgram, false, false)),
            Borsh.encodeToByteArray(ByteDiscriminatorSerializer(26), Args_FreezeDelegatedAccount()))

    fun ThawDelegatedAccount(
        delegate: PublicKey,
        tokenAccount: PublicKey,
        edition: PublicKey,
        mint: PublicKey,
        tokenProgram: PublicKey
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(delegate, true, true), AccountMeta(tokenAccount, false, true),
            AccountMeta(edition, false, false), AccountMeta(mint, false, false),
            AccountMeta(tokenProgram, false, false)),
            Borsh.encodeToByteArray(ByteDiscriminatorSerializer(27), Args_ThawDelegatedAccount()))

    fun RemoveCreatorVerification(metadata: PublicKey, creator: PublicKey): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(metadata, false, true), AccountMeta(creator, true, false)),
            Borsh.encodeToByteArray(ByteDiscriminatorSerializer(28),
            Args_RemoveCreatorVerification()))

    fun BurnNft(
        metadata: PublicKey,
        owner: PublicKey,
        mint: PublicKey,
        tokenAccount: PublicKey,
        masterEditionAccount: PublicKey,
        splTokenProgram: PublicKey,
        collectionMetadata: PublicKey
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(metadata, false, true), AccountMeta(owner, true, true),
            AccountMeta(mint, false, true), AccountMeta(tokenAccount, false, true),
            AccountMeta(masterEditionAccount, false, true), AccountMeta(splTokenProgram, false,
            false), AccountMeta(collectionMetadata, false, true)),
            Borsh.encodeToByteArray(ByteDiscriminatorSerializer(29), Args_BurnNft()))

    fun VerifySizedCollectionItem(
        metadata: PublicKey,
        collectionAuthority: PublicKey,
        payer: PublicKey,
        collectionMint: PublicKey,
        collection: PublicKey,
        collectionMasterEditionAccount: PublicKey,
        collectionAuthorityRecord: PublicKey
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(metadata, false, true), AccountMeta(collectionAuthority, true,
            false), AccountMeta(payer, true, true), AccountMeta(collectionMint, false, false),
            AccountMeta(collection, false, true), AccountMeta(collectionMasterEditionAccount, false,
            false), AccountMeta(collectionAuthorityRecord, false, false)),
            Borsh.encodeToByteArray(ByteDiscriminatorSerializer(30),
            Args_VerifySizedCollectionItem()))

    fun UnverifySizedCollectionItem(
        metadata: PublicKey,
        collectionAuthority: PublicKey,
        payer: PublicKey,
        collectionMint: PublicKey,
        collection: PublicKey,
        collectionMasterEditionAccount: PublicKey,
        collectionAuthorityRecord: PublicKey
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(metadata, false, true), AccountMeta(collectionAuthority, true,
            false), AccountMeta(payer, true, true), AccountMeta(collectionMint, false, false),
            AccountMeta(collection, false, true), AccountMeta(collectionMasterEditionAccount, false,
            false), AccountMeta(collectionAuthorityRecord, false, false)),
            Borsh.encodeToByteArray(ByteDiscriminatorSerializer(31),
            Args_UnverifySizedCollectionItem()))

    fun SetAndVerifySizedCollectionItem(
        metadata: PublicKey,
        collectionAuthority: PublicKey,
        payer: PublicKey,
        updateAuthority: PublicKey,
        collectionMint: PublicKey,
        collection: PublicKey,
        collectionMasterEditionAccount: PublicKey,
        collectionAuthorityRecord: PublicKey
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(metadata, false, true), AccountMeta(collectionAuthority, true,
            false), AccountMeta(payer, true, true), AccountMeta(updateAuthority, false, false),
            AccountMeta(collectionMint, false, false), AccountMeta(collection, false, true),
            AccountMeta(collectionMasterEditionAccount, false, true),
            AccountMeta(collectionAuthorityRecord, false, false)),
            Borsh.encodeToByteArray(ByteDiscriminatorSerializer(32),
            Args_SetAndVerifySizedCollectionItem()))

    fun CreateMetadataAccountV3(
        metadata: PublicKey,
        mint: PublicKey,
        mintAuthority: PublicKey,
        payer: PublicKey,
        updateAuthority: PublicKey,
        systemProgram: PublicKey,
        rent: PublicKey,
        createMetadataAccountArgsV3: CreateMetadataAccountArgsV3
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(metadata, false, true), AccountMeta(mint, false, false),
            AccountMeta(mintAuthority, true, false), AccountMeta(payer, true, true),
            AccountMeta(updateAuthority, false, false), AccountMeta(systemProgram, false, false),
            AccountMeta(rent, false, false)),
            Borsh.encodeToByteArray(ByteDiscriminatorSerializer(33),
            Args_CreateMetadataAccountV3(createMetadataAccountArgsV3)))

    fun SetCollectionSize(
        collectionMetadata: PublicKey,
        collectionAuthority: PublicKey,
        collectionMint: PublicKey,
        collectionAuthorityRecord: PublicKey,
        setCollectionSizeArgs: SetCollectionSizeArgs
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(collectionMetadata, false, true), AccountMeta(collectionAuthority,
            true, true), AccountMeta(collectionMint, false, false),
            AccountMeta(collectionAuthorityRecord, false, false)),
            Borsh.encodeToByteArray(ByteDiscriminatorSerializer(34),
            Args_SetCollectionSize(setCollectionSizeArgs)))

    fun SetTokenStandard(
        metadata: PublicKey,
        updateAuthority: PublicKey,
        mint: PublicKey,
        edition: PublicKey
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(metadata, false, true), AccountMeta(updateAuthority, true, true),
            AccountMeta(mint, false, false), AccountMeta(edition, false, false)),
            Borsh.encodeToByteArray(ByteDiscriminatorSerializer(35), Args_SetTokenStandard()))

    fun BubblegumSetCollectionSize(
        collectionMetadata: PublicKey,
        collectionAuthority: PublicKey,
        collectionMint: PublicKey,
        bubblegumSigner: PublicKey,
        collectionAuthorityRecord: PublicKey,
        setCollectionSizeArgs: SetCollectionSizeArgs
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(collectionMetadata, false, true), AccountMeta(collectionAuthority,
            true, true), AccountMeta(collectionMint, false, false), AccountMeta(bubblegumSigner,
            true, false), AccountMeta(collectionAuthorityRecord, false, false)),
            Borsh.encodeToByteArray(ByteDiscriminatorSerializer(36),
            Args_BubblegumSetCollectionSize(setCollectionSizeArgs)))

    fun BurnEditionNft(
        metadata: PublicKey,
        owner: PublicKey,
        printEditionMint: PublicKey,
        masterEditionMint: PublicKey,
        printEditionTokenAccount: PublicKey,
        masterEditionTokenAccount: PublicKey,
        masterEditionAccount: PublicKey,
        printEditionAccount: PublicKey,
        editionMarkerAccount: PublicKey,
        splTokenProgram: PublicKey
    ): TransactionInstruction =
            TransactionInstruction(PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"),
            listOf(AccountMeta(metadata, false, true), AccountMeta(owner, true, true),
            AccountMeta(printEditionMint, false, true), AccountMeta(masterEditionMint, false,
            false), AccountMeta(printEditionTokenAccount, false, true),
            AccountMeta(masterEditionTokenAccount, false, false), AccountMeta(masterEditionAccount,
            false, true), AccountMeta(printEditionAccount, false, true),
            AccountMeta(editionMarkerAccount, false, true), AccountMeta(splTokenProgram, false,
            false)), Borsh.encodeToByteArray(ByteDiscriminatorSerializer(37),
            Args_BurnEditionNft()))

    @Serializable
    class Args_CreateMetadataAccount(val createMetadataAccountArgs: CreateMetadataAccountArgs)

    @Serializable
    class Args_UpdateMetadataAccount(val updateMetadataAccountArgs: UpdateMetadataAccountArgs)

    @Serializable
    class Args_DeprecatedCreateMasterEdition(val createMasterEditionArgs: CreateMasterEditionArgs)

    @Serializable
    class Args_DeprecatedMintNewEditionFromMasterEditionViaPrintingToken()

    @Serializable
    class Args_UpdatePrimarySaleHappenedViaToken()

    @Serializable
    class Args_DeprecatedSetReservationList(val setReservationListArgs: SetReservationListArgs)

    @Serializable
    class Args_DeprecatedCreateReservationList()

    @Serializable
    class Args_SignMetadata()

    @Serializable
    class Args_DeprecatedMintPrintingTokensViaToken(val mintPrintingTokensViaTokenArgs:
            MintPrintingTokensViaTokenArgs)

    @Serializable
    class Args_DeprecatedMintPrintingTokens(val mintPrintingTokensViaTokenArgs:
            MintPrintingTokensViaTokenArgs)

    @Serializable
    class Args_CreateMasterEdition(val createMasterEditionArgs: CreateMasterEditionArgs)

    @Serializable
    class Args_MintNewEditionFromMasterEditionViaToken(val
            mintNewEditionFromMasterEditionViaTokenArgs:
            MintNewEditionFromMasterEditionViaTokenArgs)

    @Serializable
    class Args_ConvertMasterEditionV1ToV2()

    @Serializable
    class Args_MintNewEditionFromMasterEditionViaVaultProxy(val
            mintNewEditionFromMasterEditionViaTokenArgs:
            MintNewEditionFromMasterEditionViaTokenArgs)

    @Serializable
    class Args_PuffMetadata()

    @Serializable
    class Args_UpdateMetadataAccountV2(val updateMetadataAccountArgsV2: UpdateMetadataAccountArgsV2)

    @Serializable
    class Args_CreateMetadataAccountV2(val createMetadataAccountArgsV2: CreateMetadataAccountArgsV2)

    @Serializable
    class Args_CreateMasterEditionV3(val createMasterEditionArgs: CreateMasterEditionArgs)

    @Serializable
    class Args_VerifyCollection()

    @Serializable
    class Args_Utilize(val utilizeArgs: UtilizeArgs)

    @Serializable
    class Args_ApproveUseAuthority(val approveUseAuthorityArgs: ApproveUseAuthorityArgs)

    @Serializable
    class Args_RevokeUseAuthority()

    @Serializable
    class Args_UnverifyCollection()

    @Serializable
    class Args_ApproveCollectionAuthority()

    @Serializable
    class Args_RevokeCollectionAuthority()

    @Serializable
    class Args_SetAndVerifyCollection()

    @Serializable
    class Args_FreezeDelegatedAccount()

    @Serializable
    class Args_ThawDelegatedAccount()

    @Serializable
    class Args_RemoveCreatorVerification()

    @Serializable
    class Args_BurnNft()

    @Serializable
    class Args_VerifySizedCollectionItem()

    @Serializable
    class Args_UnverifySizedCollectionItem()

    @Serializable
    class Args_SetAndVerifySizedCollectionItem()

    @Serializable
    class Args_CreateMetadataAccountV3(val createMetadataAccountArgsV3: CreateMetadataAccountArgsV3)

    @Serializable
    class Args_SetCollectionSize(val setCollectionSizeArgs: SetCollectionSizeArgs)

    @Serializable
    class Args_SetTokenStandard()

    @Serializable
    class Args_BubblegumSetCollectionSize(val setCollectionSizeArgs: SetCollectionSizeArgs)

    @Serializable
    class Args_BurnEditionNft()
}
