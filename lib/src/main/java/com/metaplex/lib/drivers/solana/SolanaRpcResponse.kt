/*
 * SolanaRpcSerializers
 * Metaplex
 * 
 * Created by Funkatronics on 7/22/2022
 */

package com.metaplex.lib.drivers.solana

import com.metaplex.lib.drivers.rpc.RpcResponse
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject

// TODO: still not happy with the deserialization of the solana response
//  it's working, but I know i can improve the API/devex

@Serializable
data class AccountInfo<D>(val data: D?, val executable: Boolean,
                          val lamports: Long, val owner: String?, val rentEpoch: Long)

typealias SolanaResponse = RpcResponse

val json = Json { ignoreUnknownKeys = true }

inline fun <reified D> SolanaResponse.value(deserializer: DeserializationStrategy<D>) =
    result?.jsonObject?.get("value")?.let { json.decodeFromJsonElement(deserializer, it) }