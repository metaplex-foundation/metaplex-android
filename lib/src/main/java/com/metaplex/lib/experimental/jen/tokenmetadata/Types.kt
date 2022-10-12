//
// Types
// Metaplex
//
// This code was generated locally by Funkatronics on 2022-09-29
//
@file:UseSerializers(PublicKeyAs32ByteSerializer::class)

package com.metaplex.lib.experimental.jen.tokenmetadata

import com.metaplex.lib.serialization.serializers.solana.PublicKeyAs32ByteSerializer
import com.solana.core.PublicKey
import kotlinx.serialization.KSerializer
import kotlin.Boolean
import kotlin.String
import kotlin.UByte
import kotlin.ULong
import kotlin.UShort
import kotlin.collections.List
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class MintPrintingTokensViaTokenArgs(val supply: ULong)

@Serializable
data class SetReservationListArgs(
    val reservations: List<Reservation>,
    val totalReservationSpots: ULong?,
    val offset: ULong,
    val totalSpotOffset: ULong
)

@Serializable
data class UpdateMetadataAccountArgs(
    val data: Data?,
    val updateAuthority: PublicKey?,
    val primarySaleHappened: Boolean?
)

@Serializable
data class UpdateMetadataAccountArgsV2(
    val data: DataV2?,
    val updateAuthority: PublicKey?,
    val primarySaleHappened: Boolean?,
    val isMutable: Boolean?
)

@Serializable
data class CreateMetadataAccountArgs(val data: Data, val isMutable: Boolean)

@Serializable
data class CreateMetadataAccountArgsV2(val data: DataV2, val isMutable: Boolean)

@Serializable
data class CreateMetadataAccountArgsV3(
    val data: DataV2,
    val isMutable: Boolean,
    @Serializable(with = CollectionDetailsSerializer::class) val collectionDetails: CollectionDetails?
)

object CollectionDetailsSerializer : KSerializer<CollectionDetails> {
    override val descriptor = Byte.serializer().descriptor
    override fun serialize(encoder: Encoder, value: CollectionDetails) {
        encoder.encodeByte(value.ordinal.toByte())
        encoder.encodeLong(0)
    }

    override fun deserialize(decoder: Decoder): CollectionDetails {
        return CollectionDetails.values().get(decoder.decodeByte().toInt()).also {
            decoder.decodeLong()
        }
    }
}

@Serializable
data class CreateMasterEditionArgs(val maxSupply: ULong?)

@Serializable
data class MintNewEditionFromMasterEditionViaTokenArgs(val edition: ULong)

@Serializable
data class ApproveUseAuthorityArgs(val numberOfUses: ULong)

@Serializable
data class UtilizeArgs(val numberOfUses: ULong)

@Serializable
data class SetCollectionSizeArgs(val size: ULong)

@Serializable
data class Data(
    val name: String,
    val symbol: String,
    val uri: String,
    val sellerFeeBasisPoints: UShort,
    val creators: List<Creator>?
)

@Serializable
data class DataV2(
    val name: String,
    val symbol: String,
    val uri: String,
    val sellerFeeBasisPoints: UShort,
    val creators: List<Creator>?,
    val collection: Collection?,
    val uses: Uses?
)

@Serializable
data class Uses(
    val useMethod: UseMethod,
    val remaining: ULong,
    val total: ULong
)

@Serializable
data class Collection(val verified: Boolean, val key: PublicKey)

@Serializable
data class Creator(
    val address: PublicKey,
    val verified: Boolean,
    val share: UByte
)

@Serializable
data class Reservation(
    val address: PublicKey,
    val spotsRemaining: ULong,
    val totalSpots: ULong
)

@Serializable
data class ReservationV1(
    val address: PublicKey,
    val spotsRemaining: UByte,
    val totalSpots: UByte
)

enum class Key {
    Uninitialized,

    EditionV1,

    MasterEditionV1,

    ReservationListV1,

    MetadataV1,

    ReservationListV2,

    MasterEditionV2,

    EditionMarker,

    UseAuthorityRecord,

    CollectionAuthorityRecord
}

enum class UseMethod {
    Burn,

    Multiple,

    Single
}

//@Serializable
//sealed class CollectionDetails
//
//@Serializable
//data class V1(val size: ULong) : CollectionDetails()

enum class CollectionDetails {
    V1
}

enum class TokenStandard {
    NonFungible,

    FungibleAsset,

    Fungible,

    NonFungibleEdition
}