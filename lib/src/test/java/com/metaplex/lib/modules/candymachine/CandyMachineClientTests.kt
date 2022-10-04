/*
 * CandyMachineClientTests
 * Metaplex
 * 
 * Created by Funkatronics on 9/8/2022
 */

@file:OptIn(ExperimentalCoroutinesApi::class)

package com.metaplex.lib.modules.candymachine

import com.metaplex.lib.drivers.indenty.KeypairIdentityDriver
import com.metaplex.lib.drivers.indenty.ReadOnlyIdentityDriver
import com.metaplex.lib.drivers.rpc.JdkRpcDriver
import com.metaplex.lib.drivers.solana.Commitment
import com.metaplex.lib.drivers.solana.SolanaConnectionDriver
import com.metaplex.lib.drivers.solana.TransactionOptions
import com.metaplex.lib.modules.candymachines.CandyMachineClient
import com.metaplex.lib.modules.candymachines.models.CandyMachine
import com.metaplex.mock.driver.rpc.MockErrorRpcDriver
import com.metaplex.unit.lib.modules.candymachine.createNft
import com.metaplex.unit.lib.modules.candymachine.insertItems
import com.solana.core.Account
import com.solana.core.PublicKey
import com.solana.networking.RPCEndpoint
import com.util.airdrop
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import java.net.URL

class CandyMachineClientTests {

//    @Before
//    fun prepare() {
//        jenerateCandyMachine()
//        jenerateCandyGuard()
//    }

    //region UNIT
    @Test
    fun testFindCandyMachineV2ByAddressHandlesAndReturnsError() = runTest {
        // given
        val expectedErrorMessage = "An Error Occurred"
        val cmAddress = PublicKey(ByteArray(PublicKey.PUBLIC_KEY_LENGTH))
        val connection = SolanaConnectionDriver(MockErrorRpcDriver(expectedErrorMessage))

        val client = CandyMachineClient(connection,
            ReadOnlyIdentityDriver(Account().publicKey, connection)
        )

        // when
        val result = client.findByAddress(cmAddress)

        // then
        Assert.assertTrue(result.isFailure)
        Assert.assertEquals(expectedErrorMessage, result.exceptionOrNull()!!.message)
    }

    @Test
    fun testCandyMachineCreateHandlesAndReturnsError() = runTest {
        // given
        val signer = Account()
        val expectedErrorMessage = "An Error Occurred"
        val connection = SolanaConnectionDriver(MockErrorRpcDriver(expectedErrorMessage))
        val client = CandyMachineClient(connection, KeypairIdentityDriver(signer, connection))

        // when
        val result = client.create(333, 1L, collection = signer.publicKey, collectionUpdateAuthority = signer.publicKey)

        // then
        Assert.assertTrue(result.isFailure)
        Assert.assertEquals(expectedErrorMessage, result.exceptionOrNull()?.message)
    }

    @Test
    fun testCandyMachineMintNftHandlesAndReturnsError() = runTest {
        // given
        val signer = Account()
        val expectedErrorMessage = "An Error Occurred"
        val connection = SolanaConnectionDriver(MockErrorRpcDriver(expectedErrorMessage))
        val client = CandyMachineClient(connection, KeypairIdentityDriver(signer, connection))

        // when
        val result = client.mintNft(CandyMachine(signer.publicKey, signer.publicKey, 333.toUShort(), 1L, collectionMintAddress = signer.publicKey, collectionUpdateAuthority = signer.publicKey))

        // then
        Assert.assertTrue(result.isFailure)
        Assert.assertEquals(expectedErrorMessage, result.exceptionOrNull()?.message)
    }
    //endregion

    //region INTEGRATION
    @Test
    fun testFindCandyMachineByAddressReturnsValidCandyMachine() = runTest {
        // given
        val cmAddress = PublicKey("4Add8hdxC44H3DcGfgWTvn2GNBfofk5uu2iatEW9LCYz")
        val connection = SolanaConnectionDriver(JdkRpcDriver(RPCEndpoint.devnetSolana.url))
        val client = CandyMachineClient(connection,
            ReadOnlyIdentityDriver(Account().publicKey, connection))

        // when
        val candyMachine: CandyMachine? = client.findByAddress(cmAddress).getOrNull()

        // then
        Assert.assertNotNull(candyMachine)
    }

    @Test
    fun testCandyMachineCreateCreatesValidCandyMachine() = runTest {
        // given
        val rpcUrl = URL("http://127.0.0.1:8899")
//        val rpcUrl = RPCEndpoint.devnetSolana.url
        val signer = Account()
        val connection = SolanaConnectionDriver(JdkRpcDriver(rpcUrl),
            transactionOptions = TransactionOptions(Commitment.CONFIRMED, skipPreflight = true))
        val client = CandyMachineClient(connection, KeypairIdentityDriver(signer, connection))

        // when
        connection.airdrop(signer.publicKey, 10f)
        val nft = client.createNft().getOrThrow()
        val candyMachine = client.create(333, 5000, nft.mint, signer.publicKey).map {
            client.findByAddress(it.address).getOrNull()
        }.getOrThrow()

        // then
        Assert.assertNotNull(candyMachine)
    }

    @Test
    fun testCandyMachineMintNftMintsAndReturnsNft() = runTest {
        // given
        val rpcUrl = URL("http://127.0.0.1:8899")
//        val rpcUrl = RPCEndpoint.devnetSolana.url
        val signer = Account()
        val connection = SolanaConnectionDriver(JdkRpcDriver(rpcUrl),
            transactionOptions = TransactionOptions(Commitment.CONFIRMED, skipPreflight = true))
        val client = CandyMachineClient(connection, KeypairIdentityDriver(signer, connection))

        // when
        connection.airdrop(signer.publicKey, 1f)
        val nft = client.createNft().getOrThrow()
        val mintResult = client.create(250, 1, nft.mint, signer.publicKey).map {
            client.insertItems(it)
            client.mintNft(it)//.getOrThrow()
        }.getOrThrow()

        // then
        Assert.assertNotNull(mintResult)
    }
    //endregion
}