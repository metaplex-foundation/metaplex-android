/*
 * Pdas
 * Metaplex
 * 
 * Created by Funkatronics on 9/21/2022
 */

package com.metaplex.lib.programs.token_metadata

import com.solana.core.PublicKey

fun collectionAuthorityRecordPda(mint: PublicKey, authority: PublicKey,
                                 programId: PublicKey = TokenMetadataProgram.publicKey) =
    PublicKey.findProgramAddress(
        listOf(
            "metadata".toByteArray(Charsets.UTF_8), programId.toByteArray(), mint.toByteArray(),
            "collection_authority".toByteArray(Charsets.UTF_8), authority.toByteArray()
        ), programId
    )