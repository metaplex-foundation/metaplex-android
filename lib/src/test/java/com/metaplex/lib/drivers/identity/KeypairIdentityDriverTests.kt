package com.metaplex.lib.drivers.identity

import com.metaplex.lib.drivers.indenty.KeypairIdentityDriver
import com.metaplex.lib.solana.SolanaConnectionDriver
import com.solana.core.Account
import com.solana.core.DerivationPath
import com.solana.core.Transaction
import com.solana.networking.RPCEndpoint
import com.solana.programs.SystemProgram
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

val mnemonic = listOf("across", "start", "ancient", "solid", "bid", "sentence", "visit", "old", "have", "hobby", "magic", "bomb", "boring", "grunt", "rule", "extra", "place", "strong", "myth", "episode", "dinner", "thrive", "wave", "decide")

class KeypairIdentityDriverTests {

    private val account = Account.fromMnemonic(mnemonic, "", DerivationPath.BIP44_M_44H_501H_0H_OH)
    private val solanaConnection = SolanaConnectionDriver(RPCEndpoint.mainnetBetaSolana)
    lateinit var keypairIdentityDriver: KeypairIdentityDriver

    @Before
    fun setUp() {
        keypairIdentityDriver = KeypairIdentityDriver(solanaConnection.solanaRPC, account)
    }

    @Test
    fun testSetUpKeypairIdentityDriver() {
        val solanaConnection = SolanaConnectionDriver(RPCEndpoint.mainnetBetaSolana)
        val keypairIdentityDriver = KeypairIdentityDriver(solanaConnection.solanaRPC, Account.fromMnemonic(mnemonic, "", DerivationPath.BIP44_M_44H_501H_0H_OH))
        Assert.assertEquals(keypairIdentityDriver.publicKey.toBase58(), "FJyTK5ggCyWaZoJoQ9YAeRokNZtHbN4UwzeSWa2HxNyy")
    }

    @Test
    fun testSendTransactionInstruction() {
        val expectedSignedTransaction = "AaHQ/obYLnD6GUFqxDKiiNkw2NYsLt+NZHa8ALB64uM0wpADNVQ5eWhzW38FcxfthDz6zXsJao58y5/fFovSoAABAAEC1J5StK6hI4+ERBMKkBUsHeIzegza3Eb/t7dwtSG4Q9QAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAJrdSfHQAfBFYiaNQeEg3d9YB3F537Ex4K5dG79qBe0rAQECAAAMAgAAAOgDAAAAAAAA"

        val lock = CountDownLatch(1)
        var result: Result<Transaction>? = null
        val instruction = SystemProgram.transfer(account.publicKey, account.publicKey, 1000)
        val transaction = Transaction()
        transaction.addInstruction(instruction)
        transaction.setRecentBlockHash("BRXVgGhoUSYEoDUipLK7yGgvGvqp5TnRh6mNRMmubsmU")
        keypairIdentityDriver.signTransaction(transaction){
            result = it
        }

        lock.await(2000, TimeUnit.MILLISECONDS)
        val signedTransaction = result!!.getOrThrow()
        Assert.assertNotNull(signedTransaction)
        val base64Trx: String = java.util.Base64.getEncoder().encodeToString(signedTransaction.serialize())
        Assert.assertEquals(base64Trx, expectedSignedTransaction)
    }
}