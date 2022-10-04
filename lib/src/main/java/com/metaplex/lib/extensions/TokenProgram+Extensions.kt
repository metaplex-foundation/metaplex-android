/*
 * TokenProgram+Extensions
 * Metaplex
 * 
 * Created by Funkatronics on 9/26/2022
 */

package com.metaplex.lib.extensions

import com.solana.core.AccountMeta
import com.solana.core.PublicKey
import com.solana.core.TransactionInstruction
import com.solana.programs.TokenProgram
import java.nio.ByteBuffer
import java.nio.ByteOrder

// should add this to SolanaKT, or refactor it to metaplex.TokenProgram
val TokenProgram.INITIALIZE_MINT_ID get() = 0
val TokenProgram.MINT_TO get() = 7

fun TokenProgram.initializeMint(
    mint: PublicKey, // Token mint account
    decimals: Byte, // Number of decimals in token account amounts
    mintAuthority: PublicKey, // Minting authority
    freezeAuthority: PublicKey? = null // Optional authority that can freeze token accounts
): TransactionInstruction {
    val keys = listOf(
        AccountMeta(mint, false, true),
        AccountMeta(SYSVAR_RENT_PUBKEY, false, false)
    )

    val buffer = ByteBuffer.allocate(67).apply {
        order(ByteOrder.LITTLE_ENDIAN)
        put(INITIALIZE_MINT_ID.toByte())
        put(decimals)
        put(mintAuthority.toByteArray())
        put(if (freezeAuthority != null) 1 else 0)
        put((freezeAuthority ?: PublicKey(ByteArray(PublicKey.PUBLIC_KEY_LENGTH))).toByteArray())
    }

    return TransactionInstruction(PROGRAM_ID, keys, buffer.array())
}

fun TokenProgram.mintTo(
    mint: PublicKey, // Public key of the mint
    destination: PublicKey, // Address of the token account to mint to
    authority: PublicKey, // The mint authority
    amount: Long,
    programId: PublicKey = PROGRAM_ID
): TransactionInstruction {
    val keys = listOf(
        AccountMeta(mint, false, true),
        AccountMeta(destination, false, true),
        AccountMeta(authority, true, false)
    )

    val buffer = ByteBuffer.allocate(67).apply {
        order(ByteOrder.LITTLE_ENDIAN)
        put(MINT_TO.toByte())
        putLong(amount)
    }

    return TransactionInstruction(programId, keys, buffer.array())
}