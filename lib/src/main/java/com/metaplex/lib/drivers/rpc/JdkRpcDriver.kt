/*
 * JdkRpcDriver
 * Metaplex
 * 
 * Created by Funkatronics on 7/27/2022
 */

package com.metaplex.lib.drivers.rpc

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.resumeWithException

/**
 * A [JsonRpcDriver] implemented using the native JDK [HttpURLConnection]
 *
 * @see JsonRpcDriver
 * @see java.net.HttpURLConnection
 *
 * @author Funkatronics
 */
class JdkRpcDriver(val url: URL) : JsonRpcDriver {

    private val json = Json {
        encodeDefaults = true
    }

    override suspend fun <R: RpcResponse> makeRequest(request: RpcRequest, responseSerializer: KSerializer<R>): R =
        suspendCancellableCoroutine { continuation ->

            println("JdkRpcDriver: makeRequest")

            with(url.openConnection() as HttpURLConnection) {
                // config
                setRequestProperty("Content-Type", "application/json; charset=utf-8")
                requestMethod = "POST"
                doOutput = true

                // cancellation
                continuation.invokeOnCancellation { disconnect() }

                // send request body
                outputStream.write(json.encodeToString(RpcRequest.serializer(), request).apply {
                    println("http connection params : $this")
                }.toByteArray())
                outputStream.close()

                // read response
                val responseString = inputStream.bufferedReader().use { it.readText() }

                // TODO: should check response code and/or errorStream for errors
                println("URL : $url")
                println("Response Code : $responseCode")
                println("input stream : $responseString")

                val result = json.decodeFromString(responseSerializer, responseString)

                result.error?.let { error ->
                    continuation.resumeWithException(Error(error.message))
                }

                result.result?.let {
                    println("json result : $result")
                    continuation.resumeWith(Result.success(result))
                }
            }
        }
}