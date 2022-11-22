/*
 * CandyGuard
 * metaplex-android
 * 
 * Created by Funkatronics on 10/13/2022
 */

package com.metaplex.lib.modules.candymachines.models

import com.metaplex.lib.experimental.jen.candyguard.*
import com.metaplex.lib.modules.candymachines.CANDY_GUARD_LABEL_SIZE
import com.metaplex.lib.serialization.serializers.solana.AnchorAccountSerializer
import com.solana.core.PublicKey
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.nio.charset.StandardCharsets
import kotlin.math.min

import com.metaplex.lib.experimental.jen.candyguard.CandyGuard as CandyGuardAccount

class CandyGuard(val base: PublicKey, val authority: PublicKey,
                 val defaultGuards: List<Guard> = listOf(),
                 val groups: Map<String, List<Guard>>? = null) {

    companion object {
        const val PROGRAM_NAME = "candy_guard"
        const val PROGRAM_ADDRESS = "Guard1JwRhJkVH6XZhzoYxeBVQe872VH6QggF4BWmS9g"
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
        val mint: PublicKey,
        val destinationAta: PublicKey
    ): Guard
    data class FreezeSolPayment(val lamports: Long, val destination: PublicKey) : Guard
    data class FreezeTokenPayment(
        val amount: Long,
        val mint: PublicKey,
        val destinationAta: PublicKey
    ) : Guard
    data class ProgramGate(val additional: List<PublicKey>) : Guard
}

object CandyGuardSerializer : KSerializer<CandyGuard> {
    override val descriptor: SerialDescriptor = CandyGuardAccount.serializer().descriptor

    override fun serialize(encoder: Encoder, value: CandyGuard) {
        // encode account data
        encoder.encodeSerializableValue(AnchorAccountSerializer(),
            CandyGuardAccount(value.base, CandyGuard.pda(value.base).nonce.toUByte(), value.authority)
        )

        // encode default guards
        encoder.encodeSerializableValue(GuardSetSerializer, value.defaultGuards.toList())

        // encode guard groups
        encoder.encodeInt(value.groups?.keys?.size ?: 0) // group count
        value.groups?.forEach { group ->

            // encode label
            val labelBytes = ByteArray(CANDY_GUARD_LABEL_SIZE)

            group.key.toByteArray(Charsets.UTF_8)
                .copyInto(labelBytes, endIndex = min(group.key.length, CANDY_GUARD_LABEL_SIZE))

            labelBytes.forEach { encoder.encodeByte(it) }

            // encode guard set for group
            encoder.encodeSerializableValue(GuardSetSerializer,
                group.value.toList().sortedBy { it.idlType.ordinal })
        }
    }

    override fun deserialize(decoder: Decoder): CandyGuard {
        // decode account data
        val account = decoder.decodeSerializableValue(AnchorAccountSerializer<CandyGuardAccount>())

        // decode default guards
        val defaultGuardSet = GuardSetSerializer.deserialize(decoder)

        // decode guard groups
        val groupCount = decoder.decodeInt()
        val groups = (0 until groupCount).associate {
            // decode fixed size label string
            val bytes = ByteArray(CANDY_GUARD_LABEL_SIZE) { decoder.decodeByte() }
            val label = String(bytes, StandardCharsets.UTF_8)
                .replace("\u0000", "")

            // build guard group
            label to GuardSetSerializer.deserialize(decoder)
        }

        return CandyGuard(account.base, account.authority, defaultGuardSet, groups)
    }
}

