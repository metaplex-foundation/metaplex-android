package com.metaplex.sample

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.solana.networking.Network
import com.solana.vendor.TweetNaclFast
import org.bitcoinj.core.Base58
import org.json.JSONObject
import java.net.URL
import java.net.URLEncoder

private const val PHANTOM_URL = "phantom.app"
typealias Cluster = Network

sealed class Result<out Success, out Failure> {
    data class Success<out Success>(val value: Success) : Result<Success, Nothing>()
    data class Failure<out Failure>(val reason: Failure) : Result<Nothing, Failure>()
}

data class DappKeyPair(var publicKey: ByteArray, var secretKey: ByteArray)

interface PhantomDecryptor {
    fun decryptPayload(data: String, nonce: String, sharedSecretDapp: ByteArray) : Result<ByteArray, PhantomError>
}

class NaCLDecryptor : PhantomDecryptor {
    override fun decryptPayload(data: String, nonce: String, sharedSecretDapp: ByteArray) : Result<ByteArray, PhantomError> {
        return try {
            val sbox = TweetNaclFast.SecretBox(sharedSecretDapp)
            val decryptedData = sbox.open(Base58.decode(data), Base58.decode(nonce))

            Result.Success(decryptedData)
        } catch (err : Error) {
            Result.Failure(PhantomError.CouldNotDecrypt(err))
        }
    }
}

data class PhantomErrorResponse(val errorCode: String, val errorMessage: String)

sealed class PhantomError {
    data class HostNotSupported(val host: String) : PhantomError()
    data class SchemeNotFound(val scheme: String) : PhantomError()
    data class ErrorResponse(val phantomErrorResponse: PhantomErrorResponse) : PhantomError()
    data class CouldNotDecrypt(val error: Error) : PhantomError()
    data class CouldNotGenerateSharedSecret(val error: Error) : PhantomError()
    object InvalidParameters : PhantomError()
}

data class PhantomConnectResponseData(val public_key: String, val session: String)

sealed class PhantomResponse {
    data class OnConnect(val response: PhantomConnectResponseData, val sharedSecretDapp: String, val phantomEncryptionPublicKey: String) : PhantomResponse()
}

enum class PhantomAction {
    connect {
        override fun host() = "sampleonconnect"
    };
    abstract fun host() : String
}


class PhantomLoginViewModel : ViewModel() {
    val ownerPublicKey = MutableLiveData<String>(null)

    companion object {
        const val SESSION_SHARED_PREFS_FILE = "com.metaplex.sample.sessionStorage"
        const val PHANTOM_SHARED_PREFS_FILE = "com.metaplex.sample.phantomStorage"
        const val DAPP_PUBLIC_KEY = "dappPublicKey"
        const val DAPP_SECRET_KEY = "dappSecretKey"
        const val OWNER_PUBLIC_KEY = "ownerPublicKey"
        const val SESSION = "sessionId"
        const val PHANTOM_ENCRYPTION_PUBLIC_KEY = "phantomEncryptionPublicKey"
        const val SHARED_SECRET_DAPP = "sharedSecretDapp"
    }

    inner class PhantomDeepLink constructor(_urlSchema: String, _appUrl: URL, _publicKey: ByteArray, _secretKey: ByteArray, _cluster: Cluster) {
        val urlSchema: String
        val appUrl: String
        val cluster: Cluster
        val dappKeyPair: DappKeyPair
        val decryptor: PhantomDecryptor

        init {
            urlSchema = _urlSchema
            appUrl = _appUrl.toString()
            cluster = _cluster
            dappKeyPair = DappKeyPair(_publicKey, _secretKey)
            decryptor = NaCLDecryptor()
        }

        fun getConnectURL() : String {
            val queryItems = HashMap<String, String>()

            queryItems["dapp_encryption_public_key"] = Base58.encode(dappKeyPair.publicKey)
            queryItems["redirect_link"] = URLEncoder.encode("${urlSchema}://${PhantomAction.connect.host()}", "utf-8")
            queryItems["cluster"] = cluster.cluster
            queryItems["app_url"] = URLEncoder.encode(appUrl, "utf-8")

            val url = "https://${PHANTOM_URL}/ul/v1/${PhantomAction.connect}" +
                    "?app_url=${queryItems["app_url"]}" +
                    "&dapp_encryption_public_key=${queryItems["dapp_encryption_public_key"]}" +
                    "&redirect_link=${queryItems["redirect_link"]}" +
                    "&cluster=${queryItems["cluster"]}"

            return url
        }

        fun handleURL(url : Uri) : PhantomResponse? {
            return when(val phantomConnectResponse = parserHandleURL(url)) {
                is Result.Success -> phantomConnectResponse.value
                is Result.Failure -> {
                    Log.e("PHANTOMCONNECTIONERROR", phantomConnectResponse.reason.toString())
                    null
                }
            }
        }

        private fun parserHandleURL(url : Uri) : Result<PhantomResponse, PhantomError> {
            if (url.scheme != null && url.scheme != urlSchema) {
                return Result.Failure(PhantomError.SchemeNotFound(url.scheme.toString()))
            }

            val section = url.host

            val errorCodeQueryParam : String? = url.getQueryParameter("errorCode")
            val errorMessageQueryParam : String? = url.getQueryParameter("errorMessage")

            if (errorCodeQueryParam != null && errorMessageQueryParam != null) {
                return Result.Failure(PhantomError.ErrorResponse(PhantomErrorResponse(errorCodeQueryParam, errorMessageQueryParam)))
            }

            when (section) {
                PhantomAction.connect.host() -> {
                    val phantomEncryptionPublicKey = url.getQueryParameter("phantom_encryption_public_key")
                        ?: return Result.Failure(PhantomError.InvalidParameters)
                    val nonce = url.getQueryParameter("nonce") ?: return Result.Failure(PhantomError.InvalidParameters)
                    val data = url.getQueryParameter("data") ?: return Result.Failure(PhantomError.InvalidParameters)

                    val sharedSecretDapp = when (val sharedKeyResult =  generateSharedSecretDapp(phantomEncryptionPublicKey)) {
                        is Result.Success -> sharedKeyResult.value
                        is Result.Failure -> return Result.Failure(sharedKeyResult.reason)
                    }

                    val decryptedData = when (val decryptPayloadResult = decryptor.decryptPayload(data, nonce, sharedSecretDapp)) {
                        is Result.Success -> decryptPayloadResult.value
                        is Result.Failure -> return Result.Failure(decryptPayloadResult.reason)
                    }
                    val decryptedJson = JSONObject(decryptedData.decodeToString())
                    val phantomConnectResponseData = PhantomConnectResponseData(decryptedJson.get("public_key").toString(), decryptedJson.get("session").toString())

                    return Result.Success(PhantomResponse.OnConnect(phantomConnectResponseData, Base58.encode(sharedSecretDapp), phantomEncryptionPublicKey))
                }
                else -> {
                    return Result.Failure(PhantomError.HostNotSupported(section ?: ""))
                }
            }
        }

        private fun generateSharedSecretDapp(phantomEncryptionPublicKey : String) : Result<ByteArray, PhantomError> {
            return try {
                val box = TweetNaclFast.Box(Base58.decode(phantomEncryptionPublicKey), dappKeyPair.secretKey)
                val sharedSecret = box.before()

                Result.Success(sharedSecret)
            } catch (e : Error) {
                Result.Failure(PhantomError.CouldNotGenerateSharedSecret(e))
            }
        }
    }
}