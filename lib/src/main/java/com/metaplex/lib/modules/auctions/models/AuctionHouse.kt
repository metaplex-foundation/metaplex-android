/*
 * AuctionHouse
 * Metaplex
 *
 * Created by Funkatronics on 7/19/2022
 */
@file:UseSerializers(PublicKeyAs32ByteSerializer::class)

package com.metaplex.lib.modules.auctions.models

import com.metaplex.lib.serialization.serializers.solana.PublicKeyAs32ByteSerializer
import com.solana.core.PublicKey
import kotlinx.serialization.*
import org.bitcoinj.core.Base58
import java.nio.ByteBuffer
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

val AuctionHouse.address get() = AuctionHouse.pda(creator, treasuryMint)

// TODO: is this correct? is there a better way to check this?
val AuctionHouse.isNative get() =
    treasuryMint == PublicKey("So11111111111111111111111111111111111111112")

//region PDAs
fun AuctionHouse.Companion.programAsSignerPda() =
    PublicKey.findProgramAddress(listOf(
        PROGRAM_NAME.toByteArray(),
        "signer".toByteArray()
    ), PublicKey(PROGRAM_ID))

fun AuctionHouse.Companion.listingReceiptPda(tradeState: PublicKey) =
    PublicKey.findProgramAddress(listOf(
        "listing_receipt".toByteArray(),
        tradeState.toByteArray()
    ), PublicKey(PROGRAM_ID))

fun AuctionHouse.Companion.bidReceiptPda(tradeState: PublicKey) =
    PublicKey.findProgramAddress(listOf(
        "bid_receipt".toByteArray(),
        tradeState.toByteArray()
    ), PublicKey(PROGRAM_ID))

fun AuctionHouse.Companion.purchaseReceiptPda(sellerTradeState: PublicKey, buyerTradeState: PublicKey) =
    PublicKey.findProgramAddress(listOf(
        "purchase_receipt".toByteArray(),
        sellerTradeState.toByteArray(),
        buyerTradeState.toByteArray(),
    ), PublicKey(PROGRAM_ID))

fun AuctionHouse.tokenAccountPda(auctioneerAuthority: PublicKey) =
    PublicKey.findProgramAddress(listOf(
        "auctioneer".toByteArray(),
        address.toByteArray(),
        auctioneerAuthority.toByteArray()
    ), AuctionHouse.publicKey).address

fun AuctionHouse.auctioneerPda(auctioneerAuthority: PublicKey) =
    PublicKey.findProgramAddress(listOf(
        "auctioneer".toByteArray(),
        address.toByteArray(),
        auctioneerAuthority.toByteArray()
    ), AuctionHouse.publicKey).address

fun AuctionHouse.buyerEscrowPda(buyer: PublicKey) =
    PublicKey.findProgramAddress(listOf(
        AuctionHouse.PROGRAM_NAME.toByteArray(),
        address.toByteArray(),
        buyer.toByteArray()
    ), PublicKey(AuctionHouse.PROGRAM_ID))

fun AuctionHouse.tradeStatePda(wallet: PublicKey, mintAccount: PublicKey, price: Long, tokens: Long,
                               tokenAccount: PublicKey?) =
    PublicKey.findProgramAddress(listOf(
        AuctionHouse.PROGRAM_NAME.toByteArray(), // program name
        wallet.toByteArray(), // wallet
        address.toByteArray(), // auction house
        tokenAccount?.toByteArray() ?: byteArrayOf(), // optional token account
        treasuryMint.toByteArray(), // treasury mint
        mintAccount.toByteArray(), // token mint
        ByteBuffer.allocate(Long.SIZE_BYTES).putLong(price).array(), // price as long
        ByteBuffer.allocate(Long.SIZE_BYTES).putLong(tokens).array(), // token size (?)
    ), PublicKey(AuctionHouse.PROGRAM_ID))
//endregion
