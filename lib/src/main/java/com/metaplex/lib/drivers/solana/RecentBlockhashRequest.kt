/*
 * RecentBlockhashRequest
 * Metaplex
 * 
 * Created by Funkatronics on 8/24/2022
 */

package com.metaplex.lib.drivers.solana

import com.metaplex.lib.drivers.rpc.RpcRequest
import com.metaplex.lib.serialization.serializers.solana.SolanaResponseSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

class RecentBlockhashRequest : RpcRequest() {
    override val method: String = "getRecentBlockhash"
}

@Serializable
// not currently using the fee calculator so just leaving it as json for now
internal data class BlockhashResponse(val blockhash: String, val feeCalculator: JsonElement)

internal fun BlockhashSerializer() = SolanaResponseSerializer(BlockhashResponse.serializer())