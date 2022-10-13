/*
 * Metadata
 * metaplex-android
 * 
 * Created by Funkatronics on 10/11/2022
 */

package com.metaplex.lib.modules.nfts.models

import com.metaplex.lib.experimental.jen.tokenmetadata.Creator
import com.solana.core.PublicKey

data class Metadata(
    val name: String,
    val symbol: String = "",
    val uri: String,
    val sellerFeeBasisPoints: Int,
    val creators: List<Creator>? = null,
    val collection: PublicKey? = null,
    val isMutable: Boolean = true
)