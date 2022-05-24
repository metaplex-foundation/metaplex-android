package com.metaplex.lib.modules.nfts.models

import com.metaplex.lib.Metaplex
import com.metaplex.lib.TEST_PUBLICKEY
import com.metaplex.lib.drivers.indenty.ReadOnlyIdentityDriver
import com.metaplex.lib.drivers.storage.OkHttpSharedStorageDriver
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

class NFTTests {
    lateinit var metaplex: Metaplex

    @Before
    fun setUp() {
        val solanaConnection = SolanaConnectionDriver(RPCEndpoint.mainnetBetaSolana)
        val solanaIdentityDriver = ReadOnlyIdentityDriver(TEST_PUBLICKEY, solanaConnection.solanaRPC)
        val storageDriver = OkHttpSharedStorageDriver()
        this.metaplex = Metaplex(solanaConnection, solanaIdentityDriver, storageDriver)
    }

    @Test
    fun testsNFTMetadata(){
        var metadata: JsonMetadata? = null
        var nft: NFT? = null
        val lock = CountDownLatch(1)
        val operation = FindNftByMintOnChainOperationHandler(metaplex)
        operation.handle(FindNftByMintOperation.pure(ResultWithCustomError.success(PublicKey("HG2gLyDxmYGUfNWnvf81bJQj38twnF2aQivpkxficJbn")))).run {
            nft = it.getOrThrows()
            nft!!.metadata(metaplex){ result ->
                metadata = result.getOrThrows()
                lock.countDown()
            }
        }
        lock.await(20000, TimeUnit.MILLISECONDS)
        Assert.assertNotNull(metadata)
        Assert.assertEquals("Aurorian #628", metadata!!.name)
        Assert.assertEquals("AUROR", metadata!!.symbol)
        Assert.assertEquals(500.0, metadata!!.seller_fee_basis_points)
        val value = (metadata!!.attributes!![10].value as Value.number).value
        Assert.assertTrue(1.0 == value)
        Assert.assertEquals("8Kag8CqNdCX55s4A5W4iraS71h6mv6uTHqsJbexdrrZm", metadata!!.properties!!.creators!!.first().address)
    }
}