/*
 * Asset
 * Metaplex
 * 
 * Created by Funkatronics on 8/16/2022
 */

package com.metaplex.lib.modules.auctions.models

import com.solana.core.PublicKey

// TODO: this will likely need to be changed/expanded as we add support for all asset types (SFT)
data class Asset(val mintAddress: PublicKey, val tokenAccount: PublicKey, val metadata: PublicKey)