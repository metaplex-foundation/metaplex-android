/*
 * Bid
 * Metaplex
 * 
 * Created by Funkatronics on 8/16/2022
 */

package com.metaplex.lib.modules.auctions.models

import com.metaplex.lib.experimental.jen.auctionhouse.BidReceipt

data class Bid(val asset: Asset, val receipt: BidReceipt)

val Bid.buyer get() = receipt.buyer
val Bid.tradeState get() = receipt.tradeState
val Bid.price get() = receipt.price
val Bid.tokenSize get() = receipt.tokenSize
val Bid.purchaseReceipt get() = receipt.purchaseReceipt