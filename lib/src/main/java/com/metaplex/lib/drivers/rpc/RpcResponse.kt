/*
 * RpcResponse
 * Metaplex
 * 
 * Created by Funkatronics on 7/27/2022
 */

package com.metaplex.lib.drivers.rpc

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

typealias DefaultRpcResponse = RpcResponse<JsonElement>

@Serializable
data class RpcError(val code: Int, val message: String)

@Serializable
open class RpcResponse<R>(
    open val result: R? = null,
    open val error: RpcError? = null,
    val id: String? = null
) {
    val jsonrpc = "2.0"
}