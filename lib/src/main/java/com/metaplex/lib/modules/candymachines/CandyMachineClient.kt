/*
 * CandyMachineClient
 * Metaplex
 * 
 * Created by Funkatronics on 9/13/2022
 */

package com.metaplex.lib.modules.candymachines

import com.metaplex.lib.drivers.indenty.IdentityDriver
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.experimental.jen.candymachine.CandyMachineInstructions
import com.metaplex.lib.modules.candymachines.models.CandyMachine
import com.metaplex.lib.modules.candymachines.models.buildInitializeCandyMachineTransaction
import com.metaplex.lib.modules.candymachines.models.creatorPda
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.programs.token_metadata.MasterEditionAccount
import com.metaplex.lib.programs.token_metadata.accounts.MetadataAccount
import com.solana.core.Account
import com.solana.core.PublicKey
import com.solana.core.Sysvar
import com.solana.core.Transaction
import kotlin.coroutines.suspendCoroutine

const val SYSVAR_CLOCK_ID = "SysvarC1ock11111111111111111111111111111111"
const val SYSVAR_INSTRUCTIONS_ID = "Sysvar1nstructions1111111111111111111111111"
const val SYSVAR_SLOT_HASHES_ID = "SysvarS1otHashes111111111111111111111111111"

val Sysvar.SYSVAR_CLOCK_PUBKEY get () = PublicKey(SYSVAR_CLOCK_ID)
val Sysvar.SYSVAR_INSTRUCTIONS_PUBKEY get () = PublicKey(SYSVAR_INSTRUCTIONS_ID)
val Sysvar.SYSVAR_SLOT_HASHES_PUBKEY get () = PublicKey(SYSVAR_SLOT_HASHES_ID)

class CandyMachineClient(val candyMachine: CandyMachine,
                         val connection: Connection,
                         val signer: IdentityDriver) {

    suspend fun create(price: Long, sellerFeeBasisPoints: Int, itemsAvailable: Long,
                       wallet: PublicKey = signer.publicKey,
                       authority: PublicKey = signer.publicKey): Result<CandyMachine> {

        val candyMachineAccount = Account()
        val candyMachineAddress = candyMachineAccount.publicKey

        CandyMachine(
            address = candyMachineAddress,
            authority = authority,
            wallet = wallet,
            price = price,
            sellerFeeBasisPoints = sellerFeeBasisPoints.toUShort(),
            itemsAvailable = itemsAvailable
        ).apply {

            buildInitializeCandyMachineTransaction(signer.publicKey).signAndSend().getOrElse {
                return Result.failure(it) // we cant proceed further, return the error
            }

            return Result.success(this)
        }
    }

//    suspend fun mintNft(): Result<NFT> {
//
//        val creatorPda = candyMachine.creatorPda
//
//        val newMint = Account().publicKey
//        val newMetadata = MetadataAccount.pda(newMint).getOrThrows()
//        val newEdition = MasterEditionAccount.pda(newMint).getOrThrows()
//
//        Transaction().apply {
//            CandyMachineInstructions.mintNft(
//                candyMachine.address,
//                candyMachineCreator = creatorPda.address,
//                payer = signer.publicKey,
//                wallet = ,
//                metadata = newMetadata,
//                mint = newMint,
//                mintAuthority = signer.publicKey,
//                updateAuthority = ,
//                masterEdition = newEdition,
//                tokenMetadataProgram = ,
//                tokenProgram = ,
//                systemProgram = ,
//                rent = ,
//                clock = Sysvar.SYSVAR_CLOCK_PUBKEY,
//                recentBlockhashes = Sysvar.SYSVAR_SLOT_HASHES_PUBKEY,
//                instructionSysvarAccount = Sysvar.SYSVAR_INSTRUCTIONS_PUBKEY,
//                creatorBump = creatorPda.nonce.toUByte(),
//            )
//        }.signAndSend().getOrElse {
//            return Result.failure(it)
//        }
//    }

    private suspend fun Transaction.signAndSend(): Result<String> {

        setRecentBlockHash(connection.getRecentBlockhash().getOrElse {
            return Result.failure(it) // we cant proceed further, return the error
        })

        // TODO: refactor identity driver to use coroutines?
        return Result.success(
            suspendCoroutine { continuation ->
                signer.signTransaction(this) { result ->
                    result.onSuccess { signedTx ->

                        // TODO: I think I would prefer to handle the send here rather than
                        //  delegating to the identity driver, but #we'llgetthere
                        signer.sendTransaction(signedTx) { continuation.resumeWith(it) }
                    }.onFailure { continuation.resumeWith(Result.failure(it)) }
                }
            })
    }
}