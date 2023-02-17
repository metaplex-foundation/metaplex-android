/*
 * JdkHttpDriver
 * metaplex-android
 * 
 * Created by Funkatronics on 11/6/2022
 */

package com.metaplex.lib.drivers.network

import kotlinx.coroutines.suspendCancellableCoroutine
import java.net.HttpURLConnection
import java.net.URL

/**
 * A [HttpNetworkDriver] implementation using the native JDK [HttpURLConnection]
 *
 * @see HttpNetworkDriver
 * @see java.net.HttpURLConnection
 *
 * @author Funkatronics
 */
class JdkHttpDriver : HttpNetworkDriver {
    override suspend fun makeHttpRequest(request: HttpRequest): String =
        suspendCancellableCoroutine { continuation ->

            with(URL(request.url).openConnection() as HttpURLConnection) {
                // config
                requestMethod = request.method
                request.properties.forEach { (key, value) ->
                    setRequestProperty(key, value)
                }

                // cancellation
                continuation.invokeOnCancellation { disconnect() }

                // send request body
                request.body?.runCatching {
                    doOutput = true
                    outputStream.write(toByteArray(Charsets.UTF_8))
                    outputStream.flush()
                    outputStream.close()
                }?.onFailure { error ->
                    continuation.resumeWith(Result.failure(error))
                    return@with
                }

                // read response
                val responseString = when (responseCode) {
                    HttpURLConnection.HTTP_OK -> inputStream.bufferedReader().use { it.readText() }
                    else -> errorStream.bufferedReader().use { it.readText() }
                }

                continuation.resumeWith(Result.success(responseString))
            }
        }
}
