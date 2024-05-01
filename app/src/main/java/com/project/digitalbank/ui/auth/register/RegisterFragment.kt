package com.project.digitalbank.ui.auth.register

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
import com.project.digitalbank.data.model.User
import com.project.digitalbank.data.model.Wallet
import com.project.digitalbank.databinding.FragmentRegisterBinding
import com.project.digitalbank.ui.user_profile.UserProfileViewModel
import com.project.digitalbank.ui.wallet.WalletViewModel
import com.project.digitalbank.util.FirebaseHelper
import com.project.digitalbank.util.StateView
import com.project.digitalbank.util.initToolBar
import com.project.digitalbank.util.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val registerViewModel: RegisterViewModel by viewModels()
    private val userProfileViewModel: UserProfileViewModel by viewModels()
    private val walletViewModel: WalletViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar(binding.toolbar)
        initListener()
    }

    private fun initListener() {
        binding.btnCreateAccount.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        binding.apply {
            val name = edtTextRegisterName.text.toString().trim()
            val email = edtTextRegisterEmail.text.toString().trim()
            val phoneNumber = edtTextRegisterTelephone.unMaskedText
            val password = edtTextRegisterPassword.text.toString().trim()
            val confirmationPassword = edtTextRegisterConfPassword.text.toString().trim()
            if (name.isEmpty()) {
                showBottomSheet(message = getString(R.string.register_provide_name))
            } else if (email.isEmpty()) {
                showBottomSheet(message = getString(R.string.register_provide_email))
            } else if (phoneNumber?.isEmpty() == true) {
                showBottomSheet(message = getString(R.string.register_provide_phone))
            } else if (phoneNumber?.length != 11) {
                showBottomSheet(message = getString(R.string.register_phone_number_invalid))
            } else if (password.isEmpty()) {
                showBottomSheet(message = getString(R.string.register_provide_password))
            } else if (confirmationPassword.isEmpty()) {
                showBottomSheet(message = getString(R.string.register_confirm_password_empty))
            } else if (confirmationPassword != password) {
                showBottomSheet(message = getString(R.string.register_confirm_password_wrong))
            } else {
                val user = User(name, email, phoneNumber, password)
                registerUser(name, email, phoneNumber, password)
            }

        }
    }

    // saves user in firebase authentication
    private fun registerUser(name: String, email: String, phone: String, password: String) {

        registerViewModel.register(name, email, phone, password)
            .observe(viewLifecycleOwner) { stateView ->
                when (stateView) {
                    is StateView.Loading -> binding.progressBar.isVisible = true
                    is StateView.Success -> {
                        stateView.data?.let {
                            saveProfile(it)
                        }
                    }

                    else -> {
                        binding.progressBar.isVisible = false
                        showBottomSheet(message = getString(FirebaseHelper.validError(stateView.message.toString())))
                    }
                }
            }
    }

    // saves user's profile in firebase database
    private fun saveProfile(user: User) {
        userProfileViewModel.saveProfile(user).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {}
                is StateView.Success -> {
                    initWallet()
                }
                else -> {
                    binding.progressBar.isVisible = false
                    showBottomSheet(message = getString(FirebaseHelper.validError(stateView.message.toString())))
                }
            }
        }
    }

    private fun initWallet() {
        walletViewModel.initWallet(Wallet(userId = FirebaseHelper.getUserId()))
            .observe(viewLifecycleOwner) { stateView ->
                when (stateView) {
                    is StateView.Loading -> {}
                    is StateView.Success -> {
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