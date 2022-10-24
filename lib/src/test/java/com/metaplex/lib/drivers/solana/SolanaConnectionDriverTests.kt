/*
 * SolanaConnectionDriverTests
 * Metaplex
 * 
 * Created by Funkatronics on 7/29/2022
 */

@file:OptIn(ExperimentalCoroutinesApi::class)

package com.metaplex.lib.drivers.solana

import com.metaplex.data.TestDataProvider
import com.metaplex.lib.MetaplexTestUtils
import com.metaplex.data.model.publicKey
import com.metaplex.lib.drivers.rpc.RpcError
import com.metaplex.lib.generateConnectionDriver
import com.metaplex.mock.driver.rpc.MockRpcDriver
import com.solana.core.HotAccount
import com.solana.core.PublicKey
import com.solana.models.ProgramAccountConfig
import com.solana.models.SignatureStatusRequestConfiguration
import com.solana.programs.SystemProgram
import com.util.airdrop
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.builtins.serializer
import org.junit.Assert
import org.junit.Test
import java.lang.Error

class SolanaConnectionDriverTests {

    //region UNIT
    //region getAccountInfo
    @Test
    fun testGetAccountInfoReturnsValidAccountInfo() = runTest {
        // given
        val address = TestDataProvider.auctionHouse.publicKey
        val accountRequest = AccountRequest(address)
        val expectedAccountInfo = AccountInfo("testAccount", false, 0, "", 0)
        val solanaDriver = SolanaConnectionDriver(MockRpcDriver().apply {
            willReturn(accountRequest, expectedAccountInfo)
        })
        
        // when
        var actualAccountInfo = solanaDriver.getAccountInfo<String>(PublicKey(address)).getOrNull()
        
        // then
        Assert.assertEquals(expectedAccountInfo, actualAccountInfo)
    }

    @Test
    fun testGetAccountInfoReturnsErrorForNullAccount() = runTest {
        // given
        val address = TestDataProvider.auctionHouse.publicKey
        val expectedResult = Result.failure<String>(Error("Account return Null"))
        val solanaDriver = SolanaConnectionDriver(MockRpcDriver())

        // when
        var actualResult = solanaDriver.getAccountInfo<String>(PublicKey(address))

        // then
        Assert.assertEquals(expectedResult.isFailure, actualResult.isFailure)
        Assert.assertEquals(expectedResult.exceptionOrNull()?.message,
            actualResult.exceptionOrNull()?.message)
    }

    @Test
    fun testGetAccountInfoReturnsError() = runTest {
        // given
        val address = TestDataProvider.badAddress
        val expectedErrorMessage = "Error Message"
        val expectedResult = Result.failure<String>(Error(expectedErrorMessage))
        val solanaDriver = SolanaConnectionDriver(MockRpcDriver().apply {
            willError(AccountRequest(address), RpcError(1234, expectedErrorMessage))
        })

        // when
        var actualResult = solanaDriver.getAccountInfo<String>(PublicKey(address))

        // then
        Assert.assertEquals(expectedResult.isFailure, actualResult.isFailure)
        Assert.assertEquals(expectedErrorMessage, actualResult.exceptionOrNull()?.message)
    }
    //endregion

    //region getMultipleAccountsInfo
    @Test
    fun testGetMultipleAccountsInfoReturnsValidAccountInfo() = runTest {
        // given
        val accounts = listOf(HotAccount().publicKey)
        val accountsRequest = MultipleAccountsRequest(accounts.map { it.toBase58() })
        val expectedAccountInfo = listOf(AccountInfo("testAccount", false, 0, "", 0))
        val solanaDriver = SolanaConnectionDriver(MockRpcDriver().apply {
            willReturn(accountsRequest, expectedAccountInfo)
        })

        // when
        val actualAccountInfo = solanaDriver.getMultipleAccountsInfo<String>(accounts).getOrNull()

        // then
        Assert.assertEquals(expectedAccountInfo, actualAccountInfo)
    }

    @Test
    fun testGetMultipleAccountsInfoReturnsEmptyListForNullAccount() = runTest {
        // given
        val accounts = listOf(HotAccount().publicKey)
        val expectedAccountInfo = listOf<String>()
        val solanaDriver = SolanaConnectionDriver(MockRpcDriver())

        // when
        val actualAccountInfo = solanaDriver.getMultipleAccountsInfo<String>(accounts).getOrNull()

        // then
        Assert.assertEquals(expectedAccountInfo, actualAccountInfo)
    }

    @Test
    fun testGetMultipleAccountsInfoReturnsError() = runTest {
        // given
        val accounts = listOf(TestDataProvider.badAddress)
        val expectedErrorMessage = "Error Message"
        val expectedResult = Result.failure<String>(Error(expectedErrorMessage))
        val solanaDriver = SolanaConnectionDriver(MockRpcDriver().apply {
            willError(MultipleAccountsRequest(accounts), RpcError(1234, expectedErrorMessage))
        })

        // when
        val actualResult = solanaDriver.getMultipleAccountsInfo<String>(accounts.map { PublicKey(it) })

        // then
        Assert.assertEquals(expectedResult.isFailure, actualResult.isFailure)
        Assert.assertEquals(expectedErrorMessage, actualResult.exceptionOrNull()?.message)
    }
    //endregion

