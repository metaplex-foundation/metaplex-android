@file:OptIn(ExperimentalCoroutinesApi::class)

package com.metaplex.lib.modules.nfts.operations

import com.metaplex.lib.*
import com.metaplex.lib.drivers.solana.SolanaConnectionDriver
import com.metaplex.lib.modules.nfts.models.Metadata
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.shared.OperationError
import com.metaplex.mock.driver.rpc.MockErrorRpcDriver
import com.solana.core.HotAccount
import com.solana.core.PublicKey
import com.util.airdrop
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class FindNftsByOwnerOnChainOperationHandlerTests {

    @Test
    fun testFindAllByOwnerHandlesAndReturnsError() = runTest {
        // given
        val expectedErrorMessage = "An Error Occurred"
        val owner = PublicKey(ByteArray(PublicKey.PUBLIC_KEY_LENGTH))
        val connection = SolanaConnectionDriver(MockErrorRpcDriver(expectedErrorMessage))

        // when
        val result = FindNftsByOwnerOnChainOperationHandler(connection).handle(owner)

        // This is really bad, OperationError needs to be refactored. An exception should not wrap
        // another exception into a custom property. Getting the actual error out is so gross:
        val actualExceptionUnwrapped =
            (result.exceptionOrNull() as? OperationError.GetFindNftsByOwnerOperation)?.exception

        // then
        Assert.assertTrue(result.isFailure)
        Assert.assertEquals(expectedErrorMessage, actualExceptionUnwrapped?.message)
    }
    //endregion

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