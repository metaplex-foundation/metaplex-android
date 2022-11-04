//
// Accounts
// Metaplex
//
// This code was generated locally by Funkatronics on 2022-11-01
//
@file:UseSerializers(PublicKeyAs32ByteSerializer::class)

package com.metaplex.lib.experimental.jen.candyguard

import com.metaplex.lib.serialization.serializers.solana.PublicKeyAs32ByteSerializer
import com.solana.core.PublicKey
import kotlin.Long
import kotlin.UByte
import kotlin.ULong
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
class FreezeEscrow(
    val candyGuard: PublicKey,
    val candyMachine: PublicKey,
    val frozenCount: ULong,
    val firstMintTime: Long?,
    val freezePeriod: Long,
    val destination: PublicKey,
    val authority: PublicKey
)

@Serializable
class CandyGuard(
    val base: PublicKey,
    val bump: UByte,
    val authority: PublicKey
)
