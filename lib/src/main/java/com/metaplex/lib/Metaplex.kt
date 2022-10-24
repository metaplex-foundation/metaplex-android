package com.metaplex.lib

import com.metaplex.lib.drivers.indenty.IdentityDriver
import com.metaplex.lib.drivers.storage.StorageDriver
import com.metaplex.lib.modules.auctions.AuctionsClient
import com.metaplex.lib.modules.token.TokenClient
import com.metaplex.lib.modules.nfts.NftClient
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.modules.candymachines.CandyMachineClient
import com.metaplex.lib.modules.candymachinesv2.CandyMachineV2Client

class Metaplex(val connection: Connection,
               private var identityDriver: IdentityDriver,
               private var storageDriver: StorageDriver){

    val nft: NftClient by lazy { NftClient(connection, identityDriver) }
    val tokens: TokenClient by lazy { TokenClient(connection) }
    val auctions: AuctionsClient by lazy { AuctionsClient(connection, identityDriver) }
    val candyMachinesV2: CandyMachineV2Client by lazy { CandyMachineV2Client(connection, identityDriver) }
    val candyMachines: CandyMachineClient by lazy { CandyMachineClient(connection, identityDriver) }

    fun identity() = this.identityDriver

    fun setIdentity(identityDriver: IdentityDriver): IdentityDriver {
        this.identityDriver = identityDriver
        return this.identityDriver
    }

    fun storage() = this.storageDriver

    fun setStorage(storageDriver: StorageDriver): StorageDriver {
        this.storageDriver = storageDriver
        return this.storageDriver
    }
}