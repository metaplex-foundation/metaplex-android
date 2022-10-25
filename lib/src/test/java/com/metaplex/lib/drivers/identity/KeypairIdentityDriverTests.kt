package com.metaplex.lib.drivers.identity

import com.metaplex.lib.*
import com.metaplex.lib.drivers.indenty.KeypairIdentityDriver
import com.solana.core.DerivationPath
import com.solana.core.HotAccount
import com.solana.core.Transaction
import com.solana.programs.SystemProgram
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class KeypairIdentityDriverTests {

    @Test
    fun testSetUpKeypairIdentityDriverFromMnemonic() {
        // given
        val expectedPublicKey = SolanaTestData.TEST_ACCOUNT_MNEMONIC_PAIR.publicKey
        val solanaConnection = MetaplexTestUtils.generateConnectionDriver()
        val account = HotAccount.fromMnemonic(SolanaTestData.TEST_ACCOUNT_MNEMONIC_PAIR.mnemonic,
            "", DerivationPath.BIP44_M_44H_501H_0H_OH)

        // when
        val keypairIdentityDriver = KeypairIdentityDriver(account, solanaConnection)

        //then
        Assert.assertEquals(expectedPublicKey, keypairIdentityDriver.publicKey.toBase58())
    }

    @Test
    fun testSignTransactionReturnsTrxHash() {
        // given
        val expectedSignedTransaction = "AaHQ/obYLnD6GUFqxDKiiNkw2NYsLt+NZHa8ALB64uM0wpADNVQ5eWhzW38FcxfthDz6zXsJao58y5/fFovSoAABAAEC1J5StK6hI4+ERBMKkBUsHeIzegza3Eb/t7dwtSG4Q9QAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAJrdSfHQAfBFYiaNQeEg3d9YB3F537Ex4K5dG79qBe0rAQECAAAMAgAAAOgDAAAAAAAA"
        val account = HotAccount.fromMnemonic(SolanaTestData.TEST_ACCOUNT_MNEMONIC_PAIR.mnemonic,
            "", DerivationPath.BIP44_M_44H_501H_0H_OH)

        val connectionDriver = MetaplexTestUtils.generateConnectionDriver()
        val instruction = SystemProgram.transfer(account.publicKey, account.publicKey, 1000)
        val transaction = Transaction().apply {
            addInstruction(instruction)
            setRecentBlockHash("BRXVgGhoUSYEoDUipLK7yGgvGvqp5TnRh6mNRMmubsmU")
        }

        // when
        var result: Transaction? = null
        val keypairIdentityDriver = KeypairIdentityDriver(account, connectionDriver)

        val lock = CountDownLatch(1)
        keypairIdentityDriver.signTransaction(transaction){
            result = it.getOrNull()
        }
        lock.await(2000, TimeUnit.MILLISECONDS)

        // then
        val base64Trx: String = java.util.Base64.getEncoder().encodeToString(result?.serialize())
        Assert.assertEquals(expectedSignedTransaction, base64Trx)
    }
}