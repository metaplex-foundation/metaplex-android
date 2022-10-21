package com.metaplex.lib

import com.metaplex.lib.drivers.indenty.IdentityDriver
import com.metaplex.lib.drivers.storage.StorageDriver
import com.metaplex.lib.modules.auctions.AuctionsClient
import com.metaplex.lib.modules.token.TokenClient
import com.metaplex.lib.modules.nfts.NftClient
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.modules.candymachines.CandyMachineClient
import com.metaplex.lib.modules.candymachinesv2.CandyMachineV2Client
import com.solana.core.PublicKey
import com.solana.models.buffer.BufferInfo
import com.solana.vendor.borshj.BorshCodable

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

    @Deprecated(ASYNC_CALLBACK_DEPRECATION_MESSAGE, ReplaceWith("connection.getAccountInfo(account)"))
    fun <T: BorshCodable> getAccountInfo(account: PublicKey,
                                         decodeTo: Class<T>,
                                         onComplete: ((Result<BufferInfo<T>>) -> Unit)){
        this.connection.getAccountInfo(account, decodeTo, onComplete)
    }

    @Deprecated(ASYNC_CALLBACK_DEPRECATION_MESSAGE, ReplaceWith("connection.getMultipleAccountsInfo(accounts)"))
    fun <T: BorshCodable> getMultipleAccountsInfo(
        accounts: List<PublicKey>,
        decodeTo: Class<T>,
        onComplete: ((Result<List<BufferInfo<T>?>>) -> Unit)
    ) {
        this.connection.getMultipleAccountsInfo(accounts, decodeTo, onComplete)
    }
}