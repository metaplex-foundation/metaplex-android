//
// Types
// Metaplex
//
// This code was generated locally by Funkatronics on 2022-09-08
//
@file:UseSerializers(PublicKeyAs32ByteSerializer::class)

package com.metaplex.lib.experimental.jen.candymachine

import com.metaplex.lib.experimental.serialization.serializers.solana.PublicKeyAs32ByteSerializer
import com.solana.core.PublicKey
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.UByte
import kotlin.ULong
import kotlin.UShort
import kotlin.collections.List
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class CandyMachineData(
    val uuid: Int,
    val price: ULong,
    val symbol: Int,
    val sellerFeeBasisPoints: UShort,
    val maxSupply: ULong,
    val isMutable: Boolean,
    val retainAuthority: Boolean,
    val goLiveDate: Long?,
    val endSettings: Int,
    val creators: List<Creator>,
    val hiddenSettings: Int,
    val whitelistMintSettings: Int,
    val itemsAvailable: ULong,
    val gatekeeper: Int
)

@Serializable
data class ConfigLine(val name: Int, val uri: Int)

@Serializable
data class EndSettings(val endSettingType: Int, val number: ULong)

@Serializable
data class Creator(
    val address: PublicKey,
    val verified: Boolean,
    val share: UByte
)

@Serializable
data class HiddenSettings(
    val name: Int,
    val uri: Int,
    val hash: Int
)

@Serializable
data class WhitelistMintSettings(
    val mode: Int,
    val mint: PublicKey,
    val presale: Boolean,
    val discountPrice: ULong?
)

@Serializable
data class GatekeeperConfig(val gatekeeperNetwork: PublicKey, val expireOnUse: Boolean)

enum class EndSettingType {
    Date,

    Amount
}

enum class WhitelistMintMode {
    BurnEveryTime,

    NeverBurn
}
