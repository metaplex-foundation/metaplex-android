/*
 * AuctionHouse
 * Metaplex
 *
 * Created by Funkatronics on 7/19/2022
 */
@file:UseSerializers(PublicKeyAs32ByteSerializer::class)

package com.metaplex.lib.modules.auctions.models

import com.metaplex.lib.modules.token.WRAPPED_SOL_MINT_ADDRESS
import com.metaplex.lib.serialization.serializers.solana.PublicKeyAs32ByteSerializer
import com.solana.core.PublicKey
import kotlinx.serialization.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets

@Serializable
data class AuctionHouse(
    val treasuryWithdrawalDestinationOwner: PublicKey,
    val feeWithdrawalDestination: PublicKey,
    val treasuryMint: PublicKey = PublicKey(WRAPPED_SOL_MINT_ADDRESS),
    val authority: PublicKey,
    val creator: PublicKey,
    val sellerFeeBasisPoints: UShort,
    val requiresSignOff: Boolean = false,
    val canChangeSalePrice: Boolean = false,
    val hasAuctioneer: Boolean = false
) {
    companion object {
        const val PROGRAM_NAME = "auction_house"
        const val PROGRAM_ADDRESS = "hausS13jsjafwWwGqZTUQRmWyvyxn9EQpqMwV1PBBmk"

        fun pda(creator: PublicKey, treasuryMint: PublicKey) =
            PublicKey.findProgramAddress(listOf(
                PROGRAM_NAME.toByteArray(StandardCharsets.UTF_8),
                creator.toByteArray(),
                treasuryMint.toByteArray()
            ), PublicKey(PROGRAM_ADDRESS))
    }
}

val AuctionHouse.address get() = AuctionHouse.pda(creator, treasuryMint).address
val AuctionHouse.auctionHouseFeeAccount get() = feeAccountPda().address
val AuctionHouse.auctionHouseTreasury get() = treasuryAccountPda().address

// TODO: is this correct? is there a better way to check this?
val AuctionHouse.isNative get() =
    treasuryMint == PublicKey(WRAPPED_SOL_MINT_ADDRESS)

//region PDAs
fun AuctionHouse.feeAccountPda() =
    PublicKey.findProgramAddress(listOf(
        AuctionHouse.PROGRAM_NAME.toByteArray(Charsets.UTF_8),
        address.toByteArray(),
        "fee_payer".toByteArray(Charsets.UTF_8)
    ), PublicKey(AuctionHouse.PROGRAM_ADDRESS))

fun AuctionHouse.treasuryAccountPda() =
    PublicKey.findProgramAddress(listOf(
        AuctionHouse.PROGRAM_NAME.toByteArray(Charsets.UTF_8),
        address.toByteArray(),
        "treasury".toByteArray(Charsets.UTF_8)
    ), PublicKey(AuctionHouse.PROGRAM_ADDRESS))

fun AuctionHouse.Companion.programAsSignerPda() =
    PublicKey.findProgramAddress(listOf(
        PROGRAM_NAME.toByteArray(Charsets.UTF_8),
        "signer".toByteArray(Charsets.UTF_8)
    ), PublicKey(PROGRAM_ADDRESS))

fun AuctionHouse.Companion.listingReceiptPda(tradeState: PublicKey) =
    PublicKey.findProgramAddress(listOf(
        "listing_receipt".toByteArray(Charsets.UTF_8),
        tradeState.toByteArray()
    ), PublicKey(PROGRAM_ADDRESS))

fun AuctionHouse.Companion.bidReceiptPda(tradeState: PublicKey) =
    PublicKey.findProgramAddress(listOf(
        "bid_receipt".toByteArray(Charsets.UTF_8),
        tradeState.toByteArray()
    ), PublicKey(PROGRAM_ADDRESS))

fun AuctionHouse.Companion.purchaseReceiptPda(sellerTradeState: PublicKey, buyerTradeState: PublicKey) =
    PublicKey.findProgramAddress(listOf(
        "purchase_receipt".toByteArray(Charsets.UTF_8),
        sellerTradeState.toByteArray(),
        buyerTradeState.toByteArray(),
    ), PublicKey(PROGRAM_ADDRESS))

fun AuctionHouse.tokenAccountPda(auctioneerAuthority: PublicKey) =
    PublicKey.findProgramAddress(listOf(
        "auctioneer".toByteArray(Charsets.UTF_8),
        address.toByteArray(),
        auctioneerAuthority.toByteArray()
    ), PublicKey(AuctionHouse.PROGRAM_ADDRESS)).address

fun AuctionHouse.auctioneerPda(auctioneerAuthority: PublicKey) =
    PublicKey.findProgramAddress(listOf(
        "auctioneer".toByteArray(Charsets.UTF_8),
        address.toByteArray(),
        auctioneerAuthority.toByteArray()
    ), PublicKey(AuctionHouse.PROGRAM_ADDRESS)).address

fun AuctionHouse.buyerEscrowPda(buyer: PublicKey) =
    PublicKey.findProgramAddress(listOf(
        AuctionHouse.PROGRAM_NAME.toByteArray(Charsets.UTF_8),
        address.toByteArray(),
        buyer.toByteArray()
    ), PublicKey(AuctionHouse.PROGRAM_ADDRESS))

fun AuctionHouse.tradeStatePda(wallet: PublicKey, mintAccount: PublicKey, price: Long, tokens: Long,
                               tokenAccount: PublicKey?) =
    PublicKey.findProgramAddress(listOf(
        AuctionHouse.PROGRAM_NAME.toByteArray(Charsets.UTF_8), // program name
        wallet.toByteArray(), // wallet
        address.toByteArray(), // auction house
        tokenAccount?.toByteArray() ?: byteArrayOf(), // optional token account
        treasuryMint.toByteArray(), // treasury mint
        mintAccount.toByteArray(), // token mint
        ByteBuffer.allocate(Long.SIZE_BYTES)
            .order(ByteOrder.LITTLE_ENDIAN).putLong(price).array(), // price as long
        ByteBuffer.allocate(Long.SIZE_BYTES)
            .order(ByteOrder.LITTLE_ENDIAN).putLong(tokens).array(), // token size (?)
    ), PublicKey(AuctionHouse.PROGRAM_ADDRESS))
//endregion