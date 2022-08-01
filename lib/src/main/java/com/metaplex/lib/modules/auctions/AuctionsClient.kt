/*
 * AuctionsClient
 * Metaplex
 * 
 * Created by Funkatronics on 7/27/2022
 */

package com.metaplex.lib.modules.auctions

import com.metaplex.lib.drivers.solana.ConnectionKt
import com.metaplex.lib.drivers.solana.getAccountInfo
import com.metaplex.lib.modules.auctions.models.AuctionHouse
import com.metaplex.lib.programs.token_metadata.accounts.MetadataAccount
import com.metaplex.lib.programs.tokens.TokenProgram
import com.metaplex.lib.solana.Connection
import com.solana.core.PublicKey
import kotlinx.coroutines.*
import org.bitcoinj.core.Base58
import java.nio.charset.StandardCharsets

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
            return getAccountInfo<AuctionHouse>(address).map {
                it.data!! // safe unwrap, successful result will not have null
            }
        }
    }

    /**
     * Attempts to find an AuctionHouse account on chain via its [creator] address and
     * treasury [mint] address
     */
    suspend fun findAuctionHouseByCreatorAndMint(creator: PublicKey,
                                                 treasuryMint: PublicKey): Result<AuctionHouse> =
        findAuctionHouseByAddress(AuctionHouse.pda(creator, treasuryMint))

    /**
     * Async-callback version of [findAuctionHouseByAddress]
     */
    fun findAuctionHouseByAddress(address: PublicKey, onComplete: (Result<AuctionHouse>) -> Unit) =
        CoroutineScope(Dispatchers.IO).launch {
            onComplete(findAuctionHouseByAddress(address))
        }

    /**
     * Async-callback version of [findAuctionHouseByCreatorAndMint]
     */
    fun findAuctionHouseByCreatorAndMint(creator: PublicKey, mint: PublicKey,
                                         onComplete: (Result<AuctionHouse>) -> Unit) =
        CoroutineScope(Dispatchers.IO).launch {
            onComplete(findAuctionHouseByCreatorAndMint(creator, mint))
        }
}