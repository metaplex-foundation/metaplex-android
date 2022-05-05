package com.metaplex.lib.programs.token_metadata.gpa_builders

import com.metaplex.lib.solana.Connection
import com.solana.core.PublicKey

class TokenGpaBuilder(
    override val connection: Connection,
    override val programId: PublicKey
): TokenProgramGpaBuilder(connection, programId) {

    fun selectMint(): TokenGpaBuilder {
        val mutableGpaBulder = this
        return mutableGpaBulder.slice(0, 32)
    }

    fun whereMint(mint: PublicKey): TokenGpaBuilder {
        val mutableGpaBulder = this
        return mutableGpaBulder.where(0.toUInt(), mint)
    }

    fun selectOwner(): TokenGpaBuilder {
        val mutableGpaBulder = this
        return mutableGpaBulder.slice(32, 32)
    }

    fun whereOwner(owner: PublicKey): TokenGpaBuilder {
        val mutableGpaBulder = this
        return mutableGpaBulder.where(32.toUInt(), owner)
    }

    fun selectAmount(): TokenGpaBuilder {
        var mutableGpaBulder = this
        return mutableGpaBulder.slice(64, 8)
    }

    fun whereAmount(amount: Int): TokenGpaBuilder {
        val mutableGpaBulder = this
        return mutableGpaBulder.where(64.toUInt(), amount)
    }

    fun whereDoesntHaveDelegate(): TokenGpaBuilder {
        val mutableGpaBulder = this
        return mutableGpaBulder.where(72.toUInt(), 0.toByte())
    }

    fun whereHasDelegate(): TokenGpaBuilder {
        var mutableGpaBulder = this
        return mutableGpaBulder.where(72.toUInt(), 1.toByte())
    }

    fun whereDelegate(delegate: PublicKey): TokenGpaBuilder {
        var mutableGpaBulder = this
        mutableGpaBulder = mutableGpaBulder.whereHasDelegate()
        return mutableGpaBulder.where(76.toUInt(), delegate)
    }
}
