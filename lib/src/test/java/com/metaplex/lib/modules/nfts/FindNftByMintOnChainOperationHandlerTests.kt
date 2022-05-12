package com.metaplex.lib.modules.nfts

import com.metaplex.lib.MemoryStorageDriver
import com.metaplex.lib.Metaplex
import com.metaplex.lib.TEST_PUBLICKEY
import com.metaplex.lib.drivers.indenty.ReadOnlyIdentityDriver
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.modules.nfts.operations.FindNftByMintOnChainOperationHandler
import com.metaplex.lib.modules.nfts.operations.FindNftByMintOperation
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

class FindNftByMintOnChainOperationHandlerTests {

    lateinit var metaplex: Metaplex

    @Before
    fun setUp() {
        val solanaConnection = SolanaConnectionDriver(RPCEndpoint.mainnetBetaSolana)
        val solanaIdentityDriver = ReadOnlyIdentityDriver(TEST_PUBLICKEY, solanaConnection.solanaRPC)
        val storageDriver = MemoryStorageDriver()
        this.metaplex = Metaplex(solanaConnection, solanaIdentityDriver, storageDriver)
    }

    @Test
    fun testFindNftByMintOnChainOperation() {
        val lock = CountDownLatch(1)
        var result: ResultWithCustomError<NFT, OperationError>? = null
        val operation = FindNftByMintOnChainOperationHandler(metaplex)
        operation.handle(FindNftByMintOperation.pure(ResultWithCustomError.success(PublicKey("HG2gLyDxmYGUfNWnvf81bJQj38twnF2aQivpkxficJbn")))).run {
            result = it
            lock.countDown()
        }
        lock.await(200000, TimeUnit.MILLISECONDS)
        val nft = result!!.getOrThrows()
        Assert.assertNotNull(nft)
        Assert.assertEquals("Aurorian #628", nft.metadataAccount.data.name,)
        Assert.assertEquals("HG2gLyDxmYGUfNWnvf81bJQj38twnF2aQivpkxficJbn", nft.metadataAccount.mint.toBase58())
        Assert.assertEquals(6, nft.masterEditionAccount!!.type)
    }
}