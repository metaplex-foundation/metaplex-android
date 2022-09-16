/*
 * CandyMachineClientTests
 * Metaplex
 * 
 * Created by Funkatronics on 9/8/2022
 */

package com.metaplex.lib.modules.candymachine

import android.util.Base64
import com.metaplex.lib.drivers.rpc.JdkRpcDriver
import com.metaplex.lib.drivers.solana.SolanaConnectionDriver
import com.metaplex.lib.experimental.jen.jenerateAuctionHouse
import com.metaplex.lib.experimental.jen.jenerateCandyCore
import com.metaplex.lib.experimental.jen.jenerateCandyMachine
import com.metaplex.lib.modules.candymachines.models.CandyMachine
import com.metaplex.lib.modules.candymachines.models.buildInitializeCandyMachineTransaction
import com.solana.core.Account
import com.solana.networking.RPCEndpoint
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CandyMachineClientTests {

//    @Before
//    fun prepare() {
//        jenerateCandyCore()
//    }

    @Test
    fun doStuff() = runTest {
        // given
        val signer = Account()
        val wallet = signer.publicKey
        val authority = signer.publicKey
        val candyMachine = Account()
        val candyMachineAddress = candyMachine.publicKey

        CandyMachine(
            address = candyMachineAddress,
            authority = authority,
            wallet = wallet,
            price = 1,
            sellerFeeBasisPoints = 250.toUShort(),
            itemsAvailable = 10
        ).apply {

            buildInitializeCandyMachineTransaction(signer.publicKey).apply {
                setRecentBlockHash(signer.publicKey.toBase58())
//                sign(listOf(signer, candyMachine))
                sign(signer)
                sign(candyMachine)
                println(Base64.encodeToString(serialize(), 0))
            }
        }
    }
}