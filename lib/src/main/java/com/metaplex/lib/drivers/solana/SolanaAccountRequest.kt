/*
 * SolanaAccountResponse
 * Metaplex
 * 
 * Created by Funkatronics on 7/28/2022
 */

package com.metaplex.lib.drivers.solana

import com.metaplex.lib.drivers.rpc.RpcRequest
import com.solana.models.RpcSendTransactionConfig
import kotlinx.serialization.json.*

class SolanaAccountRequest(
    accountAddress: String,
    encoding: RpcSendTransactionConfig.Encoding = RpcSendTransactionConfig.Encoding.base64,
    commitment: String = "max",
    length: Int? = null,
    offset: Int? = length?.let { 0 }
) : RpcRequest() {
    override val method = "getAccountInfo"
    override val params = buildJsonArray {
        add(accountAddress)
        addJsonObject {
            put("encoding", encoding.getEncoding())
            put("commitment", commitment)
            length?.let {
                putJsonObject("dataSlice") {
                    put("length", length)
                    put("offset", offset)
                }
            }
        }
    }
}