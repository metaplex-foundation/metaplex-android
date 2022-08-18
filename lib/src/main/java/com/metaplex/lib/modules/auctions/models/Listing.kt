/*
 * Listing
 * Metaplex
 * 
 * Created by Funkatronics on 8/18/2022
 */

package com.metaplex.lib.modules.auctions.models

import com.metaplex.lib.experimental.jen.auctionhouse.ListingReceipt

data class Listing(val asset: Asset, val receipt: ListingReceipt)

val Listing.seller get() = receipt.seller
val Listing.tradeState get() = receipt.tradeState
val Listing.price get() = receipt.price
val Listing.tokenSize get() = receipt.tokenSize
val Listing.purchaseReceipt get() = receipt.purchaseReceipt