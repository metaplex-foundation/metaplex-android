@file:OptIn(ExperimentalCoroutinesApi::class)

package com.metaplex.lib.modules.nfts.operations

import com.metaplex.lib.*
import com.metaplex.lib.modules.nfts.models.Metadata
import com.metaplex.lib.modules.nfts.models.NFT
import com.solana.core.HotAccount
import com.util.airdrop
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class FindNftsByOwnerOnChainOperationHandlerTests {

    @Test
    fun testFindNftsByOwnerOnChainOperation() = runTest {
        // given
        val owner = HotAccount()
        val connection = MetaplexTestUtils.generateConnectionDriver()
        val metaplex = MetaplexTestUtils.generateMetaplexInstance(owner, connection)

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