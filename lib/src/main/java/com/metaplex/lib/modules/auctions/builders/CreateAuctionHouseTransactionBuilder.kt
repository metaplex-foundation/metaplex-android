/*
 * CreateAuctionHouseTransactionBuilder
 * Metaplex
 * 
 * Created by Funkatronics on 10/20/2022
 */

package com.metaplex.lib.modules.auctions.builders

import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.experimental.jen.auctionhouse.AuctionHouseInstructions
import com.metaplex.lib.modules.auctions.models.AuctionHouse
import com.metaplex.lib.modules.auctions.models.feeAccountPda
import com.metaplex.lib.modules.auctions.models.treasuryAccountPda
import com.metaplex.lib.modules.token.WRAPPED_SOL_MINT_ADDRESS
import com.metaplex.lib.shared.builders.TransactionBuilder
import com.solana.core.PublicKey
import com.solana.core.Sysvar
import com.solana.core.Transaction
import com.solana.programs.AssociatedTokenProgram
import com.solana.programs.SystemProgram
import com.solana.programs.TokenProgram
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CreateAuctionHouseTransactionBuilder(val auctionHouse: AuctionHouse,
                                           payer: PublicKey, connection: Connection,
                                           dispatcher: CoroutineDispatcher = Dispatchers.IO)
    : TransactionBuilder(payer, connection, dispatcher) {

    override suspend fun build(): Result<Transaction> = withContext(dispatcher) {
        Result.success(Transaction().apply {
            auctionHouse.apply {

                val auctionHousePda = AuctionHouse.pda(authority, treasuryMint)
                val feeAccountPda = feeAccountPda()
                val treasuryAccountPda = treasuryAccountPda()
                val treasuryWithdrawalDestination =
                    if (treasuryMint == PublicKey(WRAPPED_SOL_MINT_ADDRESS))
                        treasuryWithdrawalDestinationOwner
                    else
                        PublicKey.associatedTokenAddress(
                            treasuryMint, treasuryWithdrawalDestinationOwner
                        ).address

                // Initialize the candy machine account.
                addInstruction(
                    AuctionHouseInstructions.createAuctionHouse(
                        treasuryMint,
                        payer,
                        authority,
                        feeWithdrawalDestination,
                        treasuryWithdrawalDestination,
                        treasuryWithdrawalDestinationOwner,
                        auctionHouse = auctionHousePda.address,
                        auctionHouseFeeAccount = feeAccountPda.address,
                        auctionHouseTreasury = treasuryAccountPda.address,
                        tokenProgram = TokenProgram.PROGRAM_ID,
                        systemProgram = SystemProgram.PROGRAM_ID,
                        ataProgram = AssociatedTokenProgram.SPL_ASSOCIATED_TOKEN_ACCOUNT_PROGRAM_ID,
                        rent = Sysvar.SYSVAR_RENT_PUBKEY,
                        bump = auctionHousePda.nonce.toUByte(),
                        feePayerBump = feeAccountPda.nonce.toUByte(),
                        treasuryBump = treasuryAccountPda.nonce.toUByte(),
                        sellerFeeBasisPoints = sellerFeeBasisPoints,
                        requiresSignOff = requiresSignOff,
                        canChangeSalePrice = canChangeSalePrice
                    ))
            }
        })
    }
}