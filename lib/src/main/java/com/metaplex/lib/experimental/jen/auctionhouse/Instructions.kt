//
// Instructions
// Metaplex
//
// This code was generated locally by Funkatronics on 2022-08-11
//
package com.metaplex.lib.experimental.jen.auctionhouse

import com.metaplex.lib.serialization.format.Borsh
import com.solana.core.AccountMeta
import com.solana.core.PublicKey
import com.solana.core.TransactionInstruction
import kotlinx.serialization.Serializable

object AuctionHouseInstructions {
    fun withdrawFromFee(
        authority: PublicKey,
        feeWithdrawalDestination: PublicKey,
        auctionHouseFeeAccount: PublicKey,
        auctionHouse: PublicKey,
        systemProgram: PublicKey,
        amount: ULong
    ): TransactionInstruction =
        TransactionInstruction(
            PublicKey("hausS13jsjafwWwGqZTUQRmWyvyxn9EQpqMwV1PBBmk"),
            listOf(
                AccountMeta(authority, true, false), AccountMeta(
                    feeWithdrawalDestination, false,
                    true
                ), AccountMeta(auctionHouseFeeAccount, false, true), AccountMeta(
                    auctionHouse,
                    false, true
                ), AccountMeta(systemProgram, false, false)
            ),
            Borsh.encodeToByteArray(
                Args_withdrawFromFee.serializer(),
                Args_withdrawFromFee(amount)
            )
        )

    fun withdrawFromTreasury(
        treasuryMint: PublicKey,
        authority: PublicKey,
        treasuryWithdrawalDestination: PublicKey,
        auctionHouseTreasury: PublicKey,
        auctionHouse: PublicKey,
        tokenProgram: PublicKey,
        systemProgram: PublicKey,
        amount: ULong
    ): TransactionInstruction =
        TransactionInstruction(
            PublicKey("hausS13jsjafwWwGqZTUQRmWyvyxn9EQpqMwV1PBBmk"),
            listOf(
                AccountMeta(treasuryMint, false, false),
                AccountMeta(authority, true, false),
                AccountMeta(treasuryWithdrawalDestination, false, true),
                AccountMeta(auctionHouseTreasury, false, true),
                AccountMeta(auctionHouse, false, true),
                AccountMeta(tokenProgram, false, false),
                AccountMeta(systemProgram, false, false)
            ),
            Borsh.encodeToByteArray(
                Args_withdrawFromTreasury.serializer(),
                Args_withdrawFromTreasury(amount)
            )
        )

    fun updateAuctionHouse(
        treasuryMint: PublicKey,
        payer: PublicKey,
        authority: PublicKey,
        newAuthority: PublicKey,
        feeWithdrawalDestination: PublicKey,
        treasuryWithdrawalDestination: PublicKey,
        treasuryWithdrawalDestinationOwner: PublicKey,
        auctionHouse: PublicKey,
        tokenProgram: PublicKey,
        systemProgram: PublicKey,
        ataProgram: PublicKey,
        rent: PublicKey,
        sellerFeeBasisPoints: UShort?,
        requiresSignOff: Boolean?,
        canChangeSalePrice: Boolean?
    ): TransactionInstruction =
        TransactionInstruction(
            PublicKey("hausS13jsjafwWwGqZTUQRmWyvyxn9EQpqMwV1PBBmk"),
            listOf(
                AccountMeta(treasuryMint, false, false), AccountMeta(payer, true, false),
                AccountMeta(authority, true, false), AccountMeta(newAuthority, false, false),
                AccountMeta(feeWithdrawalDestination, false, true),
                AccountMeta(treasuryWithdrawalDestination, false, true),
                AccountMeta(treasuryWithdrawalDestinationOwner, false, false), AccountMeta(
                    auctionHouse,
                    false, true
                ), AccountMeta(tokenProgram, false, false), AccountMeta(
                    systemProgram, false,
                    false
                ), AccountMeta(ataProgram, false, false), AccountMeta(rent, false, false)
            ),
            Borsh.encodeToByteArray(
                Args_updateAuctionHouse.serializer(),
                Args_updateAuctionHouse(sellerFeeBasisPoints, requiresSignOff, canChangeSalePrice)
            )
        )

    fun createAuctionHouse(
        treasuryMint: PublicKey,
        payer: PublicKey,
        authority: PublicKey,
        feeWithdrawalDestination: PublicKey,
        treasuryWithdrawalDestination: PublicKey,
        treasuryWithdrawalDestinationOwner: PublicKey,
        auctionHouse: PublicKey,
        auctionHouseFeeAccount: PublicKey,
        auctionHouseTreasury: PublicKey,
        tokenProgram: PublicKey,
        systemProgram: PublicKey,
        ataProgram: PublicKey,
        rent: PublicKey,
        bump: UByte,
        feePayerBump: UByte,
        treasuryBump: UByte,
        sellerFeeBasisPoints: UShort,
        requiresSignOff: Boolean,
        canChangeSalePrice: Boolean
    ): TransactionInstruction =
        TransactionInstruction(
            PublicKey("hausS13jsjafwWwGqZTUQRmWyvyxn9EQpqMwV1PBBmk"),
            listOf(
                AccountMeta(treasuryMint, false, false),
                AccountMeta(payer, true, true),
                AccountMeta(authority, false, false),
                AccountMeta(
                    feeWithdrawalDestination, false,
                    true
                ),
                AccountMeta(treasuryWithdrawalDestination, false, true),
                AccountMeta(treasuryWithdrawalDestinationOwner, false, false),
                AccountMeta(
                    auctionHouse,
                    false, true
                ),
                AccountMeta(auctionHouseFeeAccount, false, true),
                AccountMeta(auctionHouseTreasury, false, true),
                AccountMeta(tokenProgram, false, false),
                AccountMeta(systemProgram, false, false),
                AccountMeta(ataProgram, false, false),
                AccountMeta(rent, false, false)
            ),
            Borsh.encodeToByteArray(
                Args_createAuctionHouse.serializer(),
                Args_createAuctionHouse(
                    bump, feePayerBump, treasuryBump, sellerFeeBasisPoints,
                    requiresSignOff, canChangeSalePrice
                )
            )
        )

