/*
 * SolanaAccountResponse
 * Metaplex
 * 
 * Created by Funkatronics on 7/28/2022
 */

package com.metaplex.lib.drivers.solana

import com.metaplex.lib.drivers.rpc.RpcRequest
import kotlinx.serialization.json.add
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.put

class SolanaAccountRequest(accountAddress: String, config: Map<String, String>? = null)
    : RpcRequest() {
    override val method = "getAccountInfo"
    override val params = buildJsonArray {
        add(accountAddress)
        config?.run { addJsonObject {
            forEach { (k, v) -> put(k, v) }
        } }
    }
}