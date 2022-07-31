/*
 * SolanaConnectionDriverTests
 * Metaplex
 * 
 * Created by Funkatronics on 7/29/2022
 */

package com.metaplex.lib.drivers.solana

import com.metaplex.lib.drivers.rpc.RpcError
import com.metaplex.lib.shared.AccountPublicKey
import com.metaplex.mock.driver.rpc.MockRpcDriver
import com.solana.core.PublicKey
import com.solana.models.DataSlice
import com.solana.models.ProgramAccount
import com.solana.models.ProgramAccountConfig
import com.solana.networking.RPCEndpoint
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import java.lang.Error
import kotlin.coroutines.Continuation
import kotlin.coroutines.suspendCoroutine

class SolanaConnectionDriverTests {

    //region getAccountInfo
    @Test
    fun testGetAccountInfoReturnsValidAccountInfo() {
        // given
        val address = "5xN42RZCk7wA4GjQU2VVDhda8LBL8fAnrKZK921sybLF"
        val accountRequest = SolanaAccountRequest(address)
        val expectedAccountInfo = AccountInfo("testAccount", false, 0, "", 0)
        val solanaDriver = SolanaConnectionDriver(MockRpcDriver().apply {
            willReturn(accountRequest, expectedAccountInfo)
        })
        
        // when
        var actualAccountInfo: AccountInfo<String>?
        runBlocking {
            actualAccountInfo = solanaDriver.getAccountInfo<String>(PublicKey(address)).getOrNull()
        }
        
        // then
        Assert.assertEquals(expectedAccountInfo, actualAccountInfo)
    }

    @Test
    fun testGetAccountInfoReturnsErrorForNullAccount() {
        // given
        val address = "5xN42RZCk7wA4GjQU2VVDhda8LBL8fAnrKZK921sybLF"
        val expectedResult = Result.failure<String>(Error("Account return Null"))
        val solanaDriver = SolanaConnectionDriver(MockRpcDriver())

        // when
        var actualResult: Result<Any>?
        runBlocking {
            actualResult = solanaDriver.getAccountInfo<String>(PublicKey(address))
        }

        // then
        Assert.assertEquals(expectedResult.isFailure, actualResult?.isFailure)
        Assert.assertEquals(expectedResult.exceptionOrNull()?.message,
            actualResult?.exceptionOrNull()?.message)
    }

    @Test
    fun testGetAccountInfoReturnsError() {
        // given
        val address = "5xN42RZCk7wA4GjQU2VVDhda8LBL8fAnrKZK921sybLF"
        val expectedErrorMessage = "Error Message"
        val expectedResult = Result.failure<String>(Error(expectedErrorMessage))
        val solanaDriver = SolanaConnectionDriver(MockRpcDriver().apply {
            willError(SolanaAccountRequest(address), RpcError(1234, expectedErrorMessage))
        })

        // when
        var actualResult: Result<Any>?
        runBlocking {
            actualResult = solanaDriver.getAccountInfo<String>(PublicKey(address))
        }

        // then
        Assert.assertEquals(expectedResult.isFailure, actualResult?.isFailure)
        Assert.assertEquals(expectedErrorMessage, actualResult?.exceptionOrNull()?.message)
    }
    //endregion

