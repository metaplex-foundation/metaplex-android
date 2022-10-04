//
// Types
// Metaplex
//
// This code was generated locally by Funkatronics on 2022-09-30
//
@file:UseSerializers(PublicKeyAs32ByteSerializer::class)

package com.metaplex.lib.experimental.jen.candymachine

import com.metaplex.lib.serialization.serializers.solana.PublicKeyAs32ByteSerializer
import com.solana.core.PublicKey
import kotlin.Boolean
import kotlin.String
import kotlin.UByte
import kotlin.UInt
import kotlin.ULong
import kotlin.UShort
import kotlin.collections.List
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class CandyMachineData(
    val itemsAvailable: ULong,
    val symbol: String,
    val sellerFeeBasisPoints: UShort,
    val maxSupply: ULong,
    val isMutable: Boolean,
    val creators: List<Creator>,
    val configLineSettings: ConfigLineSettings?,
    val hiddenSettings: HiddenSettings?
)

@Serializable
data class Creator(
    val address: PublicKey,
    val verified: Boolean,
    val percentageShare: UByte
)

@Serializable
data class HiddenSettings(
    val name: String,
    val uri: String,
    val hash: List<UByte>
)

@Serializable
data class ConfigLineSettings(
    val prefixName: String,
    val nameLength: UInt,
    val prefixUri: String,
    val uriLength: UInt,
    val isSequential: Boolean
)

@Serializable
data class ConfigLine(val name: String, val uri: String)
