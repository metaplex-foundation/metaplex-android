package com.metaplex.lib.drivers.storage

import com.metaplex.lib.ASYNC_CALLBACK_DEPRECATION_MESSAGE
import com.metaplex.lib.shared.ResultWithCustomError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import java.io.IOException
import java.net.URL
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

sealed class StorageDriverError: Exception() {
    object InvalidURL: StorageDriverError()
    class CanNotParse(val error: Throwable): StorageDriverError()
    object DataAndResponseAndErrorAreNull: StorageDriverError()
    class NetworkingError(val error: Throwable): StorageDriverError()
    object JsonParsedError: StorageDriverError()
}

data class NetworkingResponse(
    val data: String,
    val code: Int,
)
interface StorageDriver {
    suspend fun download(url: URL): Result<NetworkingResponse>

    @Deprecated(ASYNC_CALLBACK_DEPRECATION_MESSAGE, ReplaceWith("download(url)"))
    fun download(url: URL, onComplete: (ResultWithCustomError<NetworkingResponse, StorageDriverError>) -> Unit)
}

class MemoryStorageDriver: StorageDriver {
    override suspend fun download(url: URL): Result<NetworkingResponse> =
        Result.success(NetworkingResponse("", 200))

    @Deprecated(ASYNC_CALLBACK_DEPRECATION_MESSAGE, ReplaceWith("download(url)"))
    override fun download(
        url: URL,
        onComplete: (ResultWithCustomError<NetworkingResponse, StorageDriverError>) -> Unit
    ) {
        onComplete(ResultWithCustomError.success(NetworkingResponse("", 200)))
    }
}

class OkHttpSharedStorageDriver(private val httpClient: OkHttpClient = OkHttpClient())
    : StorageDriver {

    override suspend fun download(url: URL): Result<NetworkingResponse> =
        suspendCancellableCoroutine { scope ->
            val request: Request = Request.Builder().url(url).get().build()
            httpClient.newCall(request).apply {
                scope.invokeOnCancellation {
                    cancel()
                }

                enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        scope.resumeWithException(StorageDriverError.NetworkingError(e))
                    }

                    override fun onResponse(call: Call, response: Response) {
                        response.body?.let { body ->
                            scope.resume(Result.success(NetworkingResponse(body.string(), response.code)))
                        }?:run {
                            scope.resumeWithException(StorageDriverError.DataAndResponseAndErrorAreNull)
                        }
                    }
                })
            }
        }

    @Deprecated(ASYNC_CALLBACK_DEPRECATION_MESSAGE, ReplaceWith("download(url)"))
    override fun download(
        url: URL,
        onComplete: (ResultWithCustomError<NetworkingResponse, StorageDriverError>) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            download(url).onSuccess {
                onComplete(ResultWithCustomError.success(it))
            }.onFailure { error ->
                onComplete(ResultWithCustomError.failure(error as? StorageDriverError ?: StorageDriverError.NetworkingError(error)))
            }
        }
    }
}