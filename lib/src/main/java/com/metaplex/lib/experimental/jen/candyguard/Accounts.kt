//
// Accounts
// Metaplex
//
// This code was generated locally by Funkatronics on 2022-10-26
//
@file:UseSerializers(PublicKeyAs32ByteSerializer::class)

package com.metaplex.lib.experimental.jen.candyguard

import com.metaplex.lib.serialization.serializers.solana.PublicKeyAs32ByteSerializer
import com.solana.core.PublicKey
import kotlin.Long
import kotlin.UByte
import kotlin.UShort
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
class AllowListProof(val timestamp: Long)

@Serializable
class MintCounter(val count: UShort)

@Serializable
class CandyGuard(
    val base: PublicKey,
    val bump: UByte,
    val authority: PublicKey
)
