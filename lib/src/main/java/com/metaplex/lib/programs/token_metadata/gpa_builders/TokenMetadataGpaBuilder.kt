package com.metaplex.lib.programs.token_metadata.gpa_builders

import com.metaplex.lib.programs.token_metadata.MetadataKey
import com.metaplex.lib.shared.GpaBuilder
import com.metaplex.lib.shared.GpaBuilderFactory
import com.metaplex.lib.drivers.solana.Connection
import com.solana.core.PublicKey

open class TokenMetadataGpaBuilder(
    override val connection: Connection,
    override val programId: PublicKey
): GpaBuilder(connection, programId) {
    fun whereKey(key: MetadataKey): MetadataV1GpaBuilder {
        val mutableGpaBulder = this
        return mutableGpaBulder.where(0, key.ordinal.toByte())
    }

    fun metadataV1Accounts(): MetadataV1GpaBuilder {
        val metadata = GpaBuilderFactory.from(MetadataV1GpaBuilder::class.java, this)
        return metadata.whereKey(MetadataKey.MetadataV1)
    }

    fun masterEditionV1Accounts(): GpaBuilder {
        return this.whereKey(MetadataKey.MasterEditionV1)
    }

    fun masterEditionV2Accounts(): GpaBuilder {
        return this.whereKey(MetadataKey.MasterEditionV1)
    }
}