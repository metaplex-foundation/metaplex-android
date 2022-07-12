package com.metaplex.sample

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.metaplex.sample.databinding.FragmentPhantomLoginBinding
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar


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

    private fun observeAuthenticationState(view: View) {

        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when(authenticationState) {
                PhantomLoginViewModel.AuthenticationState.AUTHENTICATED -> {
                    Log.i(TAG, "Authenticated")
                    showSnackbar(view)
                    navController.navigate(R.id.action_PhantomLoginFragment_to_FirstFragment)
                }
                PhantomLoginViewModel.AuthenticationState.UNAUTHENTICATED -> Log.i(TAG, "Unauthenticated")
                else -> Log.e(TAG, "New $authenticationState state that doesn't require any UI changes")
            }
        })
    }

    private fun showSnackbar(contextView : View) {
        Snackbar.make(contextView, getString(R.string.snackbar_text), Snackbar.LENGTH_LONG)
            .show()
    }
}