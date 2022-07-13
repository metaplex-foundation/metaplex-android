package com.metaplex.sample

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.solana.networking.Network
import org.bitcoinj.core.Base58
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
    fun decryptPayload(data: String, nonce: String, sharedSecretDapp: ByteArray) : Result<ByteArray, PhantomError>
}

class NaCLDecryptor : PhantomDecryptor {
    override fun decryptPayload(data: String, nonce: String, sharedSecretDapp: ByteArray) : Result<ByteArray, PhantomError> {
       return Result.Success(Base58.decode("Complete this function when tweetnacl is available."))
    }
}

data class PhantomErrorResponse(val errorCode: String, val errorMessage: String)

sealed class PhantomError {
    data class errorResponse(val phantomErrorResponse: PhantomErrorResponse) : PhantomError()
    data class couldNotDecrypt(val error: Error) : PhantomError()
    data class couldNotDecodeResponse(val error: Error) : PhantomError()
    data class couldNotGenerateSharedSecret(val error: Error) : PhantomError()
}

data class PhantomConnectResponseData(val public_key: String, val session: String)

sealed class PhantomResponse {
    data class onConnect(val response: PhantomConnectResponseData, val sharedSecretDapp: ByteArray, val phantomEncryptionPublickey: String) : PhantomResponse()
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


    fun phantomDeepLinking() {
        storeSession()
        ownerPublicKey.value = "CN87nZuhnFdz74S9zn3bxCcd5ZxW55nwvgAv5C2Tz3K7"
    }

    private fun storeSession() {
        val preferences: SharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_FILE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putString(OWNER_PUBLIC_KEY, "CN87nZuhnFdz74S9zn3bxCcd5ZxW55nwvgAv5C2Tz3K7")
        editor.putString(SESSION, "testsession")
        editor.putString(PHANTOM_ENCRYPTION_PUBLIC_KEY, "testsession")
        editor.putString(SHARED_SECRET_DAPP, "sharedsecretkey")
        editor.apply()
    }

    inner class PhantomDeepLink constructor(_urlSchema: String, _appUrl: URL, _publicKey: ByteArray, _secretKey: ByteArray, _cluster: Cluster) {
        val urlSchema: String
        val appUrl: String
        val cluster: Cluster
        val dappKeyPair: DappKeyPair
        val decryptor: PhantomDecryptor

        var onConnect: ((PhantomConnectResponseData) -> Void)? = null

        init {
            urlSchema = _urlSchema
            appUrl = _appUrl.toString()
            cluster = _cluster
            dappKeyPair = DappKeyPair(_publicKey, _secretKey)
            decryptor = NaCLDecryptor()
        }

        fun connect() {
            val queryItems = HashMap<String, String>()
            queryItems["dapp_encryption_public_key"] = URLEncoder.encode(Base58.encode(dappKeyPair.publicKey), "utf-8")
            queryItems["redirect_link"] = URLEncoder.encode(redirectLinkString(PhantomAction.connect), "utf-8")
            queryItems["cluster"] = URLEncoder.encode(cluster.cluster, "utf-8")
            queryItems["app_url"] = URLEncoder.encode(appUrl, "utf-8")

            val url = URL("https://${PHANTOM_URL}/ul/v1/${PhantomAction.connect}" +
                    "?dapp_encryption_public_key=${queryItems["dapp_encryption_public_key"]}" +
                    "&redirect_link=${queryItems["redirect_link"]}" +
                    "&cluster=${cluster}" +
                    "&app_url=${queryItems["app_url"]}")

            launchURL(url)
        }

        private fun launchURL(url: URL) {
            // Navigate to the deeplink
        }

        private fun redirectLinkString(host: PhantomAction) : String {
            return "${urlSchema}://${host.host()}"
        }
    }
}