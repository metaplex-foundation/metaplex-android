package com.metaplex.lib.modules.nfts.models

import com.metaplex.lib.Metaplex
import com.metaplex.lib.modules.nfts.JsonMetadataTask
import com.metaplex.lib.modules.token.models.Token
import com.metaplex.lib.programs.token_metadata.MasterEditionAccount
import com.metaplex.lib.programs.token_metadata.accounts.MetadataAccount
import com.metaplex.lib.shared.ResultWithCustomError

object MetaplexContstants {
    const val METADATA_NAME = "metadata"
    const val METADATA_ACCOUNT_PUBKEY = "metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"
    const val METADATA_EDITION = "edition"
}

class NFT(metadataAccount: MetadataAccount,
          val masterEditionAccount: MasterEditionAccount?) : Token(metadataAccount) {

    fun metadata(
        metaplex: Metaplex,
        onComplete: (ResultWithCustomError<JsonMetadata, Exception>) -> Unit
    ) {
        JsonMetadataTask(metaplex, this).use {
            it.onSuccess { metadata ->
                onComplete(ResultWithCustomError.success(metadata))
            }.onFailure { error ->
                onComplete(ResultWithCustomError.failure(error))
            }
        }
    }
}