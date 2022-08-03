/*
 * AuctionHouse
 * Metaplex
 *
 * Created by Funkatronics on 7/19/2022
 */
@file:UseSerializers(PublicKeyAs32ByteSerializer::class)

package com.metaplex.lib.modules.auctions.models

import com.metaplex.lib.experimental.serialization.serializers.solana.PublicKeyAs32ByteSerializer
import com.solana.core.PublicKey
import kotlinx.serialization.*
import org.bitcoinj.core.Base58
import java.nio.charset.StandardCharsets

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
) {
    companion object {
        const val PROGRAM_NAME = "auction_house"
        const val PROGRAM_ID = "hausS13jsjafwWwGqZTUQRmWyvyxn9EQpqMwV1PBBmk"

        val publicKey get() = PublicKey(Base58.decode(PROGRAM_ID))

        fun pda(creator: PublicKey, treasuryMint: PublicKey) =
            PublicKey.findProgramAddress(listOf(
                PROGRAM_NAME.toByteArray(StandardCharsets.UTF_8),
                creator.toByteArray(),
                treasuryMint.toByteArray()
            ), publicKey).address
    }
}
