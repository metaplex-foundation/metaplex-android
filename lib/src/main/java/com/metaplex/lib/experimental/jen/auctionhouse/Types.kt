//
// Types
// Metaplex
//
// This code was generated locally by Funkatronics on 2022-08-11
//
package com.metaplex.lib.experimental.jen.auctionhouse

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
