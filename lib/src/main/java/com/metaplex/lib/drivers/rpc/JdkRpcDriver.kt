/*
 * JdkRpcDriver
 * Metaplex
 * 
 * Created by Funkatronics on 7/27/2022
 */

package com.metaplex.lib.drivers.rpc

import com.metaplex.lib.drivers.network.HttpPostRequest
import com.metaplex.lib.drivers.network.JdkHttpDriver
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL

/**
 * A [JsonRpcDriver] implemented using the native JDK [HttpURLConnection]
 *
 * @see JsonRpcDriver
 * @see java.net.HttpURLConnection
 *
 * @author Funkatronics
 */
class JdkRpcDriver(val url: String) : JsonRpcDriver {

    constructor(url: URL) : this(url.toString())

    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    override suspend fun <R> makeRequest(request: RpcRequest, resultSerializer: KSerializer<R>): RpcResponse<R> =
        JdkHttpDriver().makeHttpRequest(
            HttpPostRequest(
                url = url,
                properties = mapOf("Content-Type" to "application/json; charset=utf-8"),
                body = json.encodeToString(RpcRequest.serializer(), request)
            )
        ).run {
            json.decodeFromString(RpcResponse.serializer(resultSerializer), this)
        }
}