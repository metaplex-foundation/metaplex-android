//
// Accounts
// Metaplex
//
// This code was generated locally by Funkatronics on 2022-09-27
//
@file:UseSerializers(PublicKeyAs32ByteSerializer::class)

package com.metaplex.lib.experimental.jen.candymachinev2

import com.metaplex.lib.serialization.serializers.solana.PublicKeyAs32ByteSerializer
import com.solana.core.PublicKey
import kotlin.Boolean
import kotlin.Long
import kotlin.ULong
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
class CandyMachine(
    val authority: PublicKey,
    val wallet: PublicKey,
    val tokenMint: PublicKey?,
    val itemsRedeemed: ULong,
    val data: CandyMachineData
)

@Serializable
class CollectionPDA(val mint: PublicKey, val candyMachine: PublicKey)

@Serializable
class FreezePDA(
    val candyMachine: PublicKey,
    val allowThaw: Boolean,
    val frozenCount: ULong,
    val mintStart: Long?,
    val freezeTime: Long,
    val freezeFee: ULong
)
