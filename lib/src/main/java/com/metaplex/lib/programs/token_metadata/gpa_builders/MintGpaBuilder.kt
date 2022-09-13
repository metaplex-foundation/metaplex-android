package com.metaplex.lib.programs.token_metadata.gpa_builders

import com.metaplex.lib.drivers.solana.Connection
import com.solana.core.PublicKey

class MintGpaBuilder(
    override val connection: Connection,
    override val programId: PublicKey
): TokenProgramGpaBuilder(connection, programId) {

    fun whereDoesntHaveMintAuthority(): MintGpaBuilder {
        var mutableGpaBulder = this
        return mutableGpaBulder.where(0, 0.toByte())
    }

    fun whereHasMintAuthority(): MintGpaBuilder {
        var mutableGpaBulder = this
        return mutableGpaBulder.where(0, 1.toByte())
    }

    fun whereMintAuthority(mintAuthority: PublicKey): MintGpaBuilder {
        var mutableGpaBulder = this
        mutableGpaBulder = mutableGpaBulder.whereHasMintAuthority()
        return mutableGpaBulder.where(4, mintAuthority)
    }

    fun whereSupply(supply: Long): MintGpaBuilder {
        var mutableGpaBulder = this
        return mutableGpaBulder.where(36, supply)
    }
}
