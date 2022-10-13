/*
 * CandyMachineV2Tests
 * Metaplex
 * 
 * Created by Funkatronics on 9/22/2022
 */

package com.metaplex.lib.modules.candymachinev2

import com.metaplex.lib.drivers.indenty.KeypairIdentityDriver
import com.metaplex.lib.drivers.indenty.ReadOnlyIdentityDriver
import com.metaplex.lib.drivers.rpc.JdkRpcDriver
import com.metaplex.lib.drivers.solana.*
import com.metaplex.lib.modules.candymachines.CandyMachineClient
import com.metaplex.lib.modules.candymachines.models.CandyMachine
import com.metaplex.lib.modules.candymachinesv2.CandyMachineV2Client
import com.metaplex.lib.modules.candymachinesv2.models.CandyMachineV2
import com.metaplex.mock.driver.rpc.MockErrorRpcDriver
import com.solana.core.Account
import com.solana.core.PublicKey
import com.solana.networking.RPCEndpoint
import com.util.airdrop
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import java.net.URL

class CandyMachineV2ClientTests {

//    @Before
//    fun prepare() {
//        jenerateCandyMachineV2()
//    }

    //region UNIT
//    @Test
//    fun testFindCandyMachineV2ByAddressReturnsCandyMachine() = runTest {
//        // given
//        val wallet = Account().publicKey
//        val candyMachineAddress = Account().publicKey
//        val expectedCandyMachine = CandyMachineV2(
//            candyMachineAddress, wallet, wallet, 1, 250.toUShort(), 10
//        )
//
//        val connection = SolanaConnectionDriver(MockRpcDriver().apply {
//            willReturn(
//                AccountRequest(candyMachineAddress.toBase58()),
//                AccountInfo(CandyMachine(wallet, wallet, null, 0.toULong(), CandyMachineData()), false, 0, wallet.toBase58(), 0)
//            )
//        })
//
//        val client = CandyMachineV2Client(connection, ReadOnlyIdentityDriver(wallet, connection))
//
//        // when
//        val result = client.findByAddress(candyMachineAddress)
//
//        // then
//        Assert.assertTrue(result.isSuccess)
//        Assert.assertEquals(expectedCandyMachine, result.getOrNull())
//    }

    @Test
    fun testFindCandyMachineV2ByAddressReturnsError() = runTest {
        // given
        val expectedErrorMessage = "An Error Occurrred"
        val cmAddress = PublicKey(ByteArray(PublicKey.PUBLIC_KEY_LENGTH))
        val connection = SolanaConnectionDriver(MockErrorRpcDriver(expectedErrorMessage))

        val client = CandyMachineV2Client(connection,
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
        val client = CandyMachineV2Client(connection, KeypairIdentityDriver(signer, connection))

        // when
        val result = client.create(1L, 333, 1L)

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
        val client = CandyMachineV2Client(connection, KeypairIdentityDriver(signer, connection))

        // when
        val result = client.mintNft(CandyMachineV2(signer.publicKey, signer.publicKey, signer.publicKey,1L, 333.toUShort(), 1L))

        // then
        Assert.assertTrue(result.isFailure)
        Assert.assertEquals(expectedErrorMessage, result.exceptionOrNull()?.message)
    }
    //endregion

    //region INTEGRATION
    @Test
    fun testFindCandyMachineV2ByAddressReturnsValidCandyMachine() = runTest {
        // given
        val cmAddress = PublicKey("Hoj7d8Gtn7L7SUyqCDYqMVrCrgQ59By9GNWw8ppXfoGM")
        val connection = SolanaConnectionDriver(JdkRpcDriver(RPCEndpoint.devnetSolana.url), transactionOptions = TransactionOptions(Commitment.CONFIRMED, skipPreflight = true))
        val client = CandyMachineV2Client(connection,
            ReadOnlyIdentityDriver(Account().publicKey, connection)
        )

        // when
        val candyMachine: CandyMachineV2? = client.findByAddress(cmAddress).getOrNull()

        // then
        Assert.assertNotNull(candyMachine)
    }

    @Test
    fun testCandyMachineCreateCreatesValidCandyMachine() = runTest {
        // given
        val rpcUrl = URL("http://127.0.0.1:8899")
//        val rpcUrl = RPCEndpoint.devnetSolana.url
        val signer = Account()
        val connection = SolanaConnectionDriver(JdkRpcDriver(rpcUrl), transactionOptions = TransactionOptions(Commitment.CONFIRMED, skipPreflight = true))
        val client = CandyMachineV2Client(connection, KeypairIdentityDriver(signer, connection))

        // when
        connection.airdrop(signer.publicKey, 1f)

        val candyMachine = client.create(1, 250, 10).map {
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
        val connection = SolanaConnectionDriver(JdkRpcDriver(rpcUrl), transactionOptions = TransactionOptions(Commitment.CONFIRMED, skipPreflight = true))
        val client = CandyMachineV2Client(connection, KeypairIdentityDriver(signer, connection))

        // when
        connection.airdrop(signer.publicKey, 1f)

        val mintResult = client.create(1, 250, 10).map {
            client.mintNft(it)//.getOrThrow()
        }.getOrThrow()

        // then
        Assert.assertNotNull(mintResult)
    }
    //endregion
}