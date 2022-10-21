//
// Types
// Metaplex
//
// This code was generated locally by Funkatronics on 2022-10-20
//
@file:UseSerializers(PublicKeyAs32ByteSerializer::class)

package com.metaplex.lib.experimental.jen.auctionhouse

import com.metaplex.lib.serialization.serializers.solana.PublicKeyAs32ByteSerializer
import kotlinx.serialization.UseSerializers

enum class AuthorityScope {
    Deposit,

    Buy,

    PublicBuy,

    ExecuteSale,

    Sell,

    Cancel,

    Withdraw
}

enum class BidType {
    PublicSale,

    PrivateSale,

    AuctioneerPublicSale,

    AuctioneerPrivateSale
}

enum class ListingType {
    Sell,

    AuctioneerSell
}

enum class PurchaseType {
    ExecuteSale,

    AuctioneerExecuteSale
}

enum class CancelType {
    Cancel,

    AuctioneerCancel
}
