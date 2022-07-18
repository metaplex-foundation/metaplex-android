package com.metaplex.sample

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.metaplex.sample.databinding.FragmentPhantomLoginBinding
import com.solana.networking.Network
import com.solana.vendor.TweetNaclFast
import org.bitcoinj.core.Base58
import java.net.URL


class PhantomLoginFragment : Fragment() {
    private var _binding: FragmentPhantomLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    private val viewModel by viewModels<PhantomLoginViewModel>()

    private val keyPair by lazy {
        TweetNaclFast.Box.keyPair()
    }
    private val phantom by lazy {
        viewModel.PhantomDeepLink(
            _urlSchema = "metaplex",
            _appUrl = URL("https://metaplex.com"),
            _publicKey = keyPair.publicKey,
            _secretKey = keyPair.secretKey,
            _cluster = Network.mainnetBeta)
    }

    companion object {
        const val TAG = "PhantomLoginFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhantomLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        observeAuthenticationState(view)
        isSessionAvailable()

        val uri = requireActivity().intent.data
        if (uri != null) {
            val preferences = getSharedPrefs(PhantomLoginViewModel.PHANTOM_SHARED_PREFS_FILE)
            val editor = preferences.edit()

            val dappPublicKey = preferences.getString(PhantomLoginViewModel.DAPP_PUBLIC_KEY, null)
            val dappSecretKey = preferences.getString(PhantomLoginViewModel.DAPP_SECRET_KEY, null)

            editor.clear()
            editor.apply()

            phantom.dappKeyPair.publicKey = Base58.decode(dappPublicKey)
            phantom.dappKeyPair.secretKey = Base58.decode(dappSecretKey)

            val phantomResponse: PhantomResponse? = phantom.handleURL(uri)
            if (phantomResponse != null) {
                when(phantomResponse) {
                    is PhantomResponse.OnConnect -> {
                        storeSession(phantomResponse.response.public_key, phantomResponse.response.session, phantomResponse.phantomEncryptionPublicKey, phantomResponse.sharedSecretDapp)
                        viewModel.ownerPublicKey.value = phantomResponse.response.public_key
                    }
                }
            } else {
                Toast.makeText(context, "Cannot connect to Phantom!", Toast.LENGTH_LONG).show()
            }
        }

        binding.loginWithPhantomBtn.setOnClickListener {
            val urlString = phantom.getConnectURL()

            val preferences = getSharedPrefs(PhantomLoginViewModel.PHANTOM_SHARED_PREFS_FILE)
            val editor = preferences.edit()
            editor.putString(PhantomLoginViewModel.DAPP_PUBLIC_KEY, Base58.encode(phantom.dappKeyPair.publicKey))
            editor.putString(PhantomLoginViewModel.DAPP_SECRET_KEY, Base58.encode(phantom.dappKeyPair.secretKey))
            editor.apply()

            launchURL(urlString, view)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)?.supportActionBar?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeAuthenticationState(view: View) {
        viewModel.ownerPublicKey.observe(viewLifecycleOwner, Observer<String> { ownerPublicKey ->
            if (ownerPublicKey != null) {
                showSnackbar(view, "Public Key: ${viewModel.ownerPublicKey.value.toString()}")
                val bundle = bundleOf("ownerPubKey" to ownerPublicKey.toString())
                navController.navigate(R.id.action_PhantomLoginFragment_to_FirstFragment, bundle)
            } else {
                Log.i(TAG, "Unauthenticated")
            }
        })
    }

    private fun isSessionAvailable() {
        val preferences = getSharedPrefs(PhantomLoginViewModel.SESSION_SHARED_PREFS_FILE)
        if (preferences.getString(PhantomLoginViewModel.OWNER_PUBLIC_KEY, null) != null && viewModel.ownerPublicKey.value == null) {
            viewModel.ownerPublicKey.value = preferences.getString(PhantomLoginViewModel.OWNER_PUBLIC_KEY, null)
        }
    }

    private fun launchURL(url: String, view: View) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } catch (e: Error) {
            Log.e(TAG, e.toString())
            showSnackbar(view, "Cannot launch Phantom Deep Link!")
        }
    }

    private fun showSnackbar(contextView: View, message: String) {
        Snackbar.make(contextView, message, Snackbar.LENGTH_LONG)
            .show()
    }

    private fun storeSession(ownerPublicKey: String, session: String, phantomEncryptionPublicKey: String, sharedSecretDapp: String) {
        val preferences = getSharedPrefs(PhantomLoginViewModel.SESSION_SHARED_PREFS_FILE)
        val editor: SharedPreferences.Editor = preferences.edit()

        editor.putString(PhantomLoginViewModel.OWNER_PUBLIC_KEY, ownerPublicKey)
        editor.putString(PhantomLoginViewModel.SESSION, session)
        editor.putString(PhantomLoginViewModel.PHANTOM_ENCRYPTION_PUBLIC_KEY, phantomEncryptionPublicKey)
        editor.putString(PhantomLoginViewModel.SHARED_SECRET_DAPP, sharedSecretDapp)

        editor.apply()
    }

    private fun getSharedPrefs(file: String): SharedPreferences {
        return requireActivity().getSharedPreferences(file, MODE_PRIVATE)
    }
}