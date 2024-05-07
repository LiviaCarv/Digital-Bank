package com.project.digitalbank.ui.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.project.digitalbank.R
import com.project.digitalbank.databinding.FragmentLoginBinding
import com.project.digitalbank.util.FirebaseHelper
import com.project.digitalbank.util.StateView
import com.project.digitalbank.util.hideKeyboard
import com.project.digitalbank.util.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    private fun initListener() {
        binding.btnLogin.setOnClickListener {
            validateData()
        }
        binding.txtCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.txtRecoverAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_recoverAccountFragment)
        }
    }

    private fun validateData() {
        binding.apply {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            if (email.isEmpty()) {
                showBottomSheet(message =getString(R.string.register_provide_email))
            } else if (password.isEmpty()) {
                showBottomSheet(message =getString(R.string.register_provide_password))

            } else {
                hideKeyboard()
               loginUser(email, password)
            }

        }
    }

    private fun loginUser(email: String, password: String) {

        loginViewModel.login(email, password).observe(viewLifecycleOwner) { stateView ->
            when(stateView) {
                is StateView.Loading -> binding.progressBar.isVisible = true
                is StateView.Success -> {
                    Toast.makeText(requireContext(), "Login...", Toast.LENGTH_SHORT).show()
                    binding.progressBar.isVisible = false
                    findNavController().navigate(R.id.action_global_homeFragment)
                }
                else -> {
                    binding.progressBar.isVisible = false
                    showBottomSheet(message = getString(FirebaseHelper.validError(stateView.message.toString())))

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}