package com.project.digitalbank.ui.user_profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.project.digitalbank.R
import com.project.digitalbank.data.model.User
import com.project.digitalbank.databinding.FragmentUserProfileBinding
import com.project.digitalbank.util.FirebaseHelper
import com.project.digitalbank.util.StateView
import com.project.digitalbank.util.initToolBar
import com.project.digitalbank.util.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileFragment : Fragment() {
    private var _binding: FragmentUserProfileBinding? = null
    private val binding: FragmentUserProfileBinding get() = _binding!!
    private val userProfileViewModel: UserProfileViewModel by viewModels()
    private var user: User? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar(binding.toolbar)
        getProfile()
        initListener()
    }

    private fun initListener() {
        binding.btnUpdateInfo.setOnClickListener {
            if (user != null) {
                validateData()
            }
        }
    }

    private fun getProfile() {
        userProfileViewModel.getUserProfile().observe(viewLifecycleOwner) { stateView ->
            when(stateView) {
                is StateView.Loading-> {
                    binding.progressBar.isVisible = true
                }
                is StateView.Success -> {
                    binding.progressBar.isVisible = false
                    stateView.data?.let {
                        user = it
                        configData()
                    }
                }
                else -> {
                    binding.progressBar.isVisible = false

                    showBottomSheet(message = getString(FirebaseHelper.validError(stateView.message.toString())))
                }
            }

        }
    }

    private fun validateData() {
        binding.apply {
            val name = edtTextName.text.toString().trim()
            val phoneNumber = edtTextTelephone.unMaskedText

            if (name.isEmpty()) {
                showBottomSheet(message = getString(R.string.register_provide_name))
            }  else if (phoneNumber?.isEmpty() == true) {
                showBottomSheet(message = getString(R.string.register_provide_phone))
            } else if (phoneNumber?.length != 11) {
                showBottomSheet(message = getString(R.string.register_phone_number_invalid))
            } else {
                user?.name = name
                user?.phone = phoneNumber
                user?.let {
                    confirmProfileChanges()
                }
            }

        }
    }

    private fun saveProfile() {
       user?.let {
           userProfileViewModel.saveProfile(it).observe(viewLifecycleOwner) { stateView ->
               when (stateView) {
                   is StateView.Loading -> {
                       binding.progressBar.isVisible = true
                   }
                   is StateView.Success -> {
                       binding.progressBar.isVisible = false
                       Toast.makeText(requireContext(), "Profile updated", Toast.LENGTH_SHORT).show()
                   }
                   else -> {
                       binding.progressBar.isVisible = false
                       showBottomSheet(message = getString(FirebaseHelper.validError(stateView.message.toString())))
                   }
               }
           }
       }
    }

    private fun confirmProfileChanges() {
        showBottomSheet(
            message = getString(R.string.text_message_changes),
            titleDialog = R.string.title_dialog_confirm_changes,
            titleButton = R.string.txt_bottom_sheet_ok,
            onClick = {
                saveProfile()
            }
        )
    }

    private fun configData() {
        user?.let {
            binding.apply {
                edtTextName.setText(it.name)
                edtTextTelephone.setText(it.phone)
                edtTextEmail.setText(it.email)
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}