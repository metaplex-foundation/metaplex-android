/*
 * RpcRequest
 * Metaplex
 * 
 * Created by Funkatronics on 7/27/2022
 */

package com.metaplex.lib.drivers.rpc

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import java.util.*

//@Serializable
//abstract class RpcRequest {
//    abstract val method: String
//    abstract val params: Any?
//    val jsonrpc: String = "2.0"
//    val id: String = UUID.randomUUID().toString()
//}

//@Serializable
//sealed class RpcRequest {
//    abstract val method: String
//    abstract val params: Any?
//    val jsonrpc: String = "2.0"
//    val id: String = UUID.randomUUID().toString()
//}

//@Serializable
//class SolanaAccountRequest(override val params: JsonArray?)
//    : RpcRequest() {
//    override val method = "getAccountInfo"
//
//    constructor(accountAddress: String, config: Map<String, String>? = null) : this(
//        buildJsonArray {
//            add(accountAddress)
//            config?.run { addJsonObject {
//                forEach { (k, v) -> put(k, v) }
//            } }
//        }
//    )
//}

@Serializable
open class RpcRequest {
    open val method: String = ""
    open val params: JsonElement? = null
    val jsonrpc = "2.0"
    val id: String = UUID.randomUUID().toString()
}