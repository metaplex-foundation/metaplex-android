//
// Accounts
// Metaplex
//
// This code was generated locally by Funkatronics on 2023-07-18
//
@file:UseSerializers(PublicKeyAs32ByteSerializer::class)

package com.metaplex.lib.experimental.jen.bubblegum

import com.metaplex.lib.serialization.serializers.solana.PublicKeyAs32ByteSerializer
import com.solana.core.PublicKey
import kotlin.Boolean
import kotlin.UInt
import kotlin.ULong
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
class TreeConfig(
    val treeCreator: PublicKey,
    val treeDelegate: PublicKey,
    val totalMintCapacity: ULong,
    val numMinted: ULong,
    val isPublic: Boolean
)

@Serializable
class Voucher(
    val leafSchema: LeafSchema,
    val index: UInt,
    val merkleTree: PublicKey
)