    fun buy(
        wallet: PublicKey,
        paymentAccount: PublicKey,
        transferAuthority: PublicKey,
        treasuryMint: PublicKey,
        tokenAccount: PublicKey,
        metadata: PublicKey,
        escrowPaymentAccount: PublicKey,
        authority: PublicKey,
        auctionHouse: PublicKey,
        auctionHouseFeeAccount: PublicKey,
        buyerTradeState: PublicKey,
        tokenProgram: PublicKey,
        systemProgram: PublicKey,
        rent: PublicKey,
        tradeStateBump: UByte,
        escrowPaymentBump: UByte,
        buyerPrice: ULong,
        tokenSize: ULong
    ): TransactionInstruction =
        TransactionInstruction(
            PublicKey("hausS13jsjafwWwGqZTUQRmWyvyxn9EQpqMwV1PBBmk"),
            listOf(
                AccountMeta(wallet, true, false),
                AccountMeta(paymentAccount, false, true),
                AccountMeta(transferAuthority, false, false),
                AccountMeta(treasuryMint, false, false),
                AccountMeta(tokenAccount, false, false),
                AccountMeta(metadata, false, false),
                AccountMeta(escrowPaymentAccount, false, true),
                AccountMeta(authority, false, false),
                AccountMeta(auctionHouse, false, false),
                AccountMeta(
                    auctionHouseFeeAccount, false,
                    true
                ),
                AccountMeta(buyerTradeState, false, true),
                AccountMeta(
                    tokenProgram, false,
                    false
                ),
                AccountMeta(systemProgram, false, false),
                AccountMeta(rent, false, false)
            ),
            Borsh.encodeToByteArray(
                Args_buy.serializer(), Args_buy(
                    tradeStateBump,
                    escrowPaymentBump, buyerPrice, tokenSize
                )
            )
        )

    fun auctioneerBuy(
        wallet: PublicKey,
        paymentAccount: PublicKey,
        transferAuthority: PublicKey,
        treasuryMint: PublicKey,
        tokenAccount: PublicKey,
        metadata: PublicKey,
        escrowPaymentAccount: PublicKey,
        authority: PublicKey,
        auctioneerAuthority: PublicKey,
        auctionHouse: PublicKey,
        auctionHouseFeeAccount: PublicKey,
        buyerTradeState: PublicKey,
        ahAuctioneerPda: PublicKey,
        tokenProgram: PublicKey,
        systemProgram: PublicKey,
        rent: PublicKey,
        tradeStateBump: UByte,
        escrowPaymentBump: UByte,
        buyerPrice: ULong,
        tokenSize: ULong
    ): TransactionInstruction =
        TransactionInstruction(
            PublicKey("hausS13jsjafwWwGqZTUQRmWyvyxn9EQpqMwV1PBBmk"),
            listOf(
                AccountMeta(wallet, true, false),
                AccountMeta(paymentAccount, false, true),
                AccountMeta(transferAuthority, false, false),
                AccountMeta(treasuryMint, false, false),
                AccountMeta(tokenAccount, false, false),
                AccountMeta(metadata, false, false),
                AccountMeta(escrowPaymentAccount, false, true),
                AccountMeta(authority, false, false),
                AccountMeta(auctioneerAuthority, true, false),
                AccountMeta(auctionHouse, false, false),
                AccountMeta(auctionHouseFeeAccount, false, true),
                AccountMeta(
                    buyerTradeState, false,
                    true
                ),
                AccountMeta(ahAuctioneerPda, false, false),
                AccountMeta(
                    tokenProgram, false,
                    false
                ),
                AccountMeta(systemProgram, false, false),
                AccountMeta(rent, false, false)
            ),
            Borsh.encodeToByteArray(
                Args_auctioneerBuy.serializer(),
                Args_auctioneerBuy(tradeStateBump, escrowPaymentBump, buyerPrice, tokenSize)
            )
        )

    fun publicBuy(
        wallet: PublicKey,
        paymentAccount: PublicKey,
        transferAuthority: PublicKey,
        treasuryMint: PublicKey,
        tokenAccount: PublicKey,
        metadata: PublicKey,
        escrowPaymentAccount: PublicKey,
        authority: PublicKey,
        auctionHouse: PublicKey,
        auctionHouseFeeAccount: PublicKey,
        buyerTradeState: PublicKey,
        tokenProgram: PublicKey,
        systemProgram: PublicKey,
        rent: PublicKey,
        tradeStateBump: UByte,
        escrowPaymentBump: UByte,
        buyerPrice: ULong,
        tokenSize: ULong
    ): TransactionInstruction =
        TransactionInstruction(
            PublicKey("hausS13jsjafwWwGqZTUQRmWyvyxn9EQpqMwV1PBBmk"),
            listOf(
                AccountMeta(wallet, true, false),
                AccountMeta(paymentAccount, false, true),
                AccountMeta(transferAuthority, false, false),
                AccountMeta(treasuryMint, false, false),
                AccountMeta(tokenAccount, false, false),
                AccountMeta(metadata, false, false),
                AccountMeta(escrowPaymentAccount, false, true),
                AccountMeta(authority, false, false),
                AccountMeta(auctionHouse, false, false),
                AccountMeta(
                    auctionHouseFeeAccount, false,
                    true
                ),
                AccountMeta(buyerTradeState, false, true),
                AccountMeta(
                    tokenProgram, false,
                    false
                ),
                AccountMeta(systemProgram, false, false),
                AccountMeta(rent, false, false)
            ),
            Borsh.encodeToByteArray(
                Args_publicBuy.serializer(), Args_publicBuy(
                    tradeStateBump,
                    escrowPaymentBump, buyerPrice, tokenSize
                )
            )
        )