    //region getProgramAccounts
    @Test
    fun testGetProgramAccountsReturnsValidAccountInfo() = runTest {
        // given
        val account = "accountAddress"
        val request = ProgramAccountRequest(account)
        val expectedAccounts = listOf(AccountInfoWithPublicKey(
            AccountInfo("programAccount", false, 0, "", 0),
            account
        ))

        val solanaDriver = SolanaConnectionDriver(MockRpcDriver().apply {
            willReturn(request, expectedAccounts)
        })

        // when
        val actualAccounts = solanaDriver
            .getProgramAccounts(String.serializer(), PublicKey(account), ProgramAccountConfig())
            .getOrDefault(listOf())

        // then
        Assert.assertEquals(expectedAccounts, actualAccounts)
    }

    @Test
    fun testGetProgramAccountsReturnsEmptyListForUnknownAccount() = runTest {
        // given
        val account = "accountAddress"
        val expectedAccounts = listOf<AccountInfoWithPublicKey<String>>()
        val solanaDriver = SolanaConnectionDriver(MockRpcDriver())

        // when
        val actualAccounts = solanaDriver
            .getProgramAccounts(String.serializer(), PublicKey(account), ProgramAccountConfig())
            .getOrNull()

        // then
        Assert.assertEquals(expectedAccounts, actualAccounts)
    }

    @Test
    fun testGetProgramAccountsReturnsErrorForInvalidParams() = runTest {
        // given
        val account = "accountAddress"
        val expectedErrorMessage = "Error Message"
        val expectedResult = Result.failure<String>(Error(expectedErrorMessage))
        val solanaDriver = SolanaConnectionDriver(MockRpcDriver().apply {
            willError(ProgramAccountRequest(account), RpcError(1234, expectedErrorMessage))
        })

        // when
        val actualResult = solanaDriver.getProgramAccounts(
            String.serializer(), PublicKey(account), ProgramAccountConfig()
        )

        // then
        Assert.assertEquals(expectedResult.isFailure, actualResult.isFailure)
        Assert.assertEquals(expectedErrorMessage, actualResult.exceptionOrNull()?.message)
    }
    //endregion

    //region getSignatureStatuses
    @Test
    fun testGetSignatureStatusesReturnsKnownSignatureStatus() = runTest {
        // given
        val signatures = listOf("transactionSignature")
        val config = SignatureStatusRequestConfiguration(false)
        val expectedStatuses = listOf(SignatureStatus(slot=147339869, confirmations=null, err=null, confirmationStatus="finalized"))
        val solanaDriver = SolanaConnectionDriver(MockRpcDriver().apply {
            willReturn(SignatureStatusRequest(signatures), expectedStatuses)
        })

        // when
        val actualStatuses = solanaDriver.getSignatureStatuses(signatures, config).getOrNull()

        // then
        Assert.assertEquals(expectedStatuses, actualStatuses)
    }

    @Test
    fun testGetSignatureStatusesReturnsListWithNullForUnknownSignature() = runTest {
        // given
        val signatures = listOf("33toJmPYfVr71UjPge66tRDGEtEyzRpTAsJmznwXrGLhcutfv1sw8WQgHjjX7FivuaCVunNScgqY4dbPNZwDam12")
        val config = SignatureStatusRequestConfiguration(false)
        val expectedStatuses = listOf<SignatureStatus?>(null)
        val solanaDriver = SolanaConnectionDriver(MockRpcDriver().apply {
            willReturn(SignatureStatusRequest(signatures), listOf(null))
        })

        // when
        val actualStatuses = solanaDriver.getSignatureStatuses(signatures, config).getOrNull()

        // then
        Assert.assertEquals(expectedStatuses, actualStatuses)
    }

    @Test
    fun testGetSignatureStatusesReturnsErrorForInvalidSignature() = runTest {
        // given
        val signatures = listOf("invalidTransactionSignature")
        val expectedErrorMessage = "Invalid param: Invalid"
        val expectedResult = Result.failure<SignatureStatus>(Error(expectedErrorMessage))
        val solanaDriver = SolanaConnectionDriver(MockRpcDriver().apply {
            willError(SignatureStatusRequest(signatures, false), RpcError(1234, expectedErrorMessage))
        })

        // when
        val actualResult = solanaDriver.getSignatureStatuses(signatures, SignatureStatusRequestConfiguration(false))

        // then
        Assert.assertEquals(expectedResult.isFailure, actualResult.isFailure)
        Assert.assertEquals(expectedErrorMessage, actualResult.exceptionOrNull()?.message)
    }
    //endregion
    //endregion

    //region INTEGRATION
    @Test
    fun testGetAccountInfoReturnsNonNullBufferForValidAccount() = runTest {
        // given
        val account = HotAccount()
        val connection = MetaplexTestUtils.generateConnectionDriver()

        // when
        connection.airdrop(account.publicKey, 0.1f)
        val accountInfo = connection.getAccountInfo<String>(account.publicKey).getOrThrow()

        // then
        Assert.assertNotNull(accountInfo)
        Assert.assertEquals(SystemProgram.PROGRAM_ID.toString(), accountInfo.owner)
    }

    @Test
    fun testGetMultipleAccountsReturnsNonNullBuffer()  = runTest {
        // given
        val account = HotAccount()
        val accountKeys = listOf(account.publicKey)
        val connection = MetaplexTestUtils.generateConnectionDriver()

        // when
        connection.airdrop(account.publicKey, 0.1f)
        val accountInfoList = connection.getMultipleAccountsInfo<String>(accountKeys).getOrThrow()

        // then
        Assert.assertNotNull(accountInfoList)
        Assert.assertTrue(accountInfoList.isNotEmpty())
    }
    //endregion
}