object GuardSetSerializer : KSerializer<List<Guard>> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("GuardSet")

    override fun serialize(encoder: Encoder, value: List<Guard>) {

        // encode feature flags
        var flags: Long = 0
        value.forEach {
            flags = flags or (1L shl it.idlType.ordinal)
        }
        encoder.encodeLong(flags)

        // encode guards
        value.sortedBy { it.idlType.ordinal }.forEach {
            encodeGuard(it, encoder)
        }
    }

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
                Guard.TokenPayment(amount.toLong(), mint, destinationAta)
            }
        GuardType.FreezeSolPayment ->
            decoder.decodeSerializableValue(FreezeSolPayment.serializer()).run {
                Guard.FreezeSolPayment(lamports.toLong(), destination)
            }
        GuardType.FreezeTokenPayment ->
            decoder.decodeSerializableValue(FreezeTokenPayment.serializer()).run {
                Guard.FreezeTokenPayment(amount.toLong(), mint, destinationAta)
            }
        GuardType.ProgramGate ->
            decoder.decodeSerializableValue(ProgramGate.serializer()).run {
                Guard.ProgramGate(additional)
            }
    }

    private fun encodeGuard(guard: Guard, encoder: Encoder) = when(guard) {
        is Guard.AddressGate ->
            encoder.encodeSerializableValue(AddressGate.serializer(), AddressGate(guard.address))
        is Guard.AllowList ->
            encoder.encodeSerializableValue(AllowListSerializer, AllowList(guard.merkleRoot.map { it.toUByte() }))
        is Guard.BotTax ->
            encoder.encodeSerializableValue(BotTax.serializer(), BotTax(guard.lamports.toULong(), guard.lastInstruction))
        is Guard.EndDate ->
            encoder.encodeSerializableValue(EndDate.serializer(), EndDate(guard.date))
        is Guard.Gatekeeper ->
            encoder.encodeSerializableValue(Gatekeeper.serializer(), Gatekeeper(guard.gatekeeperNetwork, guard.expireOnUse))
        is Guard.MintLimit ->
            encoder.encodeSerializableValue(MintLimit.serializer(), MintLimit(guard.id.toUByte(), guard.limit.toUShort()))
        is Guard.NftBurn ->
            encoder.encodeSerializableValue(NftBurn.serializer(), NftBurn(guard.requiredCollection))
        is Guard.NftGate ->
            encoder.encodeSerializableValue(NftGate.serializer(), NftGate(guard.requiredCollection))
        is Guard.NftPayment ->
            encoder.encodeSerializableValue(NftPayment.serializer(), NftPayment(guard.requiredCollection, guard.destination))
        is Guard.RedeemedAmount ->
            encoder.encodeSerializableValue(RedeemedAmount.serializer(), RedeemedAmount(guard.maximum.toULong()))
        is Guard.SolPayment ->
            encoder.encodeSerializableValue(SolPayment.serializer(), SolPayment(guard.lamports.toULong(), guard.destination))
        is Guard.StartDate ->
            encoder.encodeSerializableValue(StartDate.serializer(), StartDate(guard.date))
        is Guard.ThirdPartySigner ->
            encoder.encodeSerializableValue(ThirdPartySigner.serializer(), ThirdPartySigner(guard.signerKey))
        is Guard.TokenBurn ->
            encoder.encodeSerializableValue(TokenBurn.serializer(), TokenBurn(guard.amount.toULong(), guard.mint))
        is Guard.TokenGate ->
            encoder.encodeSerializableValue(TokenGate.serializer(), TokenGate(guard.amount.toULong(), guard.mint))
        is Guard.TokenPayment ->
            encoder.encodeSerializableValue(TokenPayment.serializer(), TokenPayment(guard.amount.toULong(), guard.mint, guard.destinationAta))
        is Guard.FreezeSolPayment ->
            encoder.encodeSerializableValue(FreezeSolPayment.serializer(), FreezeSolPayment(guard.lamports.toULong(), guard.destination))
        is Guard.FreezeTokenPayment ->
            encoder.encodeSerializableValue(FreezeTokenPayment.serializer(), FreezeTokenPayment(guard.amount.toULong(), guard.mint, guard.destinationAta))
        is Guard.ProgramGate ->
            encoder.encodeSerializableValue(ProgramGate.serializer(), ProgramGate(guard.additional))
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
    is Guard.FreezeSolPayment -> GuardType.FreezeSolPayment
    is Guard.FreezeTokenPayment -> GuardType.FreezeTokenPayment
    is Guard.ProgramGate -> GuardType.ProgramGate
}