    fun auctioneerPublicBuy(
        wallet: PublicKey,
        paymentAccount: PublicKey,
        transferAuthority: PublicKey,
        treasuryMint: PublicKey,
        tokenAccount: PublicKey,
        metadata: PublicKey,
        escrowPaymentAccount: PublicKey,
        authority: PublicKey,
        auctioneerAuthority: PublicKey,
        auctionHouse: PublicKey,
        auctionHouseFeeAccount: PublicKey,
        buyerTradeState: PublicKey,
        ahAuctioneerPda: PublicKey,
        tokenProgram: PublicKey,
        systemProgram: PublicKey,
        rent: PublicKey,
        tradeStateBump: UByte,
        escrowPaymentBump: UByte,
        buyerPrice: ULong,
        tokenSize: ULong
    ): TransactionInstruction =
        TransactionInstruction(
            PublicKey("hausS13jsjafwWwGqZTUQRmWyvyxn9EQpqMwV1PBBmk"),
            listOf(
                AccountMeta(wallet, true, false),
                AccountMeta(paymentAccount, false, true),
                AccountMeta(transferAuthority, false, false),
                AccountMeta(treasuryMint, false, false),
                AccountMeta(tokenAccount, false, false),
                AccountMeta(metadata, false, false),
                AccountMeta(escrowPaymentAccount, false, true),
                AccountMeta(authority, false, false),
                AccountMeta(auctioneerAuthority, true, false),
                AccountMeta(auctionHouse, false, false),
                AccountMeta(auctionHouseFeeAccount, false, true),
                AccountMeta(
                    buyerTradeState, false,
                    true
                ),
                AccountMeta(ahAuctioneerPda, false, false),
                AccountMeta(
                    tokenProgram, false,
                    false
                ),
                AccountMeta(systemProgram, false, false),
                AccountMeta(rent, false, false)
            ),
            Borsh.encodeToByteArray(
                Args_auctioneerPublicBuy.serializer(),
                Args_auctioneerPublicBuy(tradeStateBump, escrowPaymentBump, buyerPrice, tokenSize)
            )
        )

    fun cancel(
        wallet: PublicKey,
        tokenAccount: PublicKey,
        tokenMint: PublicKey,
        authority: PublicKey,
        auctionHouse: PublicKey,
        auctionHouseFeeAccount: PublicKey,
        tradeState: PublicKey,
        tokenProgram: PublicKey,
        buyerPrice: ULong,
        tokenSize: ULong
    ): TransactionInstruction =
        TransactionInstruction(
            PublicKey("hausS13jsjafwWwGqZTUQRmWyvyxn9EQpqMwV1PBBmk"),
            listOf(
                AccountMeta(wallet, false, true), AccountMeta(tokenAccount, false, true),
                AccountMeta(tokenMint, false, false), AccountMeta(authority, false, false),
                AccountMeta(auctionHouse, false, false), AccountMeta(
                    auctionHouseFeeAccount, false,
                    true
                ), AccountMeta(tradeState, false, true), AccountMeta(tokenProgram, false, false)
            ),
            Borsh.encodeToByteArray(Args_cancel.serializer(), Args_cancel(buyerPrice, tokenSize))
        )

    fun auctioneerCancel(
        wallet: PublicKey,
        tokenAccount: PublicKey,
        tokenMint: PublicKey,
        authority: PublicKey,
        auctioneerAuthority: PublicKey,
        auctionHouse: PublicKey,
        auctionHouseFeeAccount: PublicKey,
        tradeState: PublicKey,
        ahAuctioneerPda: PublicKey,
        tokenProgram: PublicKey,
        buyerPrice: ULong,
        tokenSize: ULong
    ): TransactionInstruction =
        TransactionInstruction(
            PublicKey("hausS13jsjafwWwGqZTUQRmWyvyxn9EQpqMwV1PBBmk"),
            listOf(
                AccountMeta(wallet, false, true),
                AccountMeta(tokenAccount, false, true),
                AccountMeta(tokenMint, false, false),
                AccountMeta(authority, false, false),
                AccountMeta(auctioneerAuthority, true, false),
                AccountMeta(auctionHouse, false, false),
                AccountMeta(auctionHouseFeeAccount, false, true),
                AccountMeta(tradeState, false, true),
                AccountMeta(ahAuctioneerPda, false, false),
                AccountMeta(tokenProgram, false, false)
            ),
            Borsh.encodeToByteArray(
                Args_auctioneerCancel.serializer(),
                Args_auctioneerCancel(buyerPrice, tokenSize)
            )
        )

    fun deposit(
        wallet: PublicKey,
        paymentAccount: PublicKey,
        transferAuthority: PublicKey,
        escrowPaymentAccount: PublicKey,
        treasuryMint: PublicKey,
        authority: PublicKey,
        auctionHouse: PublicKey,
        auctionHouseFeeAccount: PublicKey,
        tokenProgram: PublicKey,
        systemProgram: PublicKey,
        rent: PublicKey,
        escrowPaymentBump: UByte,
        amount: ULong
    ): TransactionInstruction =
        TransactionInstruction(
            PublicKey("hausS13jsjafwWwGqZTUQRmWyvyxn9EQpqMwV1PBBmk"),
            listOf(
                AccountMeta(wallet, true, false), AccountMeta(paymentAccount, false, true),
                AccountMeta(transferAuthority, false, false), AccountMeta(
                    escrowPaymentAccount, false,
                    true
                ), AccountMeta(treasuryMint, false, false), AccountMeta(authority, false, false),
                AccountMeta(auctionHouse, false, false), AccountMeta(
                    auctionHouseFeeAccount, false,
                    true
                ), AccountMeta(tokenProgram, false, false), AccountMeta(
                    systemProgram, false,
                    false
                ), AccountMeta(rent, false, false)
            ),
            Borsh.encodeToByteArray(
                Args_deposit.serializer(), Args_deposit(
                    escrowPaymentBump,
                    amount
                )
            )
        )

