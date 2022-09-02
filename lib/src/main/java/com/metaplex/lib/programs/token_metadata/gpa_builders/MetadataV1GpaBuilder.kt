package com.metaplex.lib.programs.token_metadata.gpa_builders

import com.metaplex.lib.drivers.solana.Connection
import com.solana.core.PublicKey

val MAX_NAME_LENGTH = 32
val MAX_SYMBOL_LENGTH = 10
val MAX_URI_LENGTH = 200
val MAX_CREATOR_LEN = 32 + 1 + 1
val DATA_START = 1 + 32 + 32
val NAME_START = DATA_START + 4
val SYMBOL_START = NAME_START + MAX_NAME_LENGTH + 4
val URI_START = SYMBOL_START + MAX_SYMBOL_LENGTH + 4
val CREATORS_START = URI_START + MAX_URI_LENGTH + 2 + 1 + 4

class MetadataV1GpaBuilder(override val connection: Connection,
                           override val programId: PublicKey
                           ): TokenMetadataGpaBuilder(connection,programId) {

    fun selectUpdatedAuthority(): MetadataV1GpaBuilder {
        var mutableGpaBulder = this
        return mutableGpaBulder.slice(1, 3)
    }

    fun whereUpdateAuthority(updateAuthority: PublicKey): MetadataV1GpaBuilder {
        var mutableGpaBulder = this
        return mutableGpaBulder.where(1, updateAuthority)
    }

    fun selectMint(): MetadataV1GpaBuilder {
        var mutableGpaBulder = this
        return mutableGpaBulder.slice(33, 32)
    }

    fun whereMint(mint: PublicKey): MetadataV1GpaBuilder {
        var mutableGpaBulder = this
        return mutableGpaBulder.where(33, mint)
    }

    fun selectName(): MetadataV1GpaBuilder {
        var mutableGpaBulder = this
        return mutableGpaBulder.slice(NAME_START, MAX_NAME_LENGTH)
    }

    fun whereName(name: String): MetadataV1GpaBuilder {
        var mutableGpaBulder = this
        return mutableGpaBulder.where(NAME_START, name)
    }

    fun selectSymbol(): MetadataV1GpaBuilder {
        var mutableGpaBulder = this
        return mutableGpaBulder.slice(SYMBOL_START, MAX_SYMBOL_LENGTH)
    }

    fun whereSymbol(symbol: String): MetadataV1GpaBuilder {
        var mutableGpaBulder = this
        return mutableGpaBulder.where(SYMBOL_START, symbol)
    }

    fun selectUri(): MetadataV1GpaBuilder {
        var mutableGpaBulder = this
        return mutableGpaBulder.slice(URI_START, MAX_URI_LENGTH)
    }

    fun whereUri(uri: String): MetadataV1GpaBuilder {
        var mutableGpaBulder = this
        return mutableGpaBulder.where(URI_START, uri)
    }

    fun selectCreator(nth: Int): MetadataV1GpaBuilder {
        var mutableGpaBulder = this
        return mutableGpaBulder.slice(
            CREATORS_START + (nth - 1) * MAX_CREATOR_LEN,
            CREATORS_START + nth * MAX_CREATOR_LEN
        )
    }

    fun whereCreator(nth: Int, creator: PublicKey): MetadataV1GpaBuilder {
        var mutableGpaBulder = this
        return mutableGpaBulder.where(CREATORS_START + (nth - 1) * MAX_CREATOR_LEN, creator)
    }

    fun selectFirstCreator(): MetadataV1GpaBuilder {
        return this.selectCreator(1)
    }

    fun whereFirstCreator(firstCreator: PublicKey): MetadataV1GpaBuilder {
        return this.whereCreator(1, firstCreator)
    }
}