@file:UseSerializers(PublicKeyAs32ByteSerializer::class)

package com.metaplex.lib.programs.token_metadata.accounts

import com.metaplex.lib.modules.nfts.models.MetaplexContstants
import com.metaplex.lib.programs.token_metadata.MetadataKey
import com.metaplex.lib.serialization.serializers.solana.ByteDiscriminatorSerializer
import com.metaplex.lib.serialization.serializers.solana.PublicKeyAs32ByteSerializer
import com.metaplex.lib.shared.OperationError
import com.metaplex.lib.shared.ResultWithCustomError
import com.solana.core.PublicKey
import com.solana.vendor.borshj.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bitcoinj.core.Base58

// TODO: Deprecate objects in this file.
//  this will be replaced with beet kt generated code in the near future

enum class MetaplexTokenStandard {
    NonFungible, FungibleAsset, Fungible, NonFungibleEdition
}

enum class MetaplexCollectionDetails {
    V1
}

@Serializable
data class MetaplexCollection(
    val verified: Boolean,
    val key: PublicKey,
)

@Serializable
data class MetadataAccount(
    val key: Byte,
    val update_authority: PublicKey,
    val mint: PublicKey,
    val data: MetaplexData,
    val primarySaleHappened: Boolean,
    val isMutable: Boolean,
    val editionNonce: UByte?,
    val tokenStandard: MetaplexTokenStandard?,
    val collection: MetaplexCollection?,
    val uses: Uses? = null,
    val collectionDetails: MetaplexCollectionDetails? = null
) : BorshCodable {
    companion object {
        fun pda(publicKey: PublicKey): ResultWithCustomError<PublicKey, OperationError> {
            val pdaSeeds = listOf(
                MetaplexContstants.METADATA_NAME.toByteArray(),
                Base58.decode(MetaplexContstants.METADATA_ACCOUNT_PUBKEY),
                publicKey.toByteArray()
            )

            val pdaAddres = PublicKey.findProgramAddress(
                pdaSeeds,
                PublicKey(MetaplexContstants.METADATA_ACCOUNT_PUBKEY)
            )
            return ResultWithCustomError.success(pdaAddres.address)
        }
    }
}

@Serializable
data class MetaplexData(
    val name: String,
    val symbol: String,
    val uri: String,
    val sellerFeeBasisPoints: Short,
    val creators: Array<MetaplexCreator>?
){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MetaplexData
        if (name != other.name) return false
        if (symbol != other.symbol) return false
        if (uri != other.uri) return false
        if (sellerFeeBasisPoints != other.sellerFeeBasisPoints) return false
        if (!creators.contentEquals(other.creators)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + symbol.hashCode()
        result = 31 * result + uri.hashCode()
        result = 31 * result + sellerFeeBasisPoints
        result = 31 * result + creators.contentHashCode()
        return result
    }
}

@Serializable
data class MetaplexCreator(
    val address: PublicKey,
    val verified: Byte,
    val share: Byte,
)

@Serializable
data class Uses(val method: Byte = 0, val remaining: Long = 0, val total: Long = 0)

object CollectionDetailsSerializer : KSerializer<MetaplexCollectionDetails> {
    override val descriptor = Byte.serializer().descriptor
    override fun serialize(encoder: Encoder, value: MetaplexCollectionDetails) {
        encoder.encodeByte(value.ordinal.toByte())
        encoder.encodeLong(0)
    }

    override fun deserialize(decoder: Decoder): MetaplexCollectionDetails {
        return MetaplexCollectionDetails.values().get(decoder.decodeByte().toInt()).also {
            decoder.decodeLong()
        }
    }
}