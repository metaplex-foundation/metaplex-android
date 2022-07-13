package com.metaplex.sample

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.metaplex.sample.databinding.FragmentPhantomLoginBinding


class PhantomLoginFragment : Fragment() {
    private var _binding: FragmentPhantomLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    private val viewModel by viewModels<PhantomLoginViewModel>()

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

        binding.loginWithPhantomBtn.setOnClickListener {
            viewModel.phantomDeepLinking()
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun isSessionAvailable() {
        val preferences: SharedPreferences = requireActivity().getSharedPreferences(PhantomLoginViewModel.SHARED_PREFERENCE_FILE, MODE_PRIVATE)
        if (preferences.getString(PhantomLoginViewModel.OWNER_PUBLIC_KEY, null) != null && viewModel.ownerPublicKey.value == null) {
            viewModel.ownerPublicKey.value = preferences.getString(PhantomLoginViewModel.OWNER_PUBLIC_KEY, null)
        }
    }

    private fun observeAuthenticationState(view: View) {
        viewModel.ownerPublicKey.observe(viewLifecycleOwner, Observer<String> { ownerPublicKey ->
            if (ownerPublicKey != null) {
                showSnackbar(view)
                val bundle = bundleOf("ownerPubKey" to ownerPublicKey.toString())
                navController.navigate(R.id.action_PhantomLoginFragment_to_FirstFragment, bundle)
            } else {
                Log.i(TAG, "Unauthenticated")
            }
        })
    }

    private fun showSnackbar(contextView : View) {
        Snackbar.make(contextView, "Public Key: ${viewModel.ownerPublicKey.value.toString()}.", Snackbar.LENGTH_LONG)
            .show()
    }
}