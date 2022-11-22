//
// Accounts
// Metaplex
//
// This code was generated locally by Funkatronics on 2022-10-20
//
@file:UseSerializers(PublicKeyAs32ByteSerializer::class)

package com.metaplex.lib.experimental.jen.auctionhouse

import com.metaplex.lib.serialization.serializers.solana.PublicKeyAs32ByteSerializer
import com.solana.core.PublicKey
import kotlin.Boolean
import kotlin.Long
import kotlin.UByte
import kotlin.ULong
import kotlin.UShort
import kotlin.collections.List
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
class BidReceipt(
    val tradeState: PublicKey,
    val bookkeeper: PublicKey,
    val auctionHouse: PublicKey,
    val buyer: PublicKey,
    val metadata: PublicKey,
    val tokenAccount: PublicKey?,
    val purchaseReceipt: PublicKey?,
    val price: ULong,
    val tokenSize: ULong,
    val bump: UByte,
    val tradeStateBump: UByte,
    val createdAt: Long,
    val canceledAt: Long?
)

@Serializable
class ListingReceipt(
    val tradeState: PublicKey,
    val bookkeeper: PublicKey,
    val auctionHouse: PublicKey,
    val seller: PublicKey,
    val metadata: PublicKey,
    val purchaseReceipt: PublicKey?,
    val price: ULong,
    val tokenSize: ULong,
    val bump: UByte,
    val tradeStateBump: UByte,
    val createdAt: Long,
    val canceledAt: Long?
)

@Serializable
class PurchaseReceipt(
    val bookkeeper: PublicKey,
    val buyer: PublicKey,
    val seller: PublicKey,
    val auctionHouse: PublicKey,
    val metadata: PublicKey,
    val tokenSize: ULong,
    val price: ULong,
    val bump: UByte,
    val createdAt: Long
)

@Serializable
class AuctionHouse(
    val auctionHouseFeeAccount: PublicKey,
    val auctionHouseTreasury: PublicKey,
    val treasuryWithdrawalDestination: PublicKey,
    val feeWithdrawalDestination: PublicKey,
    val treasuryMint: PublicKey,
    val authority: PublicKey,
    val creator: PublicKey,
    val bump: UByte,
    val treasuryBump: UByte,
    val feePayerBump: UByte,
    val sellerFeeBasisPoints: UShort,
    val requiresSignOff: Boolean,
    val canChangeSalePrice: Boolean,
    val escrowPaymentBump: UByte,
    val hasAuctioneer: Boolean,
    val auctioneerAddress: PublicKey,
    val scopes: List<Boolean>
)

@Serializable
class Auctioneer(
    val auctioneerAuthority: PublicKey,
    val auctionHouse: PublicKey,
    val bump: UByte
)
