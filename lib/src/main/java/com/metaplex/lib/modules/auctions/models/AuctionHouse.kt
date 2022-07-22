@file:UseSerializers(PublicKeyAsByteArraySerializer::class)

package com.metaplex.lib.modules.auctions.models

import com.metaplex.lib.experimental.serialization.serializers.PublicKeyAsByteArraySerializer
import com.metaplex.lib.programs.token_metadata.accounts.MetaplexTokenStandard
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

@Serializable
data class MetadataAccount(
    val key: UByte,
    val update_authority: PublicKey,
    val mint: PublicKey,
    val data: MetaplexData,
    val primarySaleHappened: Boolean,
    val isMutable: Boolean,
    val editionNonce: UInt?,
    val tokenStandard: MetaplexTokenStandard?,
    val collection: MetaplexCollection?
)

@Serializable
data class MetaplexData(
    val name: String,
    val symbol: String,
    val uri: String,
    val sellerFeeBasisPoints: UInt,
    val hasCreators: Boolean,
    val addressCount: UInt,
    val creators: List<MetaplexCreator>
)

@Serializable
data class MetaplexCollection(
    val verified: Boolean,
    val key: PublicKey,
)

@Serializable
data class MetaplexCreator(
    val address: PublicKey,
    val verified: UInt,
    val share: UInt,
)
