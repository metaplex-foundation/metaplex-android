package com.metaplex.lib.modules.nfts.models

import com.metaplex.lib.Metaplex
import com.metaplex.lib.modules.nfts.JsonMetadataTask
import com.metaplex.lib.programs.token_metadata.MasterEditionAccount
import com.metaplex.lib.programs.token_metadata.accounts.MetadataAccount
import com.metaplex.lib.programs.token_metadata.accounts.MetaplexCollection
import com.metaplex.lib.programs.token_metadata.accounts.MetaplexCreator
import com.metaplex.lib.programs.token_metadata.accounts.MetaplexTokenStandard
import com.metaplex.lib.shared.ResultWithCustomError
import com.solana.core.PublicKey

object MetaplexContstants {
    const val METADATA_NAME = "metadata"
    const val METADATA_ACCOUNT_PUBKEY = "metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"
    const val METADATA_EDITION = "edition"
}

class NFT(
    val metadataAccount: MetadataAccount,
    val masterEditionAccount: MasterEditionAccount?
) {

    val updateAuthority: PublicKey = metadataAccount.update_authority
    val mint: PublicKey = metadataAccount.mint
    val name: String = metadataAccount.data.name
    val symbol: String = metadataAccount.data.symbol
    val uri: String = metadataAccount.data.uri
    val sellerFeeBasisPoints: Int = metadataAccount.data.sellerFeeBasisPoints
    val creators: Array<MetaplexCreator> = metadataAccount.data.creators
    val primarySaleHappened: Boolean = metadataAccount.primarySaleHappened
    val isMutable: Boolean = metadataAccount.isMutable
    val editionNonce: Int? = metadataAccount.editionNonce
    val tokenStandard: MetaplexTokenStandard? = metadataAccount.tokenStandard
    val collection: MetaplexCollection? = metadataAccount.collection

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