package com.metaplex.sample

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.solana.networking.Network
import com.solana.vendor.TweetNaclFast
import org.bitcoinj.core.Base58
import java.net.URL
import java.net.URLEncoder

private const val PHANTOM_URL = "phantom.app"
typealias Cluster = Network

sealed class Result<out Success, out Failure> {
    data class Success<out Success>(val value: Success) : Result<Success, Nothing>()
    data class Failure<out Failure>(val reason: Failure) : Result<Nothing, Failure>()
}

data class DappKeyPair(val publicKey: ByteArray, val secretKey: ByteArray)

interface PhantomDecryptor {
    fun decryptPayload(data: String, nonce: String, sharedSecretDapp: ByteArray) : Result<ByteArray, PhantomError>
}

class NaCLDecryptor : PhantomDecryptor {
    override fun decryptPayload(data: String, nonce: String, sharedSecretDapp: ByteArray) : Result<ByteArray, PhantomError> {
        return try {
//            val box = TweetNaclFast.Box(phantomEncryptionPublicKey, secretKey)
//            val decryptedData = box.open(Base58.decode(data), Base58.decode(nonce))

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


class PhantomLoginViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    val ownerPublicKey = MutableLiveData<String>(null)

    companion object {
        const val SHARED_PREFERENCE_FILE = "com.metaplex.sample.sessionStorage"
        const val OWNER_PUBLIC_KEY = "ownerPublicKey"
        const val SESSION = "sessionId"
        const val PHANTOM_ENCRYPTION_PUBLIC_KEY = "phantomEncryptionPublicKey"
        const val SHARED_SECRET_DAPP = "sharedSecretDapp"
        const val TAG = "PhantomLoginViewModel"
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

        fun getConnectURL() : String{
            val queryItems = HashMap<String, String>()
            queryItems["dapp_encryption_public_key"] = URLEncoder.encode(Base58.encode(dappKeyPair.publicKey), "utf-8")
            queryItems["redirect_link"] = URLEncoder.encode("${urlSchema}://${PhantomAction.connect.host()}", "utf-8") // https://sampleonconnect
            queryItems["cluster"] = URLEncoder.encode(cluster.cluster, "utf-8")
            queryItems["app_url"] = URLEncoder.encode(appUrl, "utf-8")

            val url = "https://${PHANTOM_URL}/ul/v1/${PhantomAction.connect}" +
                    "?dapp_encryption_public_key=${queryItems["dapp_encryption_public_key"]}" +
                    "&redirect_link=${queryItems["redirect_link"]}" +
                    "&cluster=${queryItems["cluster"]}" +
                    "&app_url=${queryItems["app_url"]}"
            return url
        }

        fun handleURL(url : URL) {
            when(val phantomConnectResponse = parserHandleURL(url)) {
                is Result.Success -> {
                    when(val phantomResponse = phantomConnectResponse.value) {
                        is PhantomResponse.OnConnect -> {
                            Log.i(TAG, phantomResponse.response.public_key)
                            storeSession(phantomResponse.response.public_key, phantomResponse.response.session, phantomResponse.phantomEncryptionPublicKey, phantomResponse.sharedSecretDapp)
                            ownerPublicKey.value = phantomResponse.response.public_key
                        }
                    }
                }
                is Result.Failure -> {
                    Log.e("PHANTOMCONNECTIONERROR", phantomConnectResponse.reason.toString())
                    Toast.makeText(context, "Cannot connect to Phantom!", Toast.LENGTH_LONG).show()
                }
            }
        }

        private fun parserHandleURL(url : URL) : Result<PhantomResponse, PhantomError> {
            if (url.protocol != null && url.protocol != urlSchema) {
                return Result.Failure(PhantomError.SchemeNotFound(url.protocol))
            }

            val section = url.host
            val components = Uri.parse(url.toString())

            val errorCodeQueryParam : String? = components.getQueryParameter("errorCode")
            val errorMessageQueryParam : String? = components.getQueryParameter("errorMessage")

            if (errorCodeQueryParam != null && errorMessageQueryParam != null) {
                return Result.Failure(PhantomError.ErrorResponse(PhantomErrorResponse(errorCodeQueryParam, errorMessageQueryParam)))
            }

            when (section) {
                PhantomAction.connect.host() -> {
                    val phantomEncryptionPublicKey = components.getQueryParameter("phantom_encryption_public_key")
                        ?: return Result.Failure(PhantomError.InvalidParameters)
                    val nonce = components.getQueryParameter("nonce") ?: return Result.Failure(PhantomError.InvalidParameters)
                    val data = components.getQueryParameter("data") ?: return Result.Failure(PhantomError.InvalidParameters)

                    val sharedSecretDapp = when (val sharedKeyResult =  generateSharedSecretDapp(phantomEncryptionPublicKey)) {
                        is Result.Success -> sharedKeyResult.value
                        is Result.Failure -> return Result.Failure(sharedKeyResult.reason)
                    }

//                    val decryptedData = when (val decryptPayloadResult = decryptor.decryptPayload(data, nonce, sharedSecretDapp)) {
//                        is Result.Success -> decryptPayloadResult.value
//                        is Result.Failure -> return Result.Failure(decryptPayloadResult.reason)
//                    }
//
//                    val gson = Gson()
//                    val phantomConnectResponseData = gson.fromJson(Base58.encode(decryptedData), PhantomConnectResponseData::class.java)

                    // Currently it's hardcoded because I'm yet to figure out how to decrypt the response.
                    val phantomConnectResponseData = PhantomConnectResponseData("CmXXJy2gZkcmU2Hpa8ebbJGPa46rdQTVu7dZ8uXufnQh", "testsession")

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

        private fun storeSession(ownerPublicKey: String, session: String, phantomEncryptionPublicKey: String, sharedSecretDapp: String) {
            val preferences: SharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_FILE, MODE_PRIVATE)
            val editor: SharedPreferences.Editor = preferences.edit()

            editor.putString(OWNER_PUBLIC_KEY, ownerPublicKey)
            editor.putString(SESSION, session)
            editor.putString(PHANTOM_ENCRYPTION_PUBLIC_KEY, phantomEncryptionPublicKey)
            editor.putString(SHARED_SECRET_DAPP, sharedSecretDapp)

            editor.apply()
        }
    }
}