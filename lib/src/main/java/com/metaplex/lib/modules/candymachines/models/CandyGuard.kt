/*
 * CandyGuard
 * metaplex-android
 * 
 * Created by Funkatronics on 10/13/2022
 */

package com.metaplex.lib.modules.candymachines.models

import com.metaplex.lib.experimental.jen.candyguard.*
import com.solana.core.PublicKey
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.Decoder

class CandyGuard(val base: PublicKey, val authority: PublicKey,
                 val defaultGuards: List<Guard> = listOf(),
                 val groups: Map<String, List<Guard>>? = null) {

    companion object {
        const val PROGRAM_NAME = "candy_guard"
        const val PROGRAM_ADDRESS = "CnDYGRdU51FsSyLnVgSd19MCFxA4YHT5h3nacvCKMPUJ"
    }
}

fun CandyGuard.Companion.pda(base: PublicKey) =
    PublicKey.findProgramAddress(listOf(
        PROGRAM_NAME.toByteArray(Charsets.UTF_8),
        base.toByteArray()
    ), PublicKey(PROGRAM_ADDRESS))

sealed interface Guard {
    data class AddressGate(val address: PublicKey): Guard
    data class AllowList(val merkleRoot: List<Byte>): Guard
    data class BotTax(val lamports: Long, val lastInstruction: Boolean): Guard
    data class EndDate(val date: Long): Guard
    data class Gatekeeper(val gatekeeperNetwork: PublicKey, val expireOnUse: Boolean): Guard
    data class MintLimit(val id: Byte, val limit: Short): Guard
    data class NftBurn(val requiredCollection: PublicKey): Guard
    data class NftGate(val requiredCollection: PublicKey): Guard
    data class NftPayment(val requiredCollection: PublicKey, val destination: PublicKey): Guard
    data class RedeemedAmount(val maximum: Long): Guard
    data class SolPayment(val lamports: Long, val destination: PublicKey): Guard
    data class StartDate(val date: Long): Guard
    data class ThirdPartySigner(val signerKey: PublicKey): Guard
    data class TokenBurn(val amount: Long, val mint: PublicKey): Guard
    data class TokenGate(val amount: Long, val mint: PublicKey): Guard
    data class TokenPayment(
        val amount: Long,
        val tokenMint: PublicKey,
        val destinationAta: PublicKey
    ): Guard
}

class GuardSetDeserializer : DeserializationStrategy<List<Guard>> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("GuardSet")

    override fun deserialize(decoder: Decoder): List<Guard> =
        decoder.decodeLong().let { flags ->
            GuardType.values().mapNotNull { guard ->
                if ((flags shr guard.ordinal and 1L) > 0) decodeGuard(guard, decoder) else null
            }
        }

    // I hate this, this is an extremely fragile and hard to maintain mapping
    // between the IDL GuardType and guard objects, and the library Guard set
    private fun decodeGuard(guard: GuardType, decoder: Decoder): Guard = when(guard) {
        GuardType.AddressGate ->
            decoder.decodeSerializableValue(AddressGate.serializer()).run {
                Guard.AddressGate(address)
            }
        GuardType.AllowList ->
            decoder.decodeSerializableValue(AllowListSerializer).run {
                Guard.AllowList(merkleRoot.map { it.toByte() })
            }
        GuardType.BotTax ->
            decoder.decodeSerializableValue(BotTax.serializer()).run {
                Guard.BotTax(lamports.toLong(), lastInstruction)
            }
        GuardType.EndDate ->
            decoder.decodeSerializableValue(EndDate.serializer()).run {
                Guard.EndDate(date)
            }
        GuardType.Gatekeeper ->
            decoder.decodeSerializableValue(Gatekeeper.serializer()).run {
                Guard.Gatekeeper(gatekeeperNetwork, expireOnUse)
            }
        GuardType.MintLimit ->
            decoder.decodeSerializableValue(MintLimit.serializer()).run {
                Guard.MintLimit(id.toByte(), limit.toShort())
            }
        GuardType.NftBurn ->
            decoder.decodeSerializableValue(NftBurn.serializer()).run {
                Guard.NftBurn(requiredCollection)
            }
        GuardType.NftGate ->
            decoder.decodeSerializableValue(NftGate.serializer()).run {
                Guard.NftGate(requiredCollection)
            }
        GuardType.NftPayment ->
            decoder.decodeSerializableValue(NftPayment.serializer()).run {
                Guard.NftPayment(requiredCollection, destination)
            }
        GuardType.RedeemedAmount ->
            decoder.decodeSerializableValue(RedeemedAmount.serializer()).run {
                Guard.RedeemedAmount(maximum.toLong())
            }
        GuardType.SolPayment ->
            decoder.decodeSerializableValue(SolPayment.serializer()).run {
                Guard.SolPayment(lamports.toLong(), destination)
            }
        GuardType.StartDate ->
            decoder.decodeSerializableValue(StartDate.serializer()).run {
                Guard.StartDate(date)
            }
        GuardType.ThirdPartySigner ->
            decoder.decodeSerializableValue(ThirdPartySigner.serializer()).run {
                Guard.ThirdPartySigner(signerKey)
            }
        GuardType.TokenBurn ->
            decoder.decodeSerializableValue(TokenBurn.serializer()).run {
                Guard.TokenBurn(amount.toLong(), mint)
            }
        GuardType.TokenGate ->
            decoder.decodeSerializableValue(TokenGate.serializer()).run {
                Guard.TokenGate(amount.toLong(), mint)
            }
        GuardType.TokenPayment ->
            decoder.decodeSerializableValue(TokenPayment.serializer()).run {
                Guard.TokenPayment(amount.toLong(), tokenMint, destinationAta)
            }
    }
}