    fun auctioneerDeposit(
        wallet: PublicKey,
        paymentAccount: PublicKey,
        transferAuthority: PublicKey,
        escrowPaymentAccount: PublicKey,
        treasuryMint: PublicKey,
        authority: PublicKey,
        auctioneerAuthority: PublicKey,
        auctionHouse: PublicKey,
        auctionHouseFeeAccount: PublicKey,
        ahAuctioneerPda: PublicKey,
        tokenProgram: PublicKey,
        systemProgram: PublicKey,
        rent: PublicKey,
        escrowPaymentBump: UByte,
        amount: ULong
    ): TransactionInstruction =
        TransactionInstruction(
            PublicKey("hausS13jsjafwWwGqZTUQRmWyvyxn9EQpqMwV1PBBmk"),
            listOf(
                AccountMeta(wallet, true, false),
                AccountMeta(paymentAccount, false, true),
                AccountMeta(transferAuthority, false, false),
                AccountMeta(
                    escrowPaymentAccount, false,
                    true
                ),
                AccountMeta(treasuryMint, false, false),
                AccountMeta(authority, false, false),
                AccountMeta(auctioneerAuthority, true, false),
                AccountMeta(auctionHouse, false, false),
                AccountMeta(auctionHouseFeeAccount, false, true),
                AccountMeta(
                    ahAuctioneerPda, false,
                    false
                ),
                AccountMeta(tokenProgram, false, false),
                AccountMeta(
                    systemProgram, false,
                    false
                ),
                AccountMeta(rent, false, false)
            ),
            Borsh.encodeToByteArray(
                Args_auctioneerDeposit.serializer(),
                Args_auctioneerDeposit(escrowPaymentBump, amount)
            )
        )

    fun executeSale(
        buyer: PublicKey,
        seller: PublicKey,
        tokenAccount: PublicKey,
        tokenMint: PublicKey,
        metadata: PublicKey,
        treasuryMint: PublicKey,
        escrowPaymentAccount: PublicKey,
        sellerPaymentReceiptAccount: PublicKey,
        buyerReceiptTokenAccount: PublicKey,
        authority: PublicKey,
        auctionHouse: PublicKey,
        auctionHouseFeeAccount: PublicKey,
        auctionHouseTreasury: PublicKey,
        buyerTradeState: PublicKey,
        sellerTradeState: PublicKey,
        freeTradeState: PublicKey,
        tokenProgram: PublicKey,
        systemProgram: PublicKey,
        ataProgram: PublicKey,
        programAsSigner: PublicKey,
        rent: PublicKey,
        escrowPaymentBump: UByte,
        freeTradeStateBump: UByte,
        programAsSignerBump: UByte,
        buyerPrice: ULong,
        tokenSize: ULong
    ): TransactionInstruction =
        TransactionInstruction(
            PublicKey("hausS13jsjafwWwGqZTUQRmWyvyxn9EQpqMwV1PBBmk"),
            listOf(
                AccountMeta(buyer, false, true), AccountMeta(seller, false, true),
                AccountMeta(tokenAccount, false, true), AccountMeta(tokenMint, false, false),
                AccountMeta(metadata, false, false), AccountMeta(treasuryMint, false, false),
                AccountMeta(escrowPaymentAccount, false, true), AccountMeta(
                    sellerPaymentReceiptAccount,
                    false, true
                ), AccountMeta(buyerReceiptTokenAccount, false, true), AccountMeta(
                    authority,
                    false, false
                ), AccountMeta(auctionHouse, false, false),
                AccountMeta(auctionHouseFeeAccount, false, true), AccountMeta(
                    auctionHouseTreasury,
                    false, true
                ), AccountMeta(buyerTradeState, false, true), AccountMeta(
                    sellerTradeState,
                    false, true
                ), AccountMeta(freeTradeState, false, true), AccountMeta(
                    tokenProgram, false,
                    false
                ), AccountMeta(systemProgram, false, false), AccountMeta(ataProgram, false, false),
                AccountMeta(programAsSigner, false, false), AccountMeta(rent, false, false)
            ),
            Borsh.encodeToByteArray(
                Args_executeSale.serializer(),
                Args_executeSale(
                    escrowPaymentBump, freeTradeStateBump, programAsSignerBump, buyerPrice,
                    tokenSize
                )
            )
        )

    fun executePartialSale(
        buyer: PublicKey,
        seller: PublicKey,
        tokenAccount: PublicKey,
        tokenMint: PublicKey,
        metadata: PublicKey,
        treasuryMint: PublicKey,
        escrowPaymentAccount: PublicKey,
        sellerPaymentReceiptAccount: PublicKey,
        buyerReceiptTokenAccount: PublicKey,
        authority: PublicKey,
        auctionHouse: PublicKey,
        auctionHouseFeeAccount: PublicKey,
        auctionHouseTreasury: PublicKey,
        buyerTradeState: PublicKey,
        sellerTradeState: PublicKey,
        freeTradeState: PublicKey,
        tokenProgram: PublicKey,
        systemProgram: PublicKey,
        ataProgram: PublicKey,
        programAsSigner: PublicKey,
        rent: PublicKey,
        escrowPaymentBump: UByte,
        freeTradeStateBump: UByte,
        programAsSignerBump: UByte,
        buyerPrice: ULong,
        tokenSize: ULong,
        partialOrderSize: ULong?,
        partialOrderPrice: ULong?
    ): TransactionInstruction =
        TransactionInstruction(
            PublicKey("hausS13jsjafwWwGqZTUQRmWyvyxn9EQpqMwV1PBBmk"),
            listOf(
                AccountMeta(buyer, false, true), AccountMeta(seller, false, true),
                AccountMeta(tokenAccount, false, true), AccountMeta(tokenMint, false, false),
                AccountMeta(metadata, false, false), AccountMeta(treasuryMint, false, false),
                AccountMeta(escrowPaymentAccount, false, true), AccountMeta(
                    sellerPaymentReceiptAccount,
                    false, true
                ), AccountMeta(buyerReceiptTokenAccount, false, true), AccountMeta(
                    authority,
                    false, false
                ), AccountMeta(auctionHouse, false, false),
                AccountMeta(auctionHouseFeeAccount, false, true), AccountMeta(
                    auctionHouseTreasury,
                    false, true
                ), AccountMeta(buyerTradeState, false, true), AccountMeta(
                    sellerTradeState,
                    false, true
                ), AccountMeta(freeTradeState, false, true), AccountMeta(
                    tokenProgram, false,
                    false
                ), AccountMeta(systemProgram, false, false), AccountMeta(ataProgram, false, false),
                AccountMeta(programAsSigner, false, false), AccountMeta(rent, false, false)
            ),
            Borsh.encodeToByteArray(
                Args_executePartialSale.serializer(),
                Args_executePartialSale(
                    escrowPaymentBump, freeTradeStateBump, programAsSignerBump,
                    buyerPrice, tokenSize, partialOrderSize, partialOrderPrice
                )
            )
        )

