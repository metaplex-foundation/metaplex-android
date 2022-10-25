/*
 * AuctionHouseClient
 * Metaplex
 * 
 * Created by Funkatronics on 8/11/2022
 */

package com.metaplex.lib.modules.auctions

import com.metaplex.lib.Metaplex
import com.metaplex.lib.drivers.indenty.IdentityDriver
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.drivers.solana.getRecentBlockhash
import com.metaplex.lib.extensions.signSendAndConfirm
import com.metaplex.lib.modules.auctions.builders.AuctionHouseBuyTransactionBuilder
import com.metaplex.lib.modules.auctions.builders.AuctionHouseCancelTransactionBuilder
import com.metaplex.lib.modules.auctions.models.*
import com.metaplex.lib.modules.nfts.operations.FindNftByMintOnChainOperationHandler
import com.metaplex.lib.modules.token.operations.FindFungibleTokenByMintOnChainOperationHandler
import com.metaplex.lib.modules.token.operations.FindTokenMetadataAccountOperation
import com.metaplex.lib.programs.token_metadata.accounts.MetadataAccount
import com.solana.core.PublicKey
import com.solana.core.Transaction
import com.solana.programs.AssociatedTokenProgram
import com.solana.programs.TokenProgram
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.builtins.ByteArraySerializer
import kotlin.coroutines.suspendCoroutine

/**
 * NFT Auction House Client
 *
 * This object represents a client for a specific Auction House
 *
 * @author Funkatronics
 */
class AuctionHouseClient(
    val auctionHouse: AuctionHouse, val connection: Connection, val signer: IdentityDriver,
    val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    constructor(metaplex: Metaplex, auctionHouse: AuctionHouse)
            : this(auctionHouse, metaplex.connection, metaplex.identity())

    suspend fun list(mint: PublicKey, price: Long, authority: PublicKey = auctionHouse.authority,
                     auctioneerAuthority: PublicKey? = null, printReceipt: Boolean = true)
    : Result<Listing> {

        if (auctionHouse.hasAuctioneer && auctioneerAuthority == null)
            return Result.failure(Error("Auctioneer Authority Required"))

        val tokens: Long = 1

        val seller = signer.publicKey

        Listing(auctionHouse, seller, authority,
            mintAccount = mint, price = price, tokens = tokens).apply {

            buildTransaction(printReceipt).signSendAndConfirm(connection, signer, listOf())
                .getOrElse {
                    return Result.failure(it) // we cant proceed further, return the error
                }

            return Result.success(this)
        }
    }

    suspend fun bid(mint: PublicKey, price: Long, authority: PublicKey = auctionHouse.authority,
                    auctioneerAuthority: PublicKey? = null, printReceipt: Boolean = true)
    : Result<Bid> {

        if (auctionHouse.hasAuctioneer && auctioneerAuthority == null)
            return Result.failure(Error("Auctioneer Authority Required"))

        val tokens: Long = 1

        val buyer = signer.publicKey

        Bid(auctionHouse, mint, buyer, authority, price = price, tokens = tokens).apply {

            if (tokenAccount == null) {
                connection.getAccountInfo(ByteArraySerializer(), buyerTokenAccount).getOrElse {
                    val associatedTokenAddress = PublicKey.associatedTokenAddress(buyer, mint).address
                    Transaction().addInstruction(
                        AssociatedTokenProgram.createAssociatedTokenAccountInstruction(
                            mint = mint,
                            associatedAccount = associatedTokenAddress,
                            owner = buyer,
                            payer = signer.publicKey
                        )
                    ).signSendAndConfirm(connection, signer, listOf())
                        .getOrElse {
                            return Result.failure(it) // we cant proceed further, return the error
                        }
                }
            }

            buildTransaction(printReceipt).signSendAndConfirm(connection, signer, listOf())
                .getOrElse {
                    return Result.failure(it) // we cant proceed further, return the error
                }

            return Result.success(this)
        }
    }

    suspend fun executeSale(listing: Listing, bid: Bid,
                            auctioneerAuthority: PublicKey? = null,
                            bookkeeper: PublicKey = signer.publicKey,
                            printReceipt: Boolean = true): Result<Purchase> {

        if (!listing.auctionHouse.address.equals(bid.auctionHouse.address))
            return Result.failure(Error("Bid And Listing Have Different Auction Houses"))

        if (listing.mintAccount != bid.mintAccount)
            return Result.failure(Error("Bid And Listing Have Different Mints"))

        if (auctionHouse.hasAuctioneer && auctioneerAuthority == null)
            return Result.failure(Error("Auctioneer Authority Required"))

        Purchase(auctionHouse, bookkeeper, bid.buyer, listing.seller, listing.mintAccount,
            auctioneerAuthority, bid.buyerTradeState.address, listing.sellerTradeState.address,
            bid.price, bid.tokens).apply {

            val assetMetadata = FindTokenMetadataAccountOperation(connection)
                    .run(MetadataAccount.pda(listing.mintAccount).getOrThrows()).getOrThrow().data

            val creators = assetMetadata?.data?.creators?.map { it.address } ?: listOf()

            AuctionHouseBuyTransactionBuilder(auctionHouse, this, printReceipt, connection, dispatcher)
                .addCreators(creators)
                .build()
                .getOrThrow()
                .signSendAndConfirm(connection, signer)
                .getOrElse {
                    return Result.failure(it) // we cant proceed further, return the error
                }

            return Result.success(this)
        }
    }

    suspend fun cancelListing(listing: Listing, mint: PublicKey,
                              auctioneerAuthority: PublicKey? = null): Result<String> = runCatching {
        return AuctionHouseCancelTransactionBuilder(auctionHouse, listing, mint, auctioneerAuthority,
            connection, dispatcher)
            .build().getOrThrow().signSendAndConfirm(connection, signer)
    }

    suspend fun cancelBid(mint: PublicKey, bid: Bid, authority: PublicKey? = null): Result<String> = runCatching {
        return AuctionHouseCancelTransactionBuilder(auctionHouse, bid, mint, authority,
            connection, dispatcher)
            .build().getOrThrow().signSendAndConfirm(connection, signer)
    }
}