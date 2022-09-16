/*
 * CandyMachineBuilderClient
 * Metaplex
 * 
 * Created by Funkatronics on 9/16/2022
 */

package com.metaplex.lib.modules.candymachines

import com.metaplex.lib.drivers.indenty.IdentityDriver
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.modules.candymachines.models.CandyMachine
import com.metaplex.lib.modules.candymachines.models.buildInitializeCandyMachineTransaction
import com.solana.core.Account
import com.solana.core.PublicKey
import com.solana.core.Transaction
import kotlin.coroutines.suspendCoroutine

class CandyMachineBuilderClient(val connection: Connection, val signer: IdentityDriver) {

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