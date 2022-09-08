package com.metaplex.lib.programs.tokens

import com.metaplex.lib.programs.token_metadata.gpa_builders.MintGpaBuilder
import com.metaplex.lib.programs.token_metadata.gpa_builders.TokenGpaBuilder
import com.metaplex.lib.programs.token_metadata.gpa_builders.TokenProgramGpaBuilder
import com.metaplex.lib.drivers.solana.Connection
import com.solana.core.PublicKey

class TokenProgram {
    companion object {
        val publicKey = PublicKey("TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA")

        fun accounts(connection: Connection): TokenProgramGpaBuilder {
            return TokenProgramGpaBuilder(connection, publicKey)
        }

        fun mintAccounts(connection: Connection): MintGpaBuilder {
            return this.accounts(connection).mintAccounts()
        }

        fun tokenAccounts(connection: Connection): TokenGpaBuilder {
            return this.accounts(connection).tokenAccounts()
        }
    }

}
