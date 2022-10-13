package com.metaplex.lib.programs.token_metadata

import com.metaplex.lib.programs.token_metadata.gpa_builders.MetadataV1GpaBuilder
import com.metaplex.lib.programs.token_metadata.gpa_builders.TokenMetadataGpaBuilder
import com.metaplex.lib.drivers.solana.Connection
import com.solana.core.PublicKey

class TokenMetadataProgram {
    companion object {
        val publicKey =
        PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s")

        fun accounts(connection: Connection): TokenMetadataGpaBuilder {
            return TokenMetadataGpaBuilder(connection, TokenMetadataProgram.publicKey)
        }

        fun metadataV1Accounts(connection: Connection): MetadataV1GpaBuilder {
            return this.accounts(connection).metadataV1Accounts()
        }
    }
}
