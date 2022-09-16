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
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class CandyMachineData(
    val uuid: String,
    val price: ULong,
    val symbol: String,
    val sellerFeeBasisPoints: UShort,
    val maxSupply: ULong,
    val isMutable: Boolean,
    val retainAuthority: Boolean,
    val goLiveDate: Long?,
    val endSettings: EndSettings?,
    val creators: List<Creator>,
    val hiddenSettings: HiddenSettings?,
    val whitelistMintSettings: WhitelistMintSettings?,
    val itemsAvailable: ULong,
    val gatekeeper: GatekeeperConfig?
)

@Serializable
data class ConfigLine(val name: String, val uri: String)

@Serializable
data class EndSettings(val endSettingType: EndSettingType, val number: ULong)

@Serializable
data class Creator(
    val address: PublicKey,
    val verified: Boolean,
    val share: UByte
)

@Serializable
data class HiddenSettings(
    val name: String,
    val uri: String,
    val hash: List<UByte>
)

@Serializable
data class WhitelistMintSettings(
    val mode: WhitelistMintMode,
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
