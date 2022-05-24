package com.metaplex.lib.drivers.storage

import com.metaplex.lib.shared.ResultWithCustomError
import okhttp3.*
import java.io.IOException
import java.net.URL

sealed class StorageDriverError: Exception() {
    object InvalidURL: StorageDriverError()
    class CanNotParse(val error: Exception): StorageDriverError()
    object DataAndResponseAndErrorAreNull: StorageDriverError()
    class NetworkingError(val error: Exception): StorageDriverError()
    object JsonParsedError: StorageDriverError()
}

data class NetworkingResponse(
    val data: String,
    val code: Int,
)
interface StorageDriver {
    fun download(url: URL, onComplete: (ResultWithCustomError<NetworkingResponse, StorageDriverError>) -> Unit)
}

class MemoryStorageDriver: StorageDriver {
    override fun download(
        url: URL,
        onComplete: (ResultWithCustomError<NetworkingResponse, StorageDriverError>) -> Unit
    ) {
        onComplete(ResultWithCustomError.success(NetworkingResponse("", 200)))
    }
}

class OkHttpSharedStorageDriver(private val httpClient: OkHttpClient = OkHttpClient()): StorageDriver{
    override fun download(
        url: URL,
        onComplete: (ResultWithCustomError<NetworkingResponse, StorageDriverError>) -> Unit
    ) {
        val request: Request = Request.Builder().url(url).get().build()
        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onComplete(ResultWithCustomError.failure(StorageDriverError.NetworkingError(e)))
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { body ->
                    onComplete(ResultWithCustomError.success(NetworkingResponse(body.string(), response.code)))
                }?:run {
                    onComplete(ResultWithCustomError.failure(StorageDriverError.DataAndResponseAndErrorAreNull))
                }
            }
        })
    }
}