package com.metaplex.lib.modules.nfts

import com.metaplex.lib.MemoryStorageDriver
import com.metaplex.lib.Metaplex
import com.metaplex.lib.TEST_PUBLICKEY
import com.metaplex.lib.drivers.indenty.ReadOnlyIdentityDriver
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.modules.nfts.operations.FindNftsByMintListOnChainOperationHandler
import com.metaplex.lib.modules.nfts.operations.FindNftsByMintListOperation
import com.metaplex.lib.modules.nfts.operations.FindNftsByOwnerOnChainOperationHandler
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

class FindNftsByMintListOnChainOperationHandlerTests {

    lateinit var metaplex: Metaplex

    @Before
    fun setUp() {
        val solanaConnection = SolanaConnectionDriver(RPCEndpoint.mainnetBetaSolana)
        val solanaIdentityDriver =
            ReadOnlyIdentityDriver(TEST_PUBLICKEY, solanaConnection.solanaRPC)
        val storageDriver = MemoryStorageDriver()
        this.metaplex = Metaplex(solanaConnection, solanaIdentityDriver, storageDriver)
    }

    @Test
    fun testFindNftsByMintListOnChainOperation() {
        val lock = CountDownLatch(1)
        var result: ResultWithCustomError<List<NFT?>, OperationError>? = null
        val operation = FindNftsByMintListOnChainOperationHandler(metaplex)
        operation.handle(
            FindNftsByMintListOperation.pure(
                ResultWithCustomError.success(
                    listOf(
                        PublicKey("HG2gLyDxmYGUfNWnvf81bJQj38twnF2aQivpkxficJbn"),
                        PublicKey("B3n4QMZWCfsTZxC6bgJh9YGWFjESmExSYp8NGfJ8DQvF")
                    )
                )
            )
        ).run {
            result = it
            lock.countDown()
        }
        lock.await(2000, TimeUnit.MILLISECONDS)

        val nft = result!!.getOrThrows().first()
        Assert.assertNotNull(nft)

        Assert.assertEquals("Aurorian #628", nft!!.metadataAccount.data.name,)
        Assert.assertEquals("HG2gLyDxmYGUfNWnvf81bJQj38twnF2aQivpkxficJbn", nft.metadataAccount.mint.toBase58())

        val nft2 = result!!.getOrThrows()[1]!!
        Assert.assertNotNull(nft2)

        Assert.assertEquals("Degen Ape #2031", nft2!!.metadataAccount.data.name,)
        Assert.assertEquals("B3n4QMZWCfsTZxC6bgJh9YGWFjESmExSYp8NGfJ8DQvF", nft2.metadataAccount.mint.toBase58())
    }
    @Test
    fun testFindNftsByMintListOnChainOperation_OneNull() {
        val lock = CountDownLatch(1)
        var result: ResultWithCustomError<List<NFT?>, OperationError>? = null
        val operation = FindNftsByMintListOnChainOperationHandler(metaplex)
        operation.handle(
            FindNftsByMintListOperation.pure(
                ResultWithCustomError.success(
                    listOf(
                        PublicKey("HG2gLyDxmYGUfNWnvf81bJQj38twnF2aQivpkxficJbn"),
                        PublicKey("")
                    )
                )
            )
        ).run {
            result = it
            lock.countDown()
        }
        lock.await(2000, TimeUnit.MILLISECONDS)

        val nft = result!!.getOrThrows().first()
        Assert.assertNotNull(nft)

        Assert.assertEquals("Aurorian #628", nft!!.metadataAccount.data.name)
        Assert.assertEquals("HG2gLyDxmYGUfNWnvf81bJQj38twnF2aQivpkxficJbn", nft.metadataAccount.mint.toBase58())

        val nft2 = result!!.getOrThrows()[1]
        Assert.assertNull(nft2)
    }
}