    fun auctioneerExecuteSale(
        buyer: PublicKey,
        seller: PublicKey,
        tokenAccount: PublicKey,
        tokenMint: PublicKey,
        metadata: PublicKey,
        treasuryMint: PublicKey,
        escrowPaymentAccount: PublicKey,
        sellerPaymentReceiptAccount: PublicKey,
        buyerReceiptTokenAccount: PublicKey,
        authority: PublicKey,
        auctioneerAuthority: PublicKey,
        auctionHouse: PublicKey,
        auctionHouseFeeAccount: PublicKey,
        auctionHouseTreasury: PublicKey,
        buyerTradeState: PublicKey,
        sellerTradeState: PublicKey,
        freeTradeState: PublicKey,
        ahAuctioneerPda: PublicKey,
        tokenProgram: PublicKey,
        systemProgram: PublicKey,
        ataProgram: PublicKey,
        programAsSigner: PublicKey,
        rent: PublicKey,
        escrowPaymentBump: UByte,
        freeTradeStateBump: UByte,
        programAsSignerBump: UByte,
        buyerPrice: ULong,
        tokenSize: ULong
    ): TransactionInstruction =
        TransactionInstruction(
            PublicKey("hausS13jsjafwWwGqZTUQRmWyvyxn9EQpqMwV1PBBmk"),
            listOf(
                AccountMeta(buyer, false, true), AccountMeta(seller, false, true),
                AccountMeta(tokenAccount, false, true), AccountMeta(tokenMint, false, false),
                AccountMeta(metadata, false, false), AccountMeta(treasuryMint, false, false),
                AccountMeta(escrowPaymentAccount, false, true), AccountMeta(
                    sellerPaymentReceiptAccount,
                    false, true
                ), AccountMeta(buyerReceiptTokenAccount, false, true), AccountMeta(
                    authority,
                    false, false
                ), AccountMeta(auctioneerAuthority, true, false), AccountMeta(
                    auctionHouse,
                    false, false
                ), AccountMeta(auctionHouseFeeAccount, false, true),
                AccountMeta(auctionHouseTreasury, false, true), AccountMeta(
                    buyerTradeState, false,
                    true
                ), AccountMeta(sellerTradeState, false, true), AccountMeta(
                    freeTradeState, false,
                    true
                ), AccountMeta(ahAuctioneerPda, false, false), AccountMeta(
                    tokenProgram, false,
                    false
                ), AccountMeta(systemProgram, false, false), AccountMeta(ataProgram, false, false),
                AccountMeta(programAsSigner, false, false), AccountMeta(rent, false, false)
            ),
            Borsh.encodeToByteArray(
                Args_auctioneerExecuteSale.serializer(),
                Args_auctioneerExecuteSale(
                    escrowPaymentBump, freeTradeStateBump, programAsSignerBump,
                    buyerPrice, tokenSize
                )
            )
        )

    fun auctioneerExecutePartialSale(
        buyer: PublicKey,
        seller: PublicKey,
        tokenAccount: PublicKey,
        tokenMint: PublicKey,
        metadata: PublicKey,
        treasuryMint: PublicKey,
        escrowPaymentAccount: PublicKey,
        sellerPaymentReceiptAccount: PublicKey,
        buyerReceiptTokenAccount: PublicKey,
        authority: PublicKey,
        auctioneerAuthority: PublicKey,
        auctionHouse: PublicKey,
        auctionHouseFeeAccount: PublicKey,
        auctionHouseTreasury: PublicKey,
        buyerTradeState: PublicKey,
        sellerTradeState: PublicKey,
        freeTradeState: PublicKey,
        ahAuctioneerPda: PublicKey,
        tokenProgram: PublicKey,
        systemProgram: PublicKey,
        ataProgram: PublicKey,
        programAsSigner: PublicKey,
        rent: PublicKey,
        escrowPaymentBump: UByte,
        freeTradeStateBump: UByte,
        programAsSignerBump: UByte,
        buyerPrice: ULong,
        tokenSize: ULong,
        partialOrderSize: ULong?,
        partialOrderPrice: ULong?
    ): TransactionInstruction =
        TransactionInstruction(
            PublicKey("hausS13jsjafwWwGqZTUQRmWyvyxn9EQpqMwV1PBBmk"),
            listOf(
                AccountMeta(buyer, false, true), AccountMeta(seller, false, true),
                AccountMeta(tokenAccount, false, true), AccountMeta(tokenMint, false, false),
                AccountMeta(metadata, false, false), AccountMeta(treasuryMint, false, false),
                AccountMeta(escrowPaymentAccount, false, true), AccountMeta(
                    sellerPaymentReceiptAccount,
                    false, true
                ), AccountMeta(buyerReceiptTokenAccount, false, true), AccountMeta(
                    authority,
                    false, false
                ), AccountMeta(auctioneerAuthority, true, false), AccountMeta(
                    auctionHouse,
                    false, false
                ), AccountMeta(auctionHouseFeeAccount, false, true),
                AccountMeta(auctionHouseTreasury, false, true), AccountMeta(
                    buyerTradeState, false,
                    true
                ), AccountMeta(sellerTradeState, false, true), AccountMeta(
                    freeTradeState, false,
                    true
                ), AccountMeta(ahAuctioneerPda, false, false), AccountMeta(
                    tokenProgram, false,
                    false
                ), AccountMeta(systemProgram, false, false), AccountMeta(ataProgram, false, false),
                AccountMeta(programAsSigner, false, false), AccountMeta(rent, false, false)
            ),
            Borsh.encodeToByteArray(
                Args_auctioneerExecutePartialSale.serializer(),
                Args_auctioneerExecutePartialSale(
                    escrowPaymentBump, freeTradeStateBump,
                    programAsSignerBump, buyerPrice, tokenSize, partialOrderSize, partialOrderPrice
                )
            )
        )

