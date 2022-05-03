package com.metaplex.lib.modules.nfts.models

import com.metaplex.lib.programs.token_metadata.MasterEditionAccount
import com.metaplex.lib.programs.token_metadata.MetadataAccount

object MetaplexContstants {
    const val METADATA_NAME = "metadata"
    const val METADATA_ACCOUNT_PUBKEY = "metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"
    const val METADATA_EDITION = "edition"
}

class NFT(val metadataAccount: MetadataAccount, val masterEditionAccount: MasterEditionAccount?)