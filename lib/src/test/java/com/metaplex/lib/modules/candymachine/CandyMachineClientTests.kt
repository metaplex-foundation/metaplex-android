/*
 * CandyMachineClientTests
 * Metaplex
 * 
 * Created by Funkatronics on 9/8/2022
 */

@file:OptIn(ExperimentalCoroutinesApi::class)

package com.metaplex.lib.modules.candymachine

import com.metaplex.lib.MetaplexTestUtils
import com.metaplex.lib.drivers.indenty.IdentityDriver
import com.metaplex.lib.drivers.indenty.KeypairIdentityDriver
import com.metaplex.lib.drivers.indenty.ReadOnlyIdentityDriver
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.drivers.solana.SolanaConnectionDriver
import com.metaplex.lib.extensions.epochMillis
import com.metaplex.lib.generateConnectionDriver
import com.metaplex.lib.modules.candymachines.CandyMachineClient
import com.metaplex.lib.modules.candymachines.models.*
import com.metaplex.lib.modules.candymachines.refresh
import com.metaplex.lib.modules.nfts.NftClient
import com.metaplex.lib.modules.nfts.models.Metadata
import com.metaplex.mock.driver.rpc.MockErrorRpcDriver
import com.solana.core.HotAccount
import com.solana.core.PublicKey
import com.solana.networking.RPCEndpoint
import com.util.airdrop
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import java.time.ZoneId
import java.time.ZonedDateTime

class CandyMachineClientTests {

//    @Before
//    fun prepare() {
//        jenerateCandyMachine()
//        jenerateCandyGuard()
//    }

    //region UNIT
    @Test
    fun testFindCandyMachineV2ByAddressHandlesAndReturnsError() = runTest {
        // given
        val expectedErrorMessage = "An Error Occurred"
        val cmAddress = PublicKey(ByteArray(PublicKey.PUBLIC_KEY_LENGTH))
        val connection = SolanaConnectionDriver(MockErrorRpcDriver(expectedErrorMessage))

        val client = CandyMachineClient(connection,
            ReadOnlyIdentityDriver(HotAccount().publicKey, connection)
        )

        // when
        val result = client.findByAddress(cmAddress)

        // then
        Assert.assertTrue(result.isFailure)
        Assert.assertEquals(expectedErrorMessage, result.exceptionOrNull()!!.message)
    }

    @Test
    fun testCandyMachineCreateHandlesAndReturnsError() = runTest {
        // given
        val signer = HotAccount()
        val expectedErrorMessage = "An Error Occurred"
        val connection = SolanaConnectionDriver(MockErrorRpcDriver(expectedErrorMessage))
        val client = CandyMachineClient(connection, KeypairIdentityDriver(signer, connection))

        // when
        val result = client.create(
            333,
            1L,
            collection = signer.publicKey,
            collectionUpdateAuthority = signer.publicKey
        )

        // then
        Assert.assertTrue(result.isFailure)
        Assert.assertEquals(expectedErrorMessage, result.exceptionOrNull()?.message)
    }

    @Test
    fun testCandyMachineInsertItemsHandlesAndReturnsError() = runTest {
        // given
        val signer = HotAccount()
        val expectedErrorMessage = "An Error Occurred"
        val connection = SolanaConnectionDriver(MockErrorRpcDriver(expectedErrorMessage))
        val client = CandyMachineClient(connection, KeypairIdentityDriver(signer, connection))

        // when
        val result = client.insertItems(
            CandyMachine(signer.publicKey, signer.publicKey, signer.publicKey, 333.toUShort(), 1L,
                collectionMintAddress = signer.publicKey, collectionUpdateAuthority = signer.publicKey),
            listOf(CandyMachineItem("An NFT", "test.com"))
        )

        // then
        Assert.assertTrue(result.isFailure)
        Assert.assertEquals(expectedErrorMessage, result.exceptionOrNull()?.message)
    }