    //region getProgramAccounts
    /*
     * The implementation of these tests is temporary. Eventually, we will change these tests to
     * use the suspendable API and mocked network layer like the [getAccountInfo] tests above.
     *
     * The purpose of this approach is TDD: currently SolanaConnectionDriver uses the legacy
     * implementation of [getProgramAccounts], but I will soon implement a suspendable version
     * of this method, and this test will allow me to verify that it works the same.
     *
     * Note: some test are currently commented out to avoid the "Too many requests for a specific
     * RPC call" error - This is why we need a fully mockable network/RPC layer!
     */
    @Test
    fun testGetProgramAccountsReturnsKnownAccounts() {
        // given
        val account = "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA"
        val solanaDriver = SolanaConnectionDriver(RPCEndpoint.devnetSolana)

        // this call returns a massive list, only checking 3 known keys for now
        val expectedKeys = listOf(
            "6UiiYqjJxBTpFN8PDZV7d6LWqoysFYvNZprasrWmdAnB",
            "CN6jy23npPfAZzYpwM9hg3QnPsCDNg8zQYYAqPqjvWj1",
            "GKHwi3wMdKrQoqLL58AUG4BRLJPV9et3Goeez6Purbzu"
        )

        // when
        val config = ProgramAccountConfig(
            filters = listOf(
                mapOf("dataSize" to 165),
                mapOf("memcmp" to mapOf("offset" to 32, "bytes" to "CN87nZuhnFdz74S9zn3bxCcd5ZxW55nwvgAv5C2Tz3K7")),
                mapOf("memcmp" to mapOf("offset" to 64, "bytes" to "2"))
            ),
            dataSlice = DataSlice(0, 32),
        )

        var actualAccounts: List<ProgramAccount<AccountPublicKey>>?
        runBlocking {
            actualAccounts = suspendCoroutine { continuation: Continuation<List<ProgramAccount<AccountPublicKey>>> ->
                solanaDriver.getProgramAccounts(PublicKey(account), config, AccountPublicKey::class.java) {
                    continuation.resumeWith(it)
                }
            }
        }

        // then
        Assert.assertTrue(actualAccounts?.map { it.pubkey }?.containsAll(expectedKeys) == true)
    }

//    @Test
//    fun testGetProgramAccountsReturnsEmptyListForUnknownAccount() {
//        // given
//        val account = "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DB"
//        val solanaDriver = SolanaConnectionDriver(RPCEndpoint.devnetSolana)
//
//        // this call returns a massive list, only checking 3 known keys for now
//        val expectedAccounts = listOf<ProgramAccount<AccountPublicKey>>()
//
//        // when
//        val config = ProgramAccountConfig(
//            filters = listOf(
//                mapOf("dataSize" to 165),
//                mapOf("memcmp" to mapOf("offset" to 32, "bytes" to "CN87nZuhnFdz74S9zn3bxCcd5ZxW55nwvgAv5C2Tz3K7")),
//                mapOf("memcmp" to mapOf("offset" to 64, "bytes" to "2"))
//            ),
//            dataSlice = DataSlice(0, 32),
//        )
//
//        var actualAccounts: List<ProgramAccount<AccountPublicKey>>?
//        runBlocking {
//            actualAccounts = suspendCoroutine { continuation: Continuation<List<ProgramAccount<AccountPublicKey>>> ->
//                solanaDriver.getProgramAccounts(PublicKey(account), config, AccountPublicKey::class.java) {
//                    continuation.resumeWith(it)
//                }
//            }
//        }
//
//        // then
//        Assert.assertEquals(expectedAccounts, actualAccounts)
//    }
//
//    @Test
//    fun testGetProgramAccountsReturnsErrorForInvalidParams() {
//        // given
//        val account = "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA"
//        val solanaDriver = SolanaConnectionDriver(RPCEndpoint.devnetSolana)
//        val expectedErrorMessage = "Invalid params"
//
//        // when
//        val config = ProgramAccountConfig(
//            filters = listOf(
//                "tickle me elmo", 42069
//            )
//        )
//
//        var actualErrorMessage: String? = null
//        runBlocking {
//            suspendCoroutine { continuation: Continuation<Unit> ->
//                solanaDriver.getProgramAccounts(PublicKey(account), config, AccountPublicKey::class.java) {
//                    actualErrorMessage = (it.exceptionOrNull() as? NetworkingError.invalidResponse)?.rpcError?.message
//                    continuation.resumeWith(Result.success(Unit))
//                }
//            }
//        }
//
//        // then
//        Assert.assertTrue(actualErrorMessage?.contains(expectedErrorMessage, true) == true)
//    }
    //endregion
}
