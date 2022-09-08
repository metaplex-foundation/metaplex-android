package com.metaplex.lib.modules.token

import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.modules.token.models.Token
import com.metaplex.lib.modules.token.operations.FindFungibleTokenByMintOnChainOperationHandler
import com.solana.core.PublicKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Token Client
 *
 * A client for interacting with Metaplex Tokens. This class also serves as a base class for other
 * types of tokens such as NFTs and SFTs
 *
 * @author Funkatronics
 */
open class TokenClient(private val connection: Connection,
                       private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    open suspend fun findByMint(mintKey: PublicKey): Result<Token> =
        FindFungibleTokenByMintOnChainOperationHandler(connection, dispatcher).handle(mintKey)
}