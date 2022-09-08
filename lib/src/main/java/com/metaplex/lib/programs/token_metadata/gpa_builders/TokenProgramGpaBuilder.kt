package com.metaplex.lib.programs.token_metadata.gpa_builders

import com.metaplex.lib.shared.GpaBuilder
import com.metaplex.lib.shared.GpaBuilderFactory
import com.metaplex.lib.drivers.solana.Connection
import com.solana.core.PublicKey

val MINT_SIZE = 82
val ACCOUNT_SIZE = 165

open class TokenProgramGpaBuilder(
    override val connection: Connection,
    override val programId: PublicKey
): GpaBuilder(connection, programId) {

    fun mintAccounts(): MintGpaBuilder {
        val g =  GpaBuilderFactory.from(MintGpaBuilder::class.java, this)
        return g.whereSize(MINT_SIZE)
    }

    fun tokenAccounts(): TokenGpaBuilder {
        val g =  GpaBuilderFactory.from(TokenGpaBuilder::class.java, this)
        return g.whereSize(ACCOUNT_SIZE)
    }
}