    fun sell(
        wallet: PublicKey,
        tokenAccount: PublicKey,
        metadata: PublicKey,
        authority: PublicKey,
        auctionHouse: PublicKey,
        auctionHouseFeeAccount: PublicKey,
        sellerTradeState: PublicKey,
        freeSellerTradeState: PublicKey,
        tokenProgram: PublicKey,
        systemProgram: PublicKey,
        programAsSigner: PublicKey,
        rent: PublicKey,
        tradeStateBump: UByte,
        freeTradeStateBump: UByte,
        programAsSignerBump: UByte,
        buyerPrice: ULong,
        tokenSize: ULong
    ): TransactionInstruction =
        TransactionInstruction(
            PublicKey("hausS13jsjafwWwGqZTUQRmWyvyxn9EQpqMwV1PBBmk"),
            listOf(
                AccountMeta(wallet, false, false), AccountMeta(tokenAccount, false, true),
                AccountMeta(metadata, false, false), AccountMeta(authority, false, false),
                AccountMeta(auctionHouse, false, false), AccountMeta(
                    auctionHouseFeeAccount, false,
                    true
                ), AccountMeta(sellerTradeState, false, true), AccountMeta(
                    freeSellerTradeState,
                    false, true
                ), AccountMeta(tokenProgram, false, false), AccountMeta(
                    systemProgram, false,
                    false
                ), AccountMeta(programAsSigner, false, false), AccountMeta(rent, false, false)
            ),
            Borsh.encodeToByteArray(
                Args_sell.serializer(), Args_sell(
                    tradeStateBump,
                    freeTradeStateBump, programAsSignerBump, buyerPrice, tokenSize
                )
            )
        )

    fun auctioneerSell(
        wallet: PublicKey,
        tokenAccount: PublicKey,
        metadata: PublicKey,
        authority: PublicKey,
        auctioneerAuthority: PublicKey,
        auctionHouse: PublicKey,
        auctionHouseFeeAccount: PublicKey,
        sellerTradeState: PublicKey,
        freeSellerTradeState: PublicKey,
        ahAuctioneerPda: PublicKey,
        programAsSigner: PublicKey,
        tokenProgram: PublicKey,
        systemProgram: PublicKey,
        rent: PublicKey,
        tradeStateBump: UByte,
        freeTradeStateBump: UByte,
        programAsSignerBump: UByte,
        tokenSize: ULong
    ): TransactionInstruction =
        TransactionInstruction(
            PublicKey("hausS13jsjafwWwGqZTUQRmWyvyxn9EQpqMwV1PBBmk"),
            listOf(
                AccountMeta(wallet, false, true),
                AccountMeta(tokenAccount, false, true),
                AccountMeta(metadata, false, false),
                AccountMeta(authority, false, false),
                AccountMeta(auctioneerAuthority, true, false),
                AccountMeta(auctionHouse, false, false),
                AccountMeta(auctionHouseFeeAccount, false, true),
                AccountMeta(
                    sellerTradeState, false,
                    true
                ),
                AccountMeta(freeSellerTradeState, false, true),
                AccountMeta(
                    ahAuctioneerPda,
                    false, false
                ),
                AccountMeta(programAsSigner, false, false),
                AccountMeta(
                    tokenProgram,
                    false, false
                ),
                AccountMeta(systemProgram, false, false),
                AccountMeta(
                    rent, false,
                    false
                )
            ), Borsh.encodeToByteArray(
                Args_auctioneerSell.serializer(),
                Args_auctioneerSell(
                    tradeStateBump, freeTradeStateBump, programAsSignerBump,
                    tokenSize
                )
            )
        )

    fun withdraw(
        wallet: PublicKey,
        receiptAccount: PublicKey,
        escrowPaymentAccount: PublicKey,
        treasuryMint: PublicKey,
        authority: PublicKey,
        auctionHouse: PublicKey,
        auctionHouseFeeAccount: PublicKey,
        tokenProgram: PublicKey,
        systemProgram: PublicKey,
        ataProgram: PublicKey,
        rent: PublicKey,
        escrowPaymentBump: UByte,
        amount: ULong
    ): TransactionInstruction =
        TransactionInstruction(
            PublicKey("hausS13jsjafwWwGqZTUQRmWyvyxn9EQpqMwV1PBBmk"),
            listOf(
                AccountMeta(wallet, false, false),
                AccountMeta(receiptAccount, false, true),
                AccountMeta(escrowPaymentAccount, false, true),
                AccountMeta(treasuryMint, false, false),
                AccountMeta(authority, false, false),
                AccountMeta(auctionHouse, false, false),
                AccountMeta(auctionHouseFeeAccount, false, true),
                AccountMeta(
                    tokenProgram, false,
                    false
                ),
                AccountMeta(systemProgram, false, false),
                AccountMeta(ataProgram, false, false),
                AccountMeta(rent, false, false)
            ), Borsh.encodeToByteArray(
                Args_withdraw.serializer(),
                Args_withdraw(escrowPaymentBump, amount)
            )
        )

