package com.metaplex.sample

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PhantomLoginViewModel : ViewModel() {
    val ownerPublicKey = MutableLiveData<String>(null)

    fun phantomDeepLinking() {
        ownerPublicKey.value = "CN87nZuhnFdz74S9zn3bxCcd5ZxW55nwvgAv5C2Tz3K7"
    }
}