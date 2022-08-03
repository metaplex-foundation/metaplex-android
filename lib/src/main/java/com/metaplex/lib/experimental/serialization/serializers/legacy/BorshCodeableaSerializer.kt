/*
 * BorshCodeableaSerializer
 * metaplex-android
 * 
 * Created by Funkatronics on 8/1/2022
 */

package com.metaplex.lib.experimental.serialization.serializers.legacy

import com.metaplex.lib.experimental.serialization.format.BorshDecoder
import com.metaplex.lib.experimental.serialization.format.BorshEncoder
import com.metaplex.lib.programs.token_metadata.MasterEditionAccountRule
import com.metaplex.lib.programs.token_metadata.accounts.MetadataAccountRule
import com.metaplex.lib.programs.token_metadata.accounts.MetaplexCollectionRule
import com.metaplex.lib.programs.token_metadata.accounts.MetaplexCreatorRule
import com.metaplex.lib.programs.token_metadata.accounts.MetaplexDataRule
import com.metaplex.lib.shared.AccountPublicKeyRule
import com.solana.vendor.borshj.BorshBuffer
import com.solana.vendor.borshj.BorshCodable
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializer(forClass = BorshCodable::class)
internal class BorshCodeableSerializer<T>(val clazz: Class<T>) : KSerializer<BorshCodable?> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(clazz.simpleName) {}

    val rule = listOf(
        MetadataAccountRule(),
        MetaplexDataRule(),
        MetaplexCollectionRule(),
        AccountPublicKeyRule(),
        MasterEditionAccountRule(),
        MetaplexCreatorRule()
    ).find { it.clazz == clazz }

    override fun deserialize(decoder: Decoder): BorshCodable? =
        rule?.read(BorshBuffer.wrap((decoder as? BorshDecoder)?.bytes))

    override fun serialize(encoder: Encoder, value: BorshCodable?) {
        value?.let {
            rule?.write(value, BorshBuffer.wrap((encoder as? BorshEncoder)?.borshEncodedBytes))
        }
    }
}