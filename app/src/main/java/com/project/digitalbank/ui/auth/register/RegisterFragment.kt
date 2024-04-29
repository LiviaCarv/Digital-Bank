package com.project.digitalbank.ui.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.project.digitalbank.databinding.FragmentRegisterBinding
import com.project.digitalbank.util.initToolBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

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
            val phoneNumber = edtTextRegisterTelephone.text.toString().trim()
            val password = edtTextRegisterPassword.text.toString().trim()
            val confirmationPassword = edtTextRegisterConfPassword.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "Please provide your name", Toast.LENGTH_SHORT).show()
            } else if (email.isEmpty()) {
                Toast.makeText(requireContext(), "Please provide a valid e-mail", Toast.LENGTH_SHORT).show()
            } else if (phoneNumber.isEmpty()) {
                Toast.makeText(requireContext(), "Please provide your phone number", Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty()){
                Toast.makeText(requireContext(), "Please provide a valid password", Toast.LENGTH_SHORT).show()
            }  else if (confirmationPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Please confirm your password", Toast.LENGTH_SHORT).show()
            } else if (confirmationPassword != password) {
                Toast.makeText(requireContext(), "Password different", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(requireContext(), "Creating account...", Toast.LENGTH_SHORT).show()

            }

        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}