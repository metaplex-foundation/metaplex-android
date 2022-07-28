/*
 * RpcResponse
 * Metaplex
 * 
 * Created by Funkatronics on 7/27/2022
 */

package com.metaplex.lib.drivers.rpc

import com.metaplex.lib.experimental.serialization.serializers.rpc.RpcErrorWithData
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

//@Serializable
//sealed class RpcResponse {
//    abstract val result: JsonElement?
//    abstract val error: RpcErrorWithData<Unit>?
//    abstract val id: String
//    val jsonrpc = "2.0"
//}

@Serializable
open class RpcResponse(
    open val result: JsonElement? = null,
    open val error: RpcErrorWithData<Unit>? = null,
    val id: String? = null
) {
    val jsonrpc = "2.0"
}