    @Test
    fun testCandyMachineMintNftHandlesAndReturnsError() = runTest {
        // given
        val signer = HotAccount()
        val expectedErrorMessage = "An Error Occurred"
        val connection = SolanaConnectionDriver(MockErrorRpcDriver(expectedErrorMessage))
        val client = CandyMachineClient(connection, KeypairIdentityDriver(signer, connection))

        // when
        val result = client.mintNft(
            CandyMachine(signer.publicKey, signer.publicKey, signer.publicKey, 333.toUShort(), 1L,
                collectionMintAddress = signer.publicKey, collectionUpdateAuthority = signer.publicKey)
        )

        // then
        Assert.assertTrue(result.isFailure)
        Assert.assertEquals(expectedErrorMessage, result.exceptionOrNull()?.message)
    }
    //endregion

    //region INTEGRATION
    // LOOKUP
    @Test
    fun testFindCandyMachineByAddressReturnsValidCandyMachine() = runTest {
        // given
        val cmAddress = PublicKey("4Add8hdxC44H3DcGfgWTvn2GNBfofk5uu2iatEW9LCYz")
        val connection = MetaplexTestUtils.generateConnectionDriver(RPCEndpoint.devnetSolana.url)
        val client = CandyMachineClient(connection,
            ReadOnlyIdentityDriver(HotAccount().publicKey, connection))

        // when
        val candyMachine: CandyMachine? = client.findByAddress(cmAddress).getOrNull()

        // then
        Assert.assertNotNull(candyMachine)
    }

    // CREATE
    @Test
    fun testCandyMachineCreateCreatesValidCandyMachine() = runTest {
        // given
        val signer = HotAccount()
        val connection = MetaplexTestUtils.generateConnectionDriver()
        val identityDriver = KeypairIdentityDriver(signer, connection)
        val client = CandyMachineClient(connection, identityDriver)
        val expectedMintAuthority = CandyGuard.pda(signer.publicKey).address

        // when
        connection.airdrop(signer.publicKey, 10f)
        val nft = createCollectionNft(connection, identityDriver).getOrThrow()
        val candyMachine = client.create(333, 5000, nft.mint, signer.publicKey).getOrThrow()
        val actualCandyMachine = client.findByAddress(candyMachine.address).getOrNull()

        // then
        Assert.assertNotNull(actualCandyMachine)
        Assert.assertEquals(candyMachine.configLineSettings, actualCandyMachine?.configLineSettings)
        Assert.assertEquals(expectedMintAuthority, candyMachine.mintAuthority)
        Assert.assertEquals(expectedMintAuthority, actualCandyMachine?.mintAuthority)
    }

    @Test
    fun testCandyMachineCreateWithoutCandyGuard() = runTest {
        // given
        val signer = HotAccount()
        val connection = MetaplexTestUtils.generateConnectionDriver()
        val identityDriver = KeypairIdentityDriver(signer, connection)
        val client = CandyMachineClient(connection, identityDriver)

        // when
        connection.airdrop(signer.publicKey, 10f)
        val nft = createCollectionNft(connection, identityDriver).getOrThrow()
        val candyMachine = client.create(333, 5000, nft.mint,
            signer.publicKey, withoutCandyGuard = true).getOrThrow()

        val actualCandyMachine = client.findByAddress(candyMachine.address).getOrNull()

        // then
        Assert.assertNotNull(actualCandyMachine)
        Assert.assertEquals(candyMachine.configLineSettings, actualCandyMachine?.configLineSettings)
        Assert.assertEquals(candyMachine.authority, candyMachine.mintAuthority)
        Assert.assertEquals(candyMachine.mintAuthority, actualCandyMachine?.mintAuthority)
    }

    //region SET COLLECTION
    @Test
    fun testCandyMachineSetCollectionUpdatesCandyMachineCollection() = runTest {
        // given
        val signer = HotAccount()
        val connection = MetaplexTestUtils.generateConnectionDriver()
        val identityDriver = KeypairIdentityDriver(signer, connection)
        val client = CandyMachineClient(connection, identityDriver)

        // when
        connection.airdrop(signer.publicKey, 1f)
        val nft = createCollectionNft(connection, identityDriver).getOrThrow()
        val newCollection = createCollectionNft(connection, identityDriver).getOrThrow()
        val candyMachine =
            client.create(200, 2, nft.mint, signer.publicKey).map {
                client.setCollection(it, newCollection.mint)
                client.refresh(it).getOrNull()
            }.getOrThrow()

        // then
        Assert.assertEquals(newCollection.mint, candyMachine?.collectionMintAddress)
    }
    //endregion

