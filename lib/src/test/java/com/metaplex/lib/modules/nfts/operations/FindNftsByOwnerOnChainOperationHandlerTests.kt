package com.metaplex.lib.modules.nfts.operations

import com.metaplex.lib.Metaplex
import com.metaplex.lib.TEST_PUBLICKEY
import com.metaplex.lib.drivers.indenty.ReadOnlyIdentityDriver
import com.metaplex.lib.drivers.storage.MemoryStorageDriver
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.shared.OperationError
import com.metaplex.lib.shared.ResultWithCustomError
import com.metaplex.lib.solana.SolanaConnectionDriver
import com.solana.core.PublicKey
import com.solana.networking.RPCEndpoint
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class FindNftsByOwnerOnChainOperationHandlerTests {

    lateinit var metaplex: Metaplex

    @Before
    fun setUp() {
        val solanaConnection = SolanaConnectionDriver(RPCEndpoint.mainnetBetaSolana)
        val solanaIdentityDriver = ReadOnlyIdentityDriver(TEST_PUBLICKEY, solanaConnection.solanaRPC)
        val storageDriver = MemoryStorageDriver()
        this.metaplex = Metaplex(solanaConnection, solanaIdentityDriver, storageDriver)
    }

    @Test
    fun testFindNftsByOwnerOnChainOperation() {
        val lock = CountDownLatch(1)
        var result: ResultWithCustomError<List<NFT?>, OperationError>? = null
        val operation = FindNftsByOwnerOnChainOperationHandler(metaplex)
        operation.handle(
            FindNftsByOwnerOperation.pure(
                ResultWithCustomError.success(PublicKey("CN87nZuhnFdz74S9zn3bxCcd5ZxW55nwvgAv5C2Tz3K7"))
            )
        ).run {
            result = it
            lock.countDown()
        }
        lock.await(2000, TimeUnit.MILLISECONDS)
        val nft = result!!.getOrThrows()
        Assert.assertNotNull(nft)
        val anNFT = nft.find { it?.metadataAccount?.mint?.toBase58() == "71PdnsexRQG92ZcSpEV8nn5XFqPzfK5j86yUWRF5NLyp" }
        Assert.assertEquals("AVgijgTG4WKDycFwYhqioMreXJpz14no8dbE8EF5roZV", anNFT!!.metadataAccount.update_authority.toBase58())
    }
}