/*
 * CreateMintWithTokenInstruction
 * Metaplex
 * 
 * Created by Funkatronics on 9/28/2022
 */

package com.metaplex.lib.modules.token.builders

import com.metaplex.lib.modules.token.MINT_SIZE
import com.metaplex.lib.modules.token.MIN_RENT_FOR_MINT
import com.metaplex.lib.programs.tokens.TokenProgram
import com.solana.core.PublicKey
import com.solana.core.Transaction
import com.solana.programs.AssociatedTokenProgram
import com.solana.programs.SystemProgram

fun Transaction.addMintWithTokenInstruction(payer: PublicKey, newMint: PublicKey) {

    val associatedTokenAddress = PublicKey.associatedTokenAddress(payer, newMint).address

    // create mint
    addInstruction(SystemProgram.createAccount(payer, newMint, MIN_RENT_FOR_MINT, MINT_SIZE, TokenProgram.publicKey))
    addInstruction(com.solana.programs.TokenProgram.initializeMint(newMint, 0, payer, payer))

    // create token
    addInstruction(AssociatedTokenProgram.createAssociatedTokenAccountInstruction(
        mint = newMint, associatedAccount = associatedTokenAddress, owner = payer, payer = payer))

    // this code is for creating a (non associated) token account
//    addInstruction(SystemProgram.createAccount(payer, newToken, 10000000, 165, TokenProgram.publicKey))
//    addInstruction(com.solana.programs.TokenProgram.initializeAccount(newToken, newMint, payer))

    // mint token
    addInstruction(com.solana.programs.TokenProgram.mintTo(newMint, associatedTokenAddress, payer, 1))
}