@file:UseSerializers(PublicKeyAs32ByteSerializer::class)

package com.metaplex.lib.programs.token_metadata.accounts

import com.metaplex.lib.modules.nfts.NftPdasClient
import com.metaplex.lib.serialization.serializers.solana.PublicKeyAs32ByteSerializer
import com.metaplex.lib.shared.OperationError
import com.metaplex.lib.shared.ResultWithCustomError
import com.solana.core.PublicKey
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

// TODO: Deprecate objects in this file.
//  this will be replaced with beet kt generated code in the near future

enum class MetadataKey {
     Uninitialized, // 0
     EditionV1, // 1
     MasterEditionV1, // 2
     ReservationListV1, // 3
     MetadataV1, // 4,
     ReservationListV2, // 5
     MasterEditionV2, // 6,
     EditionMarker, // 7
     UseAuthorityRecord, // 8
     CollectionAuthorityRecord, // 9
}

@Serializable
data class MasterEditionAccount(val type: Int, val masterEditionVersion: MasterEditionVersion) {
    companion object {
        @Deprecated("Deprecated, please use NftPdasClient.masterEdition",
            replaceWith = ReplaceWith(" NftPdasClient.masterEdition(publicKey)"))
        fun pda(publicKey: PublicKey): ResultWithCustomError<PublicKey, OperationError> {
            val publicKey = NftPdasClient.masterEdition(publicKey).getOrElse {
                return ResultWithCustomError.failure(OperationError.CouldNotFindPDA)
            }
            return ResultWithCustomError.success(publicKey)
        }
    }
}

@Serializable
sealed class MasterEditionVersion {
    @Serializable
    data class MasterEditionV1 (
        val supply: Long?,
        val max_supply: Long?,
        val printing_mint: PublicKey,
        val one_time_printing_authorization_mint: PublicKey
    ): MasterEditionVersion()

    @Serializable
    data class MasterEditionV2 (
        val supply: Long?,
        val max_supply: Long?
    ): MasterEditionVersion()
}

// have to use this for now because the experimental beet kt does not handle sealed classes yet
// this will be replaced with beet kt generated code in the near future
object MasterEditionAccountSerializer : KSerializer<MasterEditionAccount> {

    override val descriptor: SerialDescriptor = MasterEditionAccount.serializer().descriptor

    override fun deserialize(decoder: Decoder): MasterEditionAccount {
        val key = decoder.decodeByte().toInt()
        val masterEditionVersion = if (key == MetadataKey.MasterEditionV1.ordinal) {
            decoder.decodeSerializableValue(MasterEditionVersion.MasterEditionV1.serializer())
        } else {
            decoder.decodeSerializableValue(MasterEditionVersion.MasterEditionV2.serializer())
        }
        return MasterEditionAccount(key, masterEditionVersion)
    }

    override fun serialize(encoder: Encoder, value: MasterEditionAccount) {
        encoder.encodeByte(value.type.toByte())
        encoder.encodeSerializableValue(MasterEditionVersion.serializer(), value.masterEditionVersion)
    }
}