//
// Types
// Metaplex
//
// This code was generated locally by Funkatronics on 2022-09-28
//
@file:UseSerializers(PublicKeyAs32ByteSerializer::class)

package com.metaplex.lib.experimental.jen.candyguard

import com.metaplex.lib.experimental.serialization.serializers.solana.PublicKeyAs32ByteSerializer
import com.solana.core.PublicKey
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.UByte
import kotlin.ULong
import kotlin.UShort
import kotlin.collections.List
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class AllowList(val merkleRoot: List<UByte>)

@Serializable
data class BotTax(val lamports: ULong, val lastInstruction: Boolean)

@Serializable
data class EndSettings(val endSettingType: EndSettingType, val number: ULong)

@Serializable
data class Gatekeeper(val gatekeeperNetwork: PublicKey, val expireOnUse: Boolean)

@Serializable
data class Lamports(val amount: ULong, val destination: PublicKey)

@Serializable
data class LiveDate(val date: Long?)

@Serializable
data class MintLimit(val id: UByte, val limit: UShort)

@Serializable
data class NftPayment(val burn: Boolean, val requiredCollection: PublicKey)

@Serializable
data class SplToken(
    val amount: ULong,
    val tokenMint: PublicKey,
    val destinationAta: PublicKey
)

@Serializable
data class ThirdPartySigner(val signerKey: PublicKey)

@Serializable
data class Whitelist(
    val mint: PublicKey,
    val presale: Boolean,
    val discountPrice: ULong?,
    val mode: WhitelistTokenMode
)

@Serializable
data class CandyGuardData(val default: GuardSet, val groups: List<Group>?)

@Serializable
data class Group(val label: String, val guards: GuardSet)

@Serializable
data class GuardSet(
    val botTax: BotTax?,
    val lamports: Lamports?,
    val splToken: SplToken?,
    val liveDate: LiveDate?,
    val thirdPartySigner: ThirdPartySigner?,
    val whitelist: Whitelist?,
    val gatekeeper: Gatekeeper?,
    val endSettings: EndSettings?,
    val allowList: AllowList?,
    val mintLimit: MintLimit?,
    val nftPayment: NftPayment?
)

enum class EndSettingType {
    Date,

    Amount
}

enum class WhitelistTokenMode {
    BurnEveryTime,

    NeverBurn
}
