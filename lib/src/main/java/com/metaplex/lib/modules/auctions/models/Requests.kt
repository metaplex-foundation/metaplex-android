/*
 * Requests
 * Metaplex
 * 
 * Created by Funkatronics on 8/18/2022
 */

package com.metaplex.lib.modules.auctions.models

import com.metaplex.lib.experimental.jen.auctionhouse.AuctionHouseInstructions
import com.metaplex.lib.modules.auctions.AUCTIONEER_PRICE
import com.metaplex.lib.modules.auctions.SYSVAR_INSTRUCTIONS_PUBKEY
import com.metaplex.lib.modules.auctions.associatedTokenAddress
import com.metaplex.lib.programs.token_metadata.accounts.MetadataAccount
import com.solana.core.PublicKey
import com.solana.core.Sysvar
import com.solana.core.Transaction
import com.solana.programs.SystemProgram
import com.solana.programs.TokenProgram

internal data class CreateListingRequest(
    val auctionHouse: AuctionHouse,
    val seller: PublicKey, // Default: identity
    val authority: PublicKey = auctionHouse.authority, // Default: auctionHouse.authority
    val auctioneerAuthority: PublicKey? = null, // Use Auctioneer ix when provided
    val mintAccount: PublicKey, // Required for checking Metadata
    val tokenAccount: PublicKey = PublicKey.associatedTokenAddress(seller, mintAccount).address, // Default: ATA
    val price: Long = 0, // Default: 0 SOLs or tokens, ignored in Auctioneer.
    val tokens: Long = 1, // Default: token(1)
    val bookkeeper: PublicKey = seller, // Default: identity
    val printReceipt: Boolean = true, // Default: true
)

internal fun CreateListingRequest.buildTransaction(): Transaction {

    val auctionHouse = auctionHouse

    val price: Long = auctioneerAuthority?.let { AUCTIONEER_PRICE } ?: this.price

    // TODO: handle Result
    val metadata = MetadataAccount.pda(mintAccount).getOrThrows()

    val sellerTradeState =
        auctionHouse.tradeStatePda(seller, mintAccount, price, tokens, tokenAccount)

    val freeSellerTradeState =
        auctionHouse.tradeStatePda(seller, mintAccount, 0, tokens, tokenAccount)

    val programAsSigner = AuctionHouse.programAsSignerPda()

    return Transaction().apply {
        addInstruction(auctioneerAuthority?.let {
            AuctionHouseInstructions.auctioneerSell(
                wallet = seller,
                metadata = metadata,
                authority = authority,
                auctioneerAuthority = auctioneerAuthority,
                auctionHouse = auctionHouse.address,
                auctionHouseFeeAccount = auctionHouse.auctionHouseFeeAccount,
                sellerTradeState = sellerTradeState.address,
                freeSellerTradeState = freeSellerTradeState.address,
                programAsSigner = programAsSigner.address,
                tokenAccount = tokenAccount,
                ahAuctioneerPda = auctionHouse.auctioneerPda(authority),
                tokenProgram = TokenProgram.PROGRAM_ID,
                systemProgram = SystemProgram.PROGRAM_ID,
                rent = Sysvar.SYSVAR_RENT_ADDRESS,
                tradeStateBump = sellerTradeState.nonce.toUByte(),
                freeTradeStateBump = freeSellerTradeState.nonce.toUByte(),
                programAsSignerBump = programAsSigner.nonce.toUByte(),
                tokenSize = tokens.toULong()
            )
        } ?: AuctionHouseInstructions.sell(
            wallet = seller,
            metadata = metadata,
            authority = authority,
            auctionHouse = auctionHouse.address,
            auctionHouseFeeAccount = auctionHouse.auctionHouseFeeAccount,
            sellerTradeState = sellerTradeState.address,
            freeSellerTradeState = freeSellerTradeState.address,
            programAsSigner = programAsSigner.address,
            tokenAccount = tokenAccount,
            tokenProgram = TokenProgram.PROGRAM_ID,
            systemProgram = SystemProgram.PROGRAM_ID,
            rent = Sysvar.SYSVAR_RENT_ADDRESS,
            tradeStateBump = sellerTradeState.nonce.toUByte(),
            freeTradeStateBump = freeSellerTradeState.nonce.toUByte(),
            programAsSignerBump = programAsSigner.nonce.toUByte(),
            buyerPrice = price.toULong(), tokenSize = tokens.toULong(),
        ))

        // TODO: Since printBidReceipt can't deserialize createAuctioneerBuyInstruction due
        //  to a bug, don't print Auctioneer Bid receipt for the time being.
        if (printReceipt && auctioneerAuthority == null) {

            val receipt = AuctionHouse.listingReceiptPda(sellerTradeState.address)

            addInstruction(
                AuctionHouseInstructions.printListingReceipt(
                    receipt = receipt.address,
                    bookkeeper = bookkeeper,
                    instruction = PublicKey(SYSVAR_INSTRUCTIONS_PUBKEY),
                    systemProgram = SystemProgram.PROGRAM_ID,
                    rent = Sysvar.SYSVAR_RENT_ADDRESS,
                    receiptBump = receipt.nonce.toUByte()
                )
            )
        }
    }
}