    //region INSERT ITEMS
    @Test
    fun testCandyMachineInsertItemsCanAddItemsToCandyMachine() = runTest {
        // given
        val signer = HotAccount()
        val connection = MetaplexTestUtils.generateConnectionDriver()
        val identityDriver = KeypairIdentityDriver(signer, connection)
        val client = CandyMachineClient(connection, identityDriver)

        val expectedItems = listOf(
            CandyMachineItem("Degen #1", "https://example.com/degen/1"),
            CandyMachineItem("Degen #2", "https://example.com/degen/2")
        )

        // when
        connection.airdrop(signer.publicKey, 1f)
        val nft = createCollectionNft(connection, identityDriver).getOrThrow()
        val candyMachine =
            client.create(200, 2, nft.mint, signer.publicKey).map {
                client.insertItems(it, expectedItems)
                client.refresh(it).getOrNull()
            }.getOrThrow()

        // then
        Assert.assertEquals(expectedItems, candyMachine?.items)
    }

    @Test
    fun testCandyMachineInsertItemsSequentiallyAddsItemsToCandyMachine() = runTest {
        // given
        val signer = HotAccount()
        val connection = MetaplexTestUtils.generateConnectionDriver()
        val identityDriver = KeypairIdentityDriver(signer, connection)
        val client = CandyMachineClient(connection, identityDriver)

        val expectedItems = listOf(
            CandyMachineItem("Degen #1", "https://example.com/degen/1"),
            CandyMachineItem("Degen #2", "https://example.com/degen/2")
        )

        // when
        connection.airdrop(signer.publicKey, 1f)
        val nft = createCollectionNft(connection, identityDriver).getOrThrow()
        val candyMachine = client.create(200, 2, nft.mint, signer.publicKey).map {
            client.insertItems(it, listOf(expectedItems[0])) // insert first item
            client.refresh(it).map { // refresh cm (so we have updated data for insertion)
                client.insertItems(it, listOf(expectedItems[1])) // insert second item
            }
            client.refresh(it).getOrThrow()
        }.getOrThrow()

        // then
        Assert.assertEquals(expectedItems, candyMachine.items)
    }
    //endregion

    // MINT
    @Test
    fun testCandyMachineMintNftMintsAndReturnsNft() = runTest {
        // given
        val signer = HotAccount()
        val connection = MetaplexTestUtils.generateConnectionDriver()
        val identityDriver = KeypairIdentityDriver(signer, connection)
        val client = CandyMachineClient(connection, identityDriver)

        // when
        connection.airdrop(signer.publicKey, 1f)
        val nft = createCollectionNft(connection, identityDriver).getOrThrow()
        val mintResult = client.create(250, 1, nft.mint,
            signer.publicKey, withoutCandyGuard = true).map {
            client.insertItems(it, listOf(
                CandyMachineItem("My NFT", "https://example.com/mynft"),
            ))
            client.mintNft(it).getOrThrow()
        }.getOrThrow()

        // then
        Assert.assertNotNull(mintResult)
        Assert.assertEquals(nft.mint, mintResult.collection?.key)
    }

    //region CANDY GUARDS
    @Test
    fun testCreateEmptyCandyGuard() = runTest {
        // given
        val signer = HotAccount()
        val connection = MetaplexTestUtils.generateConnectionDriver()
        val identityDriver = KeypairIdentityDriver(signer, connection)
        val client = CandyMachineClient(connection, identityDriver)

        val guards = listOf<Guard>()

        // when
        connection.airdrop(signer.publicKey, 0.5f)
        val candyGuard = client.createCandyGuard(guards).map {
            client.findCandyGuardByBaseAddress(it.base).getOrThrow()
        }.getOrThrow()

        // then
        Assert.assertNotNull(candyGuard)
        Assert.assertEquals(guards, candyGuard.defaultGuards)
    }