internal val Guard.idlType get() = when(this) {
    is Guard.AddressGate -> GuardType.AddressGate
    is Guard.AllowList -> GuardType.AllowList
    is Guard.BotTax -> GuardType.BotTax
    is Guard.EndDate -> GuardType.EndDate
    is Guard.Gatekeeper -> GuardType.Gatekeeper
    is Guard.MintLimit -> GuardType.MintLimit
    is Guard.NftBurn -> GuardType.NftBurn
    is Guard.NftGate -> GuardType.NftGate
    is Guard.NftPayment -> GuardType.NftPayment
    is Guard.RedeemedAmount -> GuardType.RedeemedAmount
    is Guard.SolPayment -> GuardType.SolPayment
    is Guard.StartDate -> GuardType.StartDate
    is Guard.ThirdPartySigner -> GuardType.ThirdPartySigner
    is Guard.TokenBurn -> GuardType.TokenBurn
    is Guard.TokenGate -> GuardType.TokenGate
    is Guard.TokenPayment -> GuardType.TokenPayment
}

internal val Guard.idlObj get() = when(this) {
    is Guard.AddressGate -> AddressGate(address)
    is Guard.AllowList -> AllowList(merkleRoot.map { it.toUByte() })
    is Guard.BotTax -> BotTax(lamports.toULong(), lastInstruction)
    is Guard.EndDate -> EndDate(date)
    is Guard.Gatekeeper -> Gatekeeper(gatekeeperNetwork, expireOnUse)
    is Guard.MintLimit -> MintLimit(id.toUByte(), limit.toUShort())
    is Guard.NftBurn -> NftBurn(requiredCollection)
    is Guard.NftGate -> NftGate(requiredCollection)
    is Guard.NftPayment -> NftPayment(requiredCollection, destination)
    is Guard.RedeemedAmount -> RedeemedAmount(maximum.toULong())
    is Guard.SolPayment -> SolPayment(lamports.toULong(), destination)
    is Guard.StartDate -> StartDate(date)
    is Guard.ThirdPartySigner -> ThirdPartySigner(signerKey)
    is Guard.TokenBurn -> TokenBurn(amount.toULong(), mint)
    is Guard.TokenGate -> TokenGate(amount.toULong(), mint)
    is Guard.TokenPayment -> TokenPayment(amount.toULong(), tokenMint, destinationAta)
}

internal fun GuardType.serializer() = when(this) {
    GuardType.AddressGate -> AddressGate.serializer()
    GuardType.AllowList -> AllowListSerializer
    GuardType.BotTax -> BotTax.serializer()
    GuardType.EndDate -> EndDate.serializer()
    GuardType.Gatekeeper -> Gatekeeper.serializer()
    GuardType.MintLimit -> MintLimit.serializer()
    GuardType.NftBurn -> NftBurn.serializer()
    GuardType.NftGate -> NftGate.serializer()
    GuardType.NftPayment -> NftPayment.serializer()
    GuardType.RedeemedAmount -> RedeemedAmount.serializer()
    GuardType.SolPayment -> SolPayment.serializer()
    GuardType.StartDate -> StartDate.serializer()
    GuardType.ThirdPartySigner -> ThirdPartySigner.serializer()
    GuardType.TokenBurn -> TokenBurn.serializer()
    GuardType.TokenGate -> TokenGate.serializer()
    GuardType.TokenPayment -> TokenPayment.serializer()
}