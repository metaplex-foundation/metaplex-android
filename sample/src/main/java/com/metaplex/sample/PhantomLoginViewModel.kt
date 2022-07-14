package com.metaplex.sample

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.Intent
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
import org.json.JSONObject
import java.net.URL
import java.net.URLEncoder

private val PHANTOM_URL = "phantom.app"
typealias Cluster = Network

sealed class Result<out Success, out Failure> {
    data class Success<out Success>(val value: Success) : Result<Success, Nothing>()
    data class Failure<out Failure>(val reason: Failure) : Result<Nothing, Failure>()
}

data class DappKeyPair(val publicKey: ByteArray, val secretKey: ByteArray)

interface PhantomDecryptor {
    fun decryptPayload(data: String, nonce: String, phantomEncryptionPublickey : ByteArray, secretKey : ByteArray) : Result<ByteArray, PhantomError>
}

class NaCLDecryptor : PhantomDecryptor {
    // due to usage of tweetnacl-java library, we are not using shared key for decrypting like in iOS
    override fun decryptPayload(data: String, nonce: String, phantomEncryptionPublickey : ByteArray, secretKey : ByteArray) : Result<ByteArray, PhantomError> {
        try {
            val box = TweetNaclFast.Box(phantomEncryptionPublickey, secretKey)
            val decryptedData = box.open(Base58.decode(data), Base58.decode(nonce))

            return Result.Success(decryptedData)
        } catch (err : Error) {
            return Result.Failure(PhantomError.couldNotDecrypt(err))
        }
    }
}

data class PhantomErrorResponse(val errorCode: String, val errorMessage: String)

sealed class PhantomError {
    data class hostNotSupported(val host: String) : PhantomError()
    data class schemeNotFound(val scheme: String) : PhantomError()
    data class errorResponse(val phantomErrorResponse: PhantomErrorResponse) : PhantomError()
    data class couldNotDecrypt(val error: Error) : PhantomError()
    data class couldNotGenerateSharedSecret(val error: Error) : PhantomError()
    object invalidParameters : PhantomError()
}

data class PhantomConnectResponseData(val public_key: String, val session: String)

sealed class PhantomResponse {
    data class onConnect(val response: PhantomConnectResponseData, val sharedSecretDapp: String, val phantomEncryptionPublickey: String) : PhantomResponse()
}

enum class PhantomAction {
    connect {
        override fun host() = "onConnect"
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
    }


//    fun phantomDeepLinking() {
//        storeSession()
//        ownerPublicKey.value = "CN87nZuhnFdz74S9zn3bxCcd5ZxW55nwvgAv5C2Tz3K7"
//    }

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

        fun connect(){
            val queryItems = HashMap<String, String>()
            queryItems["dapp_encryption_public_key"] = URLEncoder.encode(Base58.encode(dappKeyPair.publicKey), "utf-8")
            queryItems["redirect_link"] = URLEncoder.encode("${urlSchema}://${PhantomAction.connect.host()}", "utf-8")
            queryItems["cluster"] = URLEncoder.encode(cluster.cluster, "utf-8")
            queryItems["app_url"] = URLEncoder.encode(appUrl, "utf-8")

            val url = "https://${PHANTOM_URL}/ul/v1/${PhantomAction.connect}" +
                    "?dapp_encryption_public_key=${queryItems["dapp_encryption_public_key"]}" +
                    "&redirect_link=${queryItems["redirect_link"]}" +
                    "&cluster=${cluster}" +
                    "&app_url=${queryItems["app_url"]}"

            launchURL(url)
        }

        fun handleURL(url : URL) {
            val phantomConnectResponse : Result<PhantomResponse, PhantomError> = parserHandleURL(url)

            when(phantomConnectResponse) {
                is Result.Success -> {
                    val phantomResponse = phantomConnectResponse.value
                    when(phantomResponse) {
                        is PhantomResponse.onConnect -> {
                            storeSession(phantomResponse.response.public_key, phantomResponse.response.session, phantomResponse.phantomEncryptionPublickey, phantomResponse.sharedSecretDapp)
                            ownerPublicKey.value = phantomResponse.response.public_key
                        }
                    }
                }
                is Result.Failure -> {
                    Log.i("?? ERROR ??", phantomConnectResponse.reason.toString())
                    Toast.makeText(context, "Cannot connect to Phantom!", Toast.LENGTH_LONG).show()
                }
            }
        }