    @Test
    fun testCreateEmptyCandyGuardWithAuthority() = runTest {
        // given
        val signer = HotAccount()
        val authority = HotAccount()
        val connection = MetaplexTestUtils.generateConnectionDriver()
        val identityDriver = KeypairIdentityDriver(signer, connection)
        val client = CandyMachineClient(connection, identityDriver)

        // when
        connection.airdrop(signer.publicKey, 0.5f)
        val candyGuard = client.createCandyGuard(listOf(), mapOf(), authority.publicKey).map {
            client.findCandyGuardByBaseAddress(it.base).getOrThrow()
        }.getOrThrow()

        // then
        Assert.assertNotNull(candyGuard)
        Assert.assertEquals(authority.publicKey, candyGuard.authority)
    }

    @Test
    fun testCreateCandyGuardWithAllGuards() = runTest {
        // given
        val signer = HotAccount()
        val connection = MetaplexTestUtils.generateConnectionDriver()
        val identityDriver = KeypairIdentityDriver(signer, connection)
        val client = CandyMachineClient(connection, identityDriver)

        val guards = listOf(
            Guard.BotTax(1000000, false),
            Guard.SolPayment(1000000000, HotAccount().publicKey),
            Guard.TokenPayment(5, HotAccount().publicKey, HotAccount().publicKey),
            Guard.StartDate(ZonedDateTime.of(2022, 9, 5, 20, 0, 0, 0, ZoneId.of("UTC")).epochMillis()),
            Guard.ThirdPartySigner(HotAccount().publicKey),
            Guard.TokenGate(5, HotAccount().publicKey),
            Guard.Gatekeeper(HotAccount().publicKey, true),
            Guard.EndDate(ZonedDateTime.of(2022, 9, 5, 20, 0, 0, 0, ZoneId.of("UTC")).epochMillis()),
            Guard.AllowList(List(32) { 42 }),
            Guard.MintLimit(1, 5),
            Guard.NftPayment(HotAccount().publicKey, HotAccount().publicKey),
            Guard.RedeemedAmount(100),
            Guard.AddressGate(HotAccount().publicKey),
            Guard.NftGate(HotAccount().publicKey),
            Guard.NftBurn(HotAccount().publicKey),
            Guard.TokenBurn(1, HotAccount().publicKey)
        )

        // when
        connection.airdrop(signer.publicKey, 0.5f)
        val candyGuard: CandyGuard = client.createCandyGuard(guards).map {
            client.findCandyGuardByBaseAddress(it.base).getOrThrow()
        }.getOrThrow()

        // then
        Assert.assertNotNull(candyGuard)
        Assert.assertEquals(guards, candyGuard.defaultGuards)
    }

    @Test
    fun testCreateCandyGuardWithGuardsIsOrderAgnostic() = runTest {
        // given
        val signer = HotAccount()
        val connection = MetaplexTestUtils.generateConnectionDriver()
        val identityDriver = KeypairIdentityDriver(signer, connection)
        val client = CandyMachineClient(connection, identityDriver)

        val guards = listOf(
            Guard.NftBurn(HotAccount().publicKey),
            Guard.NftGate(HotAccount().publicKey),
            Guard.SolPayment(1000000000, HotAccount().publicKey),
            Guard.BotTax(1000000, false)
        )

        // when
        connection.airdrop(signer.publicKey, 0.5f)
        val candyGuard: CandyGuard = client.createCandyGuard(guards).map {
            client.findCandyGuardByBaseAddress(it.base).getOrThrow()
        }.getOrThrow()

        // then
        Assert.assertNotNull(candyGuard)
        Assert.assertEquals(guards.sortedBy { it.idlType.ordinal }, candyGuard.defaultGuards)
    }

