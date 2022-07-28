/*
 * SolanaConnection
 * Metaplex
 * 
 * Created by Funkatronics on 7/27/2022
 */

package com.metaplex.lib.drivers.solana

import com.solana.core.PublicKey
import kotlinx.serialization.*

// Inlines let us hide the serialization complexity while still providing full control
// For example, to find an AuctionHouse by address:
//      val auctionHouse = connectionKt.getAccountInfo<AuctionHouse>(address)
// And optionally, to find an account and supply your own serializer :
//      val customAccount = connectionKt.getAccountInfo(customSerializer, address)
interface ConnectionKt {
    suspend fun <A> getAccountInfo(serializer: KSerializer<A>, account: PublicKey) : Result<A>
}

suspend inline fun <reified A> ConnectionKt.getAccountInfo(account: PublicKey): Result<A> =
    getAccountInfo(serializer(), account)