    fun auctioneerWithdraw(
        wallet: PublicKey,
        receiptAccount: PublicKey,
        escrowPaymentAccount: PublicKey,
        treasuryMint: PublicKey,
        authority: PublicKey,
        auctioneerAuthority: PublicKey,
        auctionHouse: PublicKey,
        auctionHouseFeeAccount: PublicKey,
        ahAuctioneerPda: PublicKey,
        tokenProgram: PublicKey,
        systemProgram: PublicKey,
        ataProgram: PublicKey,
        rent: PublicKey,
        escrowPaymentBump: UByte,
        amount: ULong
    ): TransactionInstruction =
        TransactionInstruction(
            PublicKey("hausS13jsjafwWwGqZTUQRmWyvyxn9EQpqMwV1PBBmk"),
            listOf(
                AccountMeta(wallet, false, false),
                AccountMeta(receiptAccount, false, true),
                AccountMeta(escrowPaymentAccount, false, true),
                AccountMeta(treasuryMint, false, false),
                AccountMeta(authority, false, false),
                AccountMeta(auctioneerAuthority, true, false),
                AccountMeta(auctionHouse, false, false),
                AccountMeta(
                    auctionHouseFeeAccount, false,
                    true
                ),
                AccountMeta(ahAuctioneerPda, false, false),
                AccountMeta(
                    tokenProgram, false,
                    false
                ),
                AccountMeta(systemProgram, false, false),
                AccountMeta(ataProgram, false, false),
                AccountMeta(rent, false, false)
            ),
            Borsh.encodeToByteArray(
                Args_auctioneerWithdraw.serializer(),
                Args_auctioneerWithdraw(escrowPaymentBump, amount)
            )
        )

    fun closeEscrowAccount(
        wallet: PublicKey,
        escrowPaymentAccount: PublicKey,
        auctionHouse: PublicKey,
        systemProgram: PublicKey,
        escrowPaymentBump: UByte
    ): TransactionInstruction =
        TransactionInstruction(
            PublicKey("hausS13jsjafwWwGqZTUQRmWyvyxn9EQpqMwV1PBBmk"),
            listOf(
                AccountMeta(wallet, true, false), AccountMeta(escrowPaymentAccount, false, true),
                AccountMeta(auctionHouse, false, false), AccountMeta(systemProgram, false, false)
            ),
            Borsh.encodeToByteArray(
                Args_closeEscrowAccount.serializer(),
                Args_closeEscrowAccount(escrowPaymentBump)
            )
        )

    fun delegateAuctioneer(
        auctionHouse: PublicKey,
        authority: PublicKey,
        auctioneerAuthority: PublicKey,
        ahAuctioneerPda: PublicKey,
        systemProgram: PublicKey,
        scopes: List<AuthorityScope>
    ): TransactionInstruction =
        TransactionInstruction(
            PublicKey("hausS13jsjafwWwGqZTUQRmWyvyxn9EQpqMwV1PBBmk"),
            listOf(
                AccountMeta(auctionHouse, false, true), AccountMeta(authority, true, true),
                AccountMeta(auctioneerAuthority, false, false), AccountMeta(
                    ahAuctioneerPda, false,
                    true
                ), AccountMeta(systemProgram, false, false)
            ),
            Borsh.encodeToByteArray(
                Args_delegateAuctioneer.serializer(),
                Args_delegateAuctioneer(scopes)
            )
        )

    fun updateAuctioneer(
        auctionHouse: PublicKey,
        authority: PublicKey,
        auctioneerAuthority: PublicKey,
        ahAuctioneerPda: PublicKey,
        systemProgram: PublicKey,
        scopes: List<AuthorityScope>
    ): TransactionInstruction =
        TransactionInstruction(
            PublicKey("hausS13jsjafwWwGqZTUQRmWyvyxn9EQpqMwV1PBBmk"),
            listOf(
                AccountMeta(auctionHouse, false, true), AccountMeta(authority, true, true),
                AccountMeta(auctioneerAuthority, false, false), AccountMeta(
                    ahAuctioneerPda, false,
                    true
                ), AccountMeta(systemProgram, false, false)
            ),
            Borsh.encodeToByteArray(
                Args_updateAuctioneer.serializer(),
                Args_updateAuctioneer(scopes)
            )
        )

    fun printListingReceipt(
        receipt: PublicKey,
        bookkeeper: PublicKey,
        systemProgram: PublicKey,
        rent: PublicKey,
        instruction: PublicKey,
        receiptBump: UByte
    ): TransactionInstruction =
        TransactionInstruction(
            PublicKey("hausS13jsjafwWwGqZTUQRmWyvyxn9EQpqMwV1PBBmk"),
            listOf(
                AccountMeta(receipt, false, true), AccountMeta(bookkeeper, true, true),
                AccountMeta(systemProgram, false, false), AccountMeta(rent, false, false),
                AccountMeta(instruction, false, false)
            ),
            Borsh.encodeToByteArray(
                Args_printListingReceipt.serializer(),
                Args_printListingReceipt(receiptBump)
            )
        )

    fun cancelListingReceipt(
        receipt: PublicKey,
        systemProgram: PublicKey,
        instruction: PublicKey
    ): TransactionInstruction =
        TransactionInstruction(
            PublicKey("hausS13jsjafwWwGqZTUQRmWyvyxn9EQpqMwV1PBBmk"),
            listOf(
                AccountMeta(receipt, false, true), AccountMeta(systemProgram, false, false),
                AccountMeta(instruction, false, false)
            ),
            Borsh.encodeToByteArray(
                Args_cancelListingReceipt.serializer(),
                Args_cancelListingReceipt()
            )
        )

    fun printBidReceipt(
        receipt: PublicKey,
        bookkeeper: PublicKey,
        systemProgram: PublicKey,
        rent: PublicKey,
        instruction: PublicKey,
        receiptBump: UByte
    ): TransactionInstruction =
        TransactionInstruction(
            PublicKey("hausS13jsjafwWwGqZTUQRmWyvyxn9EQpqMwV1PBBmk"),
            listOf(
                AccountMeta(receipt, false, true), AccountMeta(bookkeeper, true, true),
                AccountMeta(systemProgram, false, false), AccountMeta(rent, false, false),
                AccountMeta(instruction, false, false)
            ),
            Borsh.encodeToByteArray(
                Args_printBidReceipt.serializer(),
                Args_printBidReceipt(receiptBump)
            )
        )

