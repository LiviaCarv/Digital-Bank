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
import com.project.digitalbank.util.StateView
import com.project.digitalbank.util.initToolBar
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
        initToolBar(binding.toolbar)
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
                Toast.makeText(requireContext(), "Please provide a valid email", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(requireContext(), "Sending email...", Toast.LENGTH_SHORT).show()
                    binding.progressBar.isVisible = false
                }
                else -> Toast.makeText(requireContext(), stateView.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}