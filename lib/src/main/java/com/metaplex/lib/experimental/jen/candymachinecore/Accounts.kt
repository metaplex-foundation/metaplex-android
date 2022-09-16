//
// Accounts
// Metaplex
//
// This code was generated locally by Funkatronics on 2022-09-16
//
@file:UseSerializers(PublicKeyAs32ByteSerializer::class)

package com.metaplex.lib.experimental.jen.candymachinecore

import com.metaplex.lib.experimental.serialization.serializers.solana.PublicKeyAs32ByteSerializer
import com.solana.core.PublicKey
import kotlin.ULong
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
class CandyMachine(
    val features: ULong,
    val authority: PublicKey,
    val mintAuthority: PublicKey,
    val collectionMint: PublicKey,
    val itemsRedeemed: ULong,
    val data: CandyMachineData
)