    fun cancelBidReceipt(
        receipt: PublicKey,
        systemProgram: PublicKey,
        instruction: PublicKey
    ): TransactionInstruction =
        TransactionInstruction(
            PublicKey("hausS13jsjafwWwGqZTUQRmWyvyxn9EQpqMwV1PBBmk"),
            listOf(
                AccountMeta(receipt, false, true), AccountMeta(systemProgram, false, false),
                AccountMeta(instruction, false, false)
            ),
            Borsh.encodeToByteArray(Args_cancelBidReceipt.serializer(), Args_cancelBidReceipt())
        )

    fun printPurchaseReceipt(
        purchaseReceipt: PublicKey,
        listingReceipt: PublicKey,
        bidReceipt: PublicKey,
        bookkeeper: PublicKey,
        systemProgram: PublicKey,
        rent: PublicKey,
        instruction: PublicKey,
        purchaseReceiptBump: UByte
    ): TransactionInstruction =
        TransactionInstruction(
            PublicKey("hausS13jsjafwWwGqZTUQRmWyvyxn9EQpqMwV1PBBmk"),
            listOf(
                AccountMeta(purchaseReceipt, false, true), AccountMeta(
                    listingReceipt, false,
                    true
                ), AccountMeta(bidReceipt, false, true), AccountMeta(bookkeeper, true, true),
                AccountMeta(systemProgram, false, false), AccountMeta(rent, false, false),
                AccountMeta(instruction, false, false)
            ),
            Borsh.encodeToByteArray(
                Args_printPurchaseReceipt.serializer(),
                Args_printPurchaseReceipt(purchaseReceiptBump)
            )
        )

    @Serializable
    class Args_withdrawFromFee(val amount: ULong)

    @Serializable
    class Args_withdrawFromTreasury(val amount: ULong)

    @Serializable
    class Args_updateAuctionHouse(
        val sellerFeeBasisPoints: UShort?,
        val requiresSignOff: Boolean?,
        val canChangeSalePrice: Boolean?
    )

    @Serializable
    class Args_createAuctionHouse(
        val bump: UByte,
        val feePayerBump: UByte,
        val treasuryBump: UByte,
        val sellerFeeBasisPoints: UShort,
        val requiresSignOff: Boolean,
        val canChangeSalePrice: Boolean
    )

    @Serializable
    class Args_buy(
        val tradeStateBump: UByte,
        val escrowPaymentBump: UByte,
        val buyerPrice: ULong,
        val tokenSize: ULong
    )

    @Serializable
    class Args_auctioneerBuy(
        val tradeStateBump: UByte,
        val escrowPaymentBump: UByte,
        val buyerPrice: ULong,
        val tokenSize: ULong
    )

    @Serializable
    class Args_publicBuy(
        val tradeStateBump: UByte,
        val escrowPaymentBump: UByte,
        val buyerPrice: ULong,
        val tokenSize: ULong
    )

    @Serializable
    class Args_auctioneerPublicBuy(
        val tradeStateBump: UByte,
        val escrowPaymentBump: UByte,
        val buyerPrice: ULong,
        val tokenSize: ULong
    )

    @Serializable
    class Args_cancel(val buyerPrice: ULong, val tokenSize: ULong)

    @Serializable
    class Args_auctioneerCancel(val buyerPrice: ULong, val tokenSize: ULong)

    @Serializable
    class Args_deposit(val escrowPaymentBump: UByte, val amount: ULong)

    @Serializable
    class Args_auctioneerDeposit(val escrowPaymentBump: UByte, val amount: ULong)

    @Serializable
    class Args_executeSale(
        val escrowPaymentBump: UByte,
        val freeTradeStateBump: UByte,
        val programAsSignerBump: UByte,
        val buyerPrice: ULong,
        val tokenSize: ULong
    )

    @Serializable
    class Args_executePartialSale(
        val escrowPaymentBump: UByte,
        val freeTradeStateBump: UByte,
        val programAsSignerBump: UByte,
        val buyerPrice: ULong,
        val tokenSize: ULong,
        val partialOrderSize: ULong?,
        val partialOrderPrice: ULong?
    )

    @Serializable
    class Args_auctioneerExecuteSale(
        val escrowPaymentBump: UByte,
        val freeTradeStateBump: UByte,
        val programAsSignerBump: UByte,
        val buyerPrice: ULong,
        val tokenSize: ULong
    )

    @Serializable
    class Args_auctioneerExecutePartialSale(
        val escrowPaymentBump: UByte,
        val freeTradeStateBump: UByte,
        val programAsSignerBump: UByte,
        val buyerPrice: ULong,
        val tokenSize: ULong,
        val partialOrderSize: ULong?,
        val partialOrderPrice: ULong?
    )

    @Serializable
    class Args_sell(
        val tradeStateBump: UByte,
        val freeTradeStateBump: UByte,
        val programAsSignerBump: UByte,
        val buyerPrice: ULong,
        val tokenSize: ULong
    )

    @Serializable
    class Args_auctioneerSell(
        val tradeStateBump: UByte,
        val freeTradeStateBump: UByte,
        val programAsSignerBump: UByte,
        val tokenSize: ULong
    )

    @Serializable
    class Args_withdraw(val escrowPaymentBump: UByte, val amount: ULong)

    @Serializable
    class Args_auctioneerWithdraw(val escrowPaymentBump: UByte, val amount: ULong)

    @Serializable
    class Args_closeEscrowAccount(val escrowPaymentBump: UByte)

    @Serializable
    class Args_delegateAuctioneer(val scopes: List<AuthorityScope>)

    @Serializable
    class Args_updateAuctioneer(val scopes: List<AuthorityScope>)

    @Serializable
    class Args_printListingReceipt(val receiptBump: UByte)

    @Serializable
    class Args_cancelListingReceipt

    @Serializable
    class Args_printBidReceipt(val receiptBump: UByte)

    @Serializable
    class Args_cancelBidReceipt

    @Serializable
    class Args_printPurchaseReceipt(val purchaseReceiptBump: UByte)
}
