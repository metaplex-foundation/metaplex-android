package com.metaplex.lib.modules.fungibletokens.models

import com.metaplex.lib.programs.token_metadata.accounts.MetadataAccount
import com.metaplex.lib.programs.token_metadata.accounts.MetaplexCollection
import com.metaplex.lib.programs.token_metadata.accounts.MetaplexCreator
import com.metaplex.lib.programs.token_metadata.accounts.MetaplexTokenStandard
import com.solana.core.PublicKey

class FungibleToken(
    val metadataAccount: MetadataAccount
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
}