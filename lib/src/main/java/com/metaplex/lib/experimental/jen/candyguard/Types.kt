//
// Types
// Metaplex
//
// This code was generated locally by Funkatronics on 2022-11-01
//
@file:UseSerializers(PublicKeyAs32ByteSerializer::class, AllowListSerializer::class)

package com.metaplex.lib.experimental.jen.candyguard

import com.metaplex.lib.serialization.serializers.solana.PublicKeyAs32ByteSerializer
import com.solana.core.PublicKey
import kotlinx.serialization.KSerializer
import kotlin.Boolean
import kotlin.ByteArray
import kotlin.Long
import kotlin.String
import kotlin.UByte
import kotlin.ULong
import kotlin.UShort
import kotlin.collections.List
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class AddressGate(val address: PublicKey)

data class AllowList(val merkleRoot: List<UByte>)

object AllowListSerializer : KSerializer<AllowList> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("AllowList")

    override fun deserialize(decoder: Decoder): AllowList =
        AllowList(List(32) {
            decoder.decodeByte().toUByte()
        })

    override fun serialize(encoder: Encoder, value: AllowList) =
        value.merkleRoot.take(32).forEach {
            encoder.encodeByte(it.toByte())
        }
}

@Serializable
data class AllowListProof(val timestamp: Long)

@Serializable
data class BotTax(val lamports: ULong, val lastInstruction: Boolean)

@Serializable
data class EndDate(val date: Long)

@Serializable
data class FreezeSolPayment(val lamports: ULong, val destination: PublicKey)

@Serializable
data class FreezeTokenPayment(
    val amount: ULong,
    val mint: PublicKey,
    val destinationAta: PublicKey
)

@Serializable
data class Gatekeeper(val gatekeeperNetwork: PublicKey, val expireOnUse: Boolean)

@Serializable
data class MintLimit(val id: UByte, val limit: UShort)

@Serializable
data class MintCounter(val count: UShort)

@Serializable
data class NftBurn(val requiredCollection: PublicKey)

@Serializable
data class NftGate(val requiredCollection: PublicKey)

@Serializable
data class NftPayment(val requiredCollection: PublicKey, val destination: PublicKey)

@Serializable
data class ProgramGate(val additional: List<PublicKey>)

@Serializable
data class RedeemedAmount(val maximum: ULong)

@Serializable
data class SolPayment(val lamports: ULong, val destination: PublicKey)

@Serializable
data class StartDate(val date: Long)

@Serializable
data class ThirdPartySigner(val signerKey: PublicKey)

@Serializable
data class TokenBurn(val amount: ULong, val mint: PublicKey)

@Serializable
data class TokenGate(val amount: ULong, val mint: PublicKey)

@Serializable
data class TokenPayment(
    val amount: ULong,
    val mint: PublicKey,
    val destinationAta: PublicKey
)

@Serializable
data class RouteArgs(val guard: GuardType, val data: ByteArray)

@Serializable
data class CandyGuardData(val default: GuardSet, val groups: List<Group>?)

@Serializable
data class Group(val label: String, val guards: GuardSet)

@Serializable
data class GuardSet(
    val botTax: BotTax?,
    val solPayment: SolPayment?,
    val tokenPayment: TokenPayment?,
    val startDate: StartDate?,
    val thirdPartySigner: ThirdPartySigner?,
    val tokenGate: TokenGate?,
    val gatekeeper: Gatekeeper?,
    val endDate: EndDate?,
    val allowList: AllowList?,
    val mintLimit: MintLimit?,
    val nftPayment: NftPayment?,
    val redeemedAmount: RedeemedAmount?,
    val addressGate: AddressGate?,
    val nftGate: NftGate?,
    val nftBurn: NftBurn?,
    val tokenBurn: TokenBurn?,
    val freezeSolPayment: FreezeSolPayment?,
    val freezeTokenPayment: FreezeTokenPayment?,
    val programGate: ProgramGate?
)

enum class FreezeInstruction {
    Initialize,

    Thaw,

    UnlockFunds
}

enum class GuardType {
    BotTax,

    SolPayment,

    TokenPayment,

    StartDate,

    ThirdPartySigner,

    TokenGate,

    Gatekeeper,

    EndDate,

    AllowList,

    MintLimit,

    NftPayment,

    RedeemedAmount,

    AddressGate,

    NftGate,

    NftBurn,

    TokenBurn,

    FreezeSolPayment,

    FreezeTokenPayment,

    ProgramGate
}
