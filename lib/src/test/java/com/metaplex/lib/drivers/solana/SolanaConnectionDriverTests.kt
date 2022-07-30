/*
 * SolanaConnectionDriverTests
 * Metaplex
 * 
 * Created by Funkatronics on 7/29/2022
 */

package com.metaplex.lib.drivers.solana

import com.metaplex.lib.drivers.rpc.RpcError
import com.metaplex.mock.driver.rpc.MockRpcDriver
import com.solana.core.PublicKey
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import java.lang.Error

class SolanaConnectionDriverTests {
    
    @Test
    fun testGetAccountInfoReturnsValidAccountInfo() {
        // given
        val address = "5xN42RZCk7wA4GjQU2VVDhda8LBL8fAnrKZK921sybLF"
        val accountRequest = SolanaAccountRequest(address)
        val expectedAccountInfo = AccountInfo("testAccount", false, 0, "", 0)
        val solanaDriver = SolanaConnectionDriver(MockRpcDriver().apply {
            willReturn(accountRequest, SolanaValue(expectedAccountInfo))
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
        val solanaDriver = SolanaConnectionDriver(MockRpcDriver().apply {
            willReturn(SolanaAccountRequest(address), SolanaValue(null))
        })

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
}