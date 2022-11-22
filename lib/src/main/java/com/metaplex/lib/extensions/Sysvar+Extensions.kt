/*
 * Sysvar+Extensions
 * Metaplex
 * 
 * Created by Funkatronics on 9/21/2022
 */

package com.metaplex.lib.extensions

import com.solana.core.PublicKey
import com.solana.core.Sysvar

const val SYSVAR_CLOCK_ID = "SysvarC1ock11111111111111111111111111111111"
const val SYSVAR_INSTRUCTIONS_ID = "Sysvar1nstructions1111111111111111111111111"
const val SYSVAR_SLOT_HASHES_ID = "SysvarS1otHashes111111111111111111111111111"

val Sysvar.SYSVAR_CLOCK_PUBKEY get () = PublicKey(SYSVAR_CLOCK_ID)
val Sysvar.SYSVAR_INSTRUCTIONS_PUBKEY get () = PublicKey(SYSVAR_INSTRUCTIONS_ID)
val Sysvar.SYSVAR_SLOT_HASHES_PUBKEY get () = PublicKey(SYSVAR_SLOT_HASHES_ID)