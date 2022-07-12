package com.metaplex.sample

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PhantomLoginViewModel : ViewModel() {
    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    val authenticationState : MutableLiveData<AuthenticationState> by lazy {
        MutableLiveData<AuthenticationState>()
    }

    fun phantomDeepLinking() {
        authenticationState.value = AuthenticationState.AUTHENTICATED
    }
}