/*
 * FindNftsByCandyMachineOperationHnadlerTests
 * Metaplex
 * 
 * Created by Funkatronics on 11/14/2022
 */

package com.metaplex.lib.modules.nfts.operations

import com.metaplex.lib.MetaplexTestUtils
import com.metaplex.lib.drivers.solana.SolanaConnectionDriver
import com.metaplex.lib.generateConnectionDriver
import com.metaplex.lib.generateMetaplexInstance
import com.metaplex.lib.modules.candymachines.models.CandyMachineItem
import com.metaplex.lib.modules.nfts.models.Metadata
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.shared.OperationError
import com.metaplex.mock.driver.rpc.MockErrorRpcDriver
import com.solana.core.HotAccount
import com.solana.core.PublicKey
import com.util.airdrop
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class FindNftsByCandyMachineOperationHandlerTests {

    @Test
    fun testFindAllByOwnerHandlesAndReturnsError() = runTest {
        // given
        val expectedErrorMessage = "An Error Occurred"
        val owner = PublicKey(ByteArray(PublicKey.PUBLIC_KEY_LENGTH))
        val connection = SolanaConnectionDriver(MockErrorRpcDriver(expectedErrorMessage))

        // when
        val result = FindNftsByCandyMachineOnChainOperationHandler(connection)
            .handle(FindNftsByCandyMachineInput(owner, 3))

        // This is really bad, OperationError needs to be refactored. An exception should not wrap
        // another exception into a custom property. Getting the actual error out is so gross:
        val actualExceptionUnwrapped =
            (result.exceptionOrNull() as? OperationError.GetFindNftsByCreatorOperation)?.exception

        // then
        Assert.assertTrue(result.isFailure)
        Assert.assertEquals(expectedErrorMessage, actualExceptionUnwrapped?.message)
    }
    //endregion

    @Test
    fun testFindNftsByCandyMachineV2OnChainOperation() = runTest {
        // given
        val owner = HotAccount()
        val connection = MetaplexTestUtils.generateConnectionDriver()
        val metaplex = MetaplexTestUtils.generateMetaplexInstance(owner, connection)

        // when
        connection.airdrop(owner.publicKey, 1f)

        val candyMachine =
            metaplex.candyMachinesV2.create(1, 250, 1).getOrThrow()

        val createdNft = metaplex.candyMachinesV2.mintNft(candyMachine).getOrThrow()

        val ownerNfts: List<NFT?> = FindNftsByCandyMachineOnChainOperationHandler(connection)
            .handle(FindNftsByCandyMachineInput(candyMachine.address, 2)).getOrThrow()

        // then
        Assert.assertNotNull(ownerNfts.first())
        Assert.assertTrue(ownerNfts.size == 1)
        Assert.assertEquals(createdNft.mint, ownerNfts.first()?.mint)
        Assert.assertEquals(createdNft.metadataAccount.mint,
            ownerNfts.first()?.metadataAccount?.mint)
        Assert.assertEquals(createdNft.metadataAccount.update_authority,
            ownerNfts.first()?.metadataAccount?.update_authority)
    }

    @Test
    fun testFindNftsByCandyMachineOnChainOperation() = runTest {
        // given
        val owner = HotAccount()
        val connection = MetaplexTestUtils.generateConnectionDriver()
        val metaplex = MetaplexTestUtils.generateMetaplexInstance(owner, connection)

        // when
        connection.airdrop(owner.publicKey, 1f)

        val collection = metaplex.nft.create(
            Metadata("My NFT", uri = "http://example.com/sd8756fsuyvvbf37684",
                sellerFeeBasisPoints = 250), true
        ).getOrThrow()

        val candyMachine = metaplex.candyMachines.create(250, 1,
            collection.mint, collection.updateAuthority, withoutCandyGuard = true).getOrThrow()

        metaplex.candyMachines.insertItems(candyMachine,
            listOf(CandyMachineItem("Degen #1", "https://example.com/degen/1")))

        val createdNft = metaplex.candyMachines.mintNft(candyMachine).getOrThrow()

        val ownerNfts: List<NFT?> = FindNftsByCandyMachineOnChainOperationHandler(connection)
            .handle(FindNftsByCandyMachineInput(candyMachine.address, 3)).getOrThrow()

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