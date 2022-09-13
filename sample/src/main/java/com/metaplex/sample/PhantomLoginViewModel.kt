package com.metaplex.sample

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PhantomLoginViewModel : ViewModel() {
    val ownerPublicKey = MutableLiveData<String>(null)

    fun handleUrl(context: FragmentActivity, phantom: PhantomDeepLink, uri: Uri) {
        when(val phantomConnectResponse = phantom.parserHandleURL(uri)) {
            is Result.Success -> {
                when(val phantomResponse = phantomConnectResponse.value) {
                    is PhantomResponse.OnConnect -> {
                        phantom.storeSession(context, phantomResponse.response.public_key, phantomResponse.response.session, phantomResponse.phantomEncryptionPublicKey, phantomResponse.sharedSecretDapp)
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
}