    @Test
    fun testCreateCandyGuardWithGuardGroups() = runTest {
        // given
        val signer = HotAccount()
        val connection = MetaplexTestUtils.generateConnectionDriver()
        val identityDriver = KeypairIdentityDriver(signer, connection)
        val client = CandyMachineClient(connection, identityDriver)

        val guards = listOf(
            Guard.BotTax(1000000, false),
            Guard.EndDate(ZonedDateTime.of(2022, 9, 6, 16, 0, 0, 0, ZoneId.of("UTC")).epochMillis())
        )

        val groups = mapOf(
            "VIP" to listOf(
                Guard.StartDate(ZonedDateTime.of(2022, 9, 5, 16, 0, 0, 0, ZoneId.of("UTC")).epochMillis()),
                Guard.SolPayment(1000000000, signer.publicKey),
                Guard.AllowList(List(32) { 42 })
            ),
            "WLIST" to listOf(
                Guard.StartDate(ZonedDateTime.of(2022, 9, 5, 18, 0, 0, 0, ZoneId.of("UTC")).epochMillis()),
                Guard.SolPayment(2000000000, signer.publicKey),
                Guard.TokenGate(1, HotAccount().publicKey)
            ),
            "PUBLIC" to listOf(
                Guard.StartDate(ZonedDateTime.of(2022, 9, 5, 20, 0, 0, 0, ZoneId.of("UTC")).epochMillis()),
                Guard.SolPayment(3000000000, signer.publicKey),
                Guard.Gatekeeper(HotAccount().publicKey, false)
            )
        )

        // when
        connection.airdrop(signer.publicKey, 0.5f)
        val candyGuard = client.createCandyGuard(guards, groups).map {
            client.findCandyGuardByBaseAddress(it.base).getOrThrow()
        }.getOrThrow()

        // then
        Assert.assertNotNull(candyGuard)
        Assert.assertEquals(guards, candyGuard.defaultGuards)
        Assert.assertEquals(
            groups.mapValues { it.value.sortedBy { it::class.simpleName } },
            candyGuard.groups?.mapValues { it.value.sortedBy { it::class.simpleName } }
        )
    }

    @Test
    fun testWrapCandyGuard() = runTest {
        // given
        val signer = HotAccount()
        val connection = MetaplexTestUtils.generateConnectionDriver()
        val identityDriver = KeypairIdentityDriver(signer, connection)
        val client = CandyMachineClient(connection, identityDriver)

        // when
        connection.airdrop(signer.publicKey, 10f)
        val nft = createCollectionNft(connection, identityDriver).getOrThrow()
        val candyMachine = client.create(333, 5000, nft.mint,
            signer.publicKey, withoutCandyGuard = true).getOrThrow()
        val candyGuard = client.createCandyGuard(listOf(), mapOf()).getOrThrow()

        client.wrapCandyGuard(candyGuard, candyMachine.address)

        val finalCandyMachine = client.refresh(candyMachine).getOrThrow()

        //then
        Assert.assertNotNull(finalCandyMachine)
        Assert.assertEquals(candyGuard.authority, finalCandyMachine.authority)
        Assert.assertEquals(CandyGuard.pda(candyGuard.base).address, finalCandyMachine.mintAuthority)
    }

    @Test
    fun testWrapCandyGuardWithAuthority() = runTest {
        // given
        val signer = HotAccount()
        val authority = HotAccount()
        val connection = MetaplexTestUtils.generateConnectionDriver()
        val identityDriver = KeypairIdentityDriver(signer, connection)
        val client = CandyMachineClient(connection, identityDriver)

        // when
        connection.airdrop(signer.publicKey, 10f)
        val nft = createCollectionNft(connection, identityDriver).getOrThrow()
        val candyMachine = client.create(333, 5000, nft.mint,
            signer.publicKey, authority.publicKey, withoutCandyGuard = true).getOrThrow()
        val candyGuard = client.createCandyGuard(listOf(), mapOf(), authority.publicKey).getOrThrow()

        client.wrapCandyGuard(candyGuard, candyMachine.address, authority)

        val finalCandyMachine = client.refresh(candyMachine).getOrThrow()

        //then
        Assert.assertNotNull(finalCandyMachine)
        Assert.assertEquals(candyGuard.authority, finalCandyMachine.authority)
        Assert.assertEquals(CandyGuard.pda(candyGuard.base).address, finalCandyMachine.mintAuthority)
    }
    //endregion
    //endregion

    private suspend fun createCollectionNft(connection: Connection, identityDriver: IdentityDriver) =
        NftClient(connection, identityDriver).create(
            Metadata("My NFT", uri = "http://example.com/sd8756fsuyvvbf37684",
                sellerFeeBasisPoints = 250), true
        )
}