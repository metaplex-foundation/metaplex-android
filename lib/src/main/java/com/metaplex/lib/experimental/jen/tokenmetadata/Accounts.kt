//
// Accounts
// Metaplex
//
// This code was generated locally by Funkatronics on 2022-10-03
//
@file:UseSerializers(PublicKeyAs32ByteSerializer::class)

package com.metaplex.lib.experimental.jen.tokenmetadata

import com.metaplex.lib.serialization.serializers.solana.PublicKeyAs32ByteSerializer
import com.solana.core.PublicKey
import kotlin.Boolean
import kotlin.UByte
import kotlin.ULong
import kotlin.collections.List
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
class UseAuthorityRecord(
    val key: Key,
    val allowedUses: ULong,
    val bump: UByte
)

@Serializable
class CollectionAuthorityRecord(val key: Key, val bump: UByte)

@Serializable
class Metadata(
    val key: Key,
    val updateAuthority: PublicKey,
    val mint: PublicKey,
    val data: Data,
    val primarySaleHappened: Boolean,
    val isMutable: Boolean,
    val editionNonce: UByte?,
    val tokenStandard: TokenStandard?,
    val collection: Collection?,
    val uses: Uses?,
    val collectionDetails: CollectionDetails?
)

@Serializable
class MasterEditionV2(
    val key: Key,
    val supply: ULong,
    val maxSupply: ULong?
)

@Serializable
class MasterEditionV1(
    val key: Key,
    val supply: ULong,
    val maxSupply: ULong?,
    val printingMint: PublicKey,
    val oneTimePrintingAuthorizationMint: PublicKey
)

@Serializable
class Edition(
    val key: Key,
    val parent: PublicKey,
    val edition: ULong
)

@Serializable
class ReservationListV2(
    val key: Key,
    val masterEdition: PublicKey,
    val supplySnapshot: ULong?,
    val reservations: List<Reservation>,
    val totalReservationSpots: ULong,
    val currentReservationSpots: ULong
)

@Serializable
class ReservationListV1(
    val key: Key,
    val masterEdition: PublicKey,
    val supplySnapshot: ULong?,
    val reservations: List<ReservationV1>
)

@Serializable
class EditionMarker(val key: Key, val ledger: List<UByte>)
