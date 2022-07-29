/*
 * AuctionsClient
 * Metaplex
 * 
 * Created by Funkatronics on 7/27/2022
 */

package com.metaplex.lib.modules.auctions

import com.metaplex.lib.drivers.solana.ConnectionKt
import com.metaplex.lib.drivers.solana.SolanaConnectionDriver
import com.metaplex.lib.drivers.solana.getAccountInfo
import com.metaplex.lib.modules.auctions.models.AuctionHouse
import com.metaplex.lib.solana.Connection
import com.solana.core.PublicKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * NFT Auctions Client
 *
 * @author Funkatronics
 */
class AuctionsClient(val connectionDriver: Connection) {

    /**
     * Attempts to find an AuctionHouse account on chain via its [address]
     */
    suspend fun findAuctionHouseByAddress(address: PublicKey): Result<AuctionHouse> {
        // temporary cast to ConnectionKt until suspend funs are merged into Connection
        (connectionDriver as ConnectionKt).apply {
            return getAccountInfo<AuctionHouse>(address).map { it.data!! }
        }
    }

    /**
     * Async-callback version of [findAuctionHouseByAddress]
     */
    fun findAuctionHouseByAddress(address: PublicKey, onComplete: (Result<AuctionHouse>) -> Unit) =
        CoroutineScope(Dispatchers.IO).launch {
            onComplete(findAuctionHouseByAddress(address))
        }
}