internal data class CreateBidRequest(
    val auctionHouse: AuctionHouse,
    val mintAccount: PublicKey, // Required for checking Metadata
    val buyer: PublicKey, // Default: identity
    val authority: PublicKey = auctionHouse.authority, // Default: auctionHouse.authority
    val auctioneerAuthority: PublicKey? = null, // Use Auctioneer ix when provided
    val seller: PublicKey? = null, // Default: null (i.e. public bid unless token account is provided)
    val tokenAccount: PublicKey? = null, // Default: null (i.e. public bid unless seller is provided).
    val price: Long = 0, // Default: 0 SOLs or tokens.
    val tokens: Long = 1, // Default: token(1)
    val bookkeeper: PublicKey = buyer, // Default: identity
    val printReceipt: Boolean = true, // Default: true
)

internal fun CreateBidRequest.buildTransaction(): Transaction {

    val auctionHouse = auctionHouse

    // TODO: handle Result
    val metadata = MetadataAccount.pda(mintAccount).getOrThrows()

    val tokenAccount = tokenAccount ?: seller?.let {
        PublicKey.associatedTokenAddress(seller, mintAccount).address
    }

    val buyerTokenAccount = PublicKey.associatedTokenAddress(buyer, mintAccount).address

    val paymentAccount: PublicKey = if (auctionHouse.isNative) buyer else
        PublicKey.associatedTokenAddress(buyer, auctionHouse.treasuryMint).address

    val escrowPayment = auctionHouse.buyerEscrowPda(buyer)

    val buyerTradeState =
        auctionHouse.tradeStatePda(buyer, mintAccount, price, tokens, tokenAccount)

    return Transaction().apply {
        addInstruction(auctioneerAuthority?.let {
            tokenAccount?.let{
                AuctionHouseInstructions.auctioneerBuy(
                    wallet = buyer,
                    paymentAccount = paymentAccount,
                    transferAuthority = buyer,
                    treasuryMint = auctionHouse.treasuryMint,
                    metadata = metadata,
                    escrowPaymentAccount = escrowPayment.address,
                    authority = authority,
                    auctioneerAuthority = auctioneerAuthority,
                    auctionHouse = auctionHouse.address,
                    auctionHouseFeeAccount = auctionHouse.auctionHouseFeeAccount,
                    buyerTradeState = buyerTradeState.address,
                    tokenAccount = tokenAccount,
                    ahAuctioneerPda = auctionHouse.auctioneerPda(authority),
                    tokenProgram = TokenProgram.PROGRAM_ID,
                    systemProgram = SystemProgram.PROGRAM_ID,
                    rent = Sysvar.SYSVAR_RENT_ADDRESS,
                    tradeStateBump = buyerTradeState.nonce.toUByte(),
                    escrowPaymentBump = escrowPayment.nonce.toUByte(),
                    buyerPrice = price.toULong(), tokenSize = tokens.toULong()
                )
            } ?: AuctionHouseInstructions.auctioneerPublicBuy(
                wallet = buyer,
                paymentAccount = paymentAccount,
                transferAuthority = buyer,
                treasuryMint = auctionHouse.treasuryMint,
                metadata = metadata,
                escrowPaymentAccount = escrowPayment.address,
                authority = authority,
                auctioneerAuthority = auctioneerAuthority,
                auctionHouse = auctionHouse.address,
                auctionHouseFeeAccount = auctionHouse.auctionHouseFeeAccount,
                buyerTradeState = buyerTradeState.address,
                tokenAccount = buyerTokenAccount,
                ahAuctioneerPda = auctionHouse.auctioneerPda(authority),
                tokenProgram = TokenProgram.PROGRAM_ID,
                systemProgram = SystemProgram.PROGRAM_ID,
                rent = Sysvar.SYSVAR_RENT_ADDRESS,
                tradeStateBump = buyerTradeState.nonce.toUByte(),
                escrowPaymentBump = escrowPayment.nonce.toUByte(),
                buyerPrice = price.toULong(), tokenSize = tokens.toULong()
            )
        } ?: tokenAccount?.let {
            AuctionHouseInstructions.buy(
                wallet = buyer,
                paymentAccount = paymentAccount,
                transferAuthority = buyer,
                treasuryMint = auctionHouse.treasuryMint,
                metadata = metadata,
                escrowPaymentAccount = escrowPayment.address,
                authority = authority,
                auctionHouse = AuctionHouse.pda(auctionHouse.creator, auctionHouse.treasuryMint),
                auctionHouseFeeAccount = auctionHouse.auctionHouseFeeAccount,
                buyerTradeState = buyerTradeState.address,
                tokenAccount = tokenAccount,
                tokenProgram = TokenProgram.PROGRAM_ID,
                systemProgram = SystemProgram.PROGRAM_ID,
                rent = Sysvar.SYSVAR_RENT_ADDRESS,
                tradeStateBump = buyerTradeState.nonce.toUByte(),
                escrowPaymentBump = escrowPayment.nonce.toUByte(),
                buyerPrice = price.toULong(), tokenSize = tokens.toULong()
            )
        } ?:
        AuctionHouseInstructions.publicBuy(
            wallet = buyer,
            paymentAccount = paymentAccount,
            transferAuthority = buyer,
            treasuryMint = auctionHouse.treasuryMint,
            metadata = metadata,
            escrowPaymentAccount = escrowPayment.address,
            authority = authority,
            auctionHouse = auctionHouse.address,
            auctionHouseFeeAccount = auctionHouse.auctionHouseFeeAccount,
            buyerTradeState = buyerTradeState.address,
            tokenAccount = buyerTokenAccount,
            tokenProgram = TokenProgram.PROGRAM_ID,
            systemProgram = SystemProgram.PROGRAM_ID,
            rent = Sysvar.SYSVAR_RENT_ADDRESS,
            tradeStateBump = buyerTradeState.nonce.toUByte(),
            escrowPaymentBump = escrowPayment.nonce.toUByte(),
            buyerPrice = price.toULong(), tokenSize = tokens.toULong()
        ))

        // TODO: Since printBidReceipt can't deserialize createAuctioneerBuyInstruction due
        //  to a bug, don't print Auctioneer Bid receipt for the time being.
        if (printReceipt && auctioneerAuthority == null) {

            val receipt = AuctionHouse.bidReceiptPda(buyerTradeState.address)

            addInstruction(
                AuctionHouseInstructions.printBidReceipt(
                    receipt = receipt.address,
                    bookkeeper = bookkeeper,
                    instruction = PublicKey(SYSVAR_INSTRUCTIONS_PUBKEY),
                    systemProgram = SystemProgram.PROGRAM_ID,
                    rent = Sysvar.SYSVAR_RENT_ADDRESS,
                    receiptBump = receipt.nonce.toUByte()
                )
            )
        }
    }
}