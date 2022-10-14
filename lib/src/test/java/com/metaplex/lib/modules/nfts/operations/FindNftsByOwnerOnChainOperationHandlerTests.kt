@file:OptIn(ExperimentalCoroutinesApi::class)

package com.metaplex.lib.modules.nfts.operations

import com.metaplex.lib.Metaplex
import com.metaplex.lib.drivers.indenty.KeypairIdentityDriver
import com.metaplex.lib.drivers.rpc.JdkRpcDriver
import com.metaplex.lib.drivers.solana.Commitment
import com.metaplex.lib.drivers.solana.SolanaConnectionDriver
import com.metaplex.lib.drivers.solana.TransactionOptions
import com.metaplex.lib.drivers.storage.MemoryStorageDriver
import com.metaplex.lib.modules.nfts.models.Metadata
import com.metaplex.lib.modules.nfts.models.NFT
import com.solana.core.Account
import com.util.airdrop
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import java.net.URL

class FindNftsByOwnerOnChainOperationHandlerTests {

    @Test
    fun testFindNftsByOwnerOnChainOperation() = runTest {
        // given
        val rpcUrl = URL("http://127.0.0.1:8899")
        val owner = Account()
        val connection = SolanaConnectionDriver(JdkRpcDriver(rpcUrl),
            transactionOptions = TransactionOptions(Commitment.CONFIRMED, skipPreflight = true)
        )
        val metaplex = Metaplex(connection,
            KeypairIdentityDriver(owner, connection), MemoryStorageDriver())

        // when
        connection.airdrop(owner.publicKey, 1f)
        val createdNft = metaplex.nft.create(Metadata("My NFT",
            uri = "http://example.com/1234abcd", sellerFeeBasisPoints = 250)).getOrThrow()

        val ownerNfts: List<NFT?> = FindNftsByOwnerOnChainOperationHandler(metaplex)
            .handle(owner.publicKey).getOrThrow()

        // then
        Assert.assertNotNull(ownerNfts.first())
        Assert.assertTrue(ownerNfts.size == 1)
        Assert.assertEquals(createdNft.mint, ownerNfts.first()?.mint)
        Assert.assertEquals(createdNft.metadataAccount.mint,
            ownerNfts.first()?.metadataAccount?.mint)
        Assert.assertEquals(createdNft.metadataAccount.update_authority,
            ownerNfts.first()?.metadataAccount?.update_authority)
    }
}