        private fun launchURL(url: String) {
            if (url.startsWith("https://") || url.startsWith("http://")) {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            } else {
                Toast.makeText(context, "Invalid Url", Toast.LENGTH_SHORT).show()
            }
        }

        private fun parserHandleURL(url : URL) : Result<PhantomResponse, PhantomError> {
            if (url.protocol != null && url.protocol != urlSchema) {
                return Result.Failure(PhantomError.schemeNotFound(url.protocol))
            }

            val section = url.host
            val components = Uri.parse(url.toString())

            val errorCodeQueryParam : String? = components.getQueryParameter("errorCode")
            val errorMessageQueryParam : String? = components.getQueryParameter("errorMessage")

            if (errorCodeQueryParam != null && errorMessageQueryParam != null) {
                return Result.Failure(PhantomError.errorResponse(PhantomErrorResponse(errorCodeQueryParam, errorMessageQueryParam)))
            }

            when (section) {
                PhantomAction.connect.host() -> {
                    val phantomEncryptionPublickey = components.getQueryParameter("phantom_encryption_public_key")
                        ?: return Result.Failure(PhantomError.invalidParameters)
                    val nonce = components.getQueryParameter("nonce") ?: return Result.Failure(PhantomError.invalidParameters)
                    val data = components.getQueryParameter("data") ?: return Result.Failure(PhantomError.invalidParameters)

                    var res =  extractResultValue(generateSharedSecretDapp(phantomEncryptionPublickey))
                    val sharedSecretDapp : String
                    if (res is ByteArray) {
                        sharedSecretDapp = Base58.encode(res)
                    } else {
                        return Result.Failure(res as PhantomError)
                    }

                    res = decryptor.decryptPayload(data, nonce, Base58.decode(phantomEncryptionPublickey), dappKeyPair.secretKey)
                    val decryptedData : ByteArray
                    if (res is ByteArray) {
                        decryptedData = res
                    } else {
                        return Result.Failure(res as PhantomError)
                    }

                    val gson = Gson()
                    val phantomConnectResponseData = gson.fromJson(Base58.encode(decryptedData), PhantomConnectResponseData::class.java)

                    return Result.Success(PhantomResponse.onConnect(phantomConnectResponseData, sharedSecretDapp, phantomEncryptionPublickey))
                }
                else -> {
                    return Result.Failure(PhantomError.hostNotSupported(section ?: ""))
                }
            }
        }

        private fun generateSharedSecretDapp(phantomEncryptionPublickey : String) : Result<ByteArray, PhantomError> {
            try {
                val box = TweetNaclFast.Box(Base58.decode(phantomEncryptionPublickey), dappKeyPair.secretKey)
                val sharedSecret = box.before()

                return Result.Success(sharedSecret)
            } catch (e : Error) {
                return Result.Failure(PhantomError.couldNotGenerateSharedSecret(e))
            }
        }

        private fun extractResultValue(result: Result<ByteArray, PhantomError>) = when(result) {
            is Result.Success -> result.value
            is Result.Failure -> result.reason
        }

        private fun storeSession(ownerPublicKey: String, session: String, phantomEncryptionPublickey: String, sharedSecretDapp: String) {
            val preferences: SharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_FILE, MODE_PRIVATE)
            val editor: SharedPreferences.Editor = preferences.edit()
            editor.putString(OWNER_PUBLIC_KEY, ownerPublicKey)
            editor.putString(SESSION, session)
            editor.putString(PHANTOM_ENCRYPTION_PUBLIC_KEY, phantomEncryptionPublickey)
            editor.putString(SHARED_SECRET_DAPP, sharedSecretDapp)
            editor.apply()
        }
    }
}