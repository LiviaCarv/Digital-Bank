package com.project.digitalbank.ui.auth.recover

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
import com.project.digitalbank.databinding.FragmentRecoverAccountBinding
import com.project.digitalbank.util.FirebaseHelper
import com.project.digitalbank.util.StateView
import com.project.digitalbank.util.initToolBar
import com.project.digitalbank.util.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecoverAccountFragment : Fragment() {
    private var _binding: FragmentRecoverAccountBinding? = null
    private val binding get() = _binding!!
    private val recoverViewModel: RecoverViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecoverAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar(toolbar = binding.toolbar)
        initListener()
    }

    private fun initListener() {
        binding.btnLogin.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        binding.apply {
            val email = editTextEmail.text.toString().trim()
            if (email.isEmpty()) {
                showBottomSheet(message=getString(R.string.register_provide_email))
            } else {
                recoverAccount(email)

            }

        }
    }

    private fun recoverAccount(email: String) {
        recoverViewModel.recoverAccount(email).observe(viewLifecycleOwner) { stateView ->
            when(stateView) {
                is StateView.Loading -> binding.progressBar.isVisible = true
                is StateView.Success -> {
                    showBottomSheet(message=getString(R.string.recover_email_sent))
                    binding.progressBar.isVisible = false
                }
                else -> {
                    binding.progressBar.isVisible = false
                    showBottomSheet(message = getString(FirebaseHelper.validError(stateView.message.toString())))
                }
            }
            binding.progressBar.isVisible = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}