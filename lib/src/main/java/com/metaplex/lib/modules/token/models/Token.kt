/*
 * Token
 * Metaplex
 * 
 * Created by Funkatronics on 8/31/2022
 */

package com.metaplex.lib.modules.token.models

import com.metaplex.lib.drivers.storage.StorageDriver
import com.metaplex.lib.modules.nfts.JsonMetadataTask
import com.metaplex.lib.programs.token_metadata.accounts.*
import com.solana.core.PublicKey

typealias FungibleToken = Token

open class Token (val metadataAccount: MetadataAccount) {
    val updateAuthority: PublicKey = metadataAccount.update_authority
    val mint: PublicKey = metadataAccount.mint
    val name: String = metadataAccount.data.name
    val symbol: String = metadataAccount.data.symbol
    val uri: String = metadataAccount.data.uri
    val sellerFeeBasisPoints: Int = metadataAccount.data.sellerFeeBasisPoints.toInt()
    val creators: Array<MetaplexCreator> = metadataAccount.data.creators ?: arrayOf()
    val primarySaleHappened: Boolean = metadataAccount.primarySaleHappened
    val isMutable: Boolean = metadataAccount.isMutable
    val editionNonce: Int? = metadataAccount.editionNonce?.toInt()
    val tokenStandard: MetaplexTokenStandard? = metadataAccount.tokenStandard ?: MetaplexTokenStandard.NonFungible
    val collection: MetaplexCollection? = metadataAccount.collection
    val collectionDetails: MetaplexCollectionDetails? = metadataAccount.collectionDetails
}

suspend fun Token.metadata(storageDriver: StorageDriver) =
    JsonMetadataTask(storageDriver, this).use()