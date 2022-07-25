/*
 * AuctionHouse
 * Metaplex
 *
 * Created by Funkatronics on 7/19/2022
 */
@file:UseSerializers(PublicKeyAsByteArraySerializer::class)

package com.metaplex.lib.modules.auctions.models

import com.metaplex.lib.experimental.serialization.serializers.PublicKeyAsByteArraySerializer
import com.solana.core.PublicKey
import kotlinx.serialization.*

@Serializable
data class AuctionHouse(
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
    val auctioneerPdaBump: UByte
)
