package com.metaplex.lib

import com.metaplex.lib.drivers.indenty.IdentityDriver
import com.metaplex.lib.drivers.storage.StorageDriver
import com.metaplex.lib.modules.fungibletokens.FungibleTokenClient
import com.metaplex.lib.modules.nfts.NftClient
import com.metaplex.lib.solana.Connection
import com.solana.core.PublicKey
import com.solana.models.buffer.BufferInfo
import com.solana.vendor.borshj.BorshCodable

class Metaplex(val connection: Connection,
               private var identityDriver: IdentityDriver,
               private var storageDriver: StorageDriver){

    val nft: NftClient by lazy { NftClient(this) }

    val fungibletoken: FungibleTokenClient by lazy { FungibleTokenClient(this) }

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

    fun <T: BorshCodable> getAccountInfo(account: PublicKey,
                                         decodeTo: Class<T>,
                                         onComplete: ((Result<BufferInfo<T>>) -> Unit)){
        this.connection.getAccountInfo(account, decodeTo, onComplete)
    }

    fun <T: BorshCodable> getMultipleAccountsInfo(
        accounts: List<PublicKey>,
        decodeTo: Class<T>,
        onComplete: ((Result<List<BufferInfo<T>?>>) -> Unit)
    ) {
        this.connection.getMultipleAccountsInfo(accounts, decodeTo, onComplete)
    }
}