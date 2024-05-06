package com.project.digitalbank.ui.user_profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.project.digitalbank.R
import com.project.digitalbank.data.model.User
import com.project.digitalbank.databinding.FragmentUserProfileBinding
import com.project.digitalbank.util.FirebaseHelper
import com.project.digitalbank.util.StateView
import com.project.digitalbank.util.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileFragment : Fragment() {
    private var _binding: FragmentUserProfileBinding? = null
    private val binding: FragmentUserProfileBinding get() = _binding!!
    private val profileViewModel: UserProfileViewModel by viewModels()


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
        getProfile()
    }

    private fun getProfile() {
        profileViewModel.getUserProfile().observe(viewLifecycleOwner) { stateView ->
            when(stateView) {
                is StateView.Loading-> {
                    binding.progressBar.isVisible = true
                }
                is StateView.Success -> {
                    binding.progressBar.isVisible = false
                    stateView.data?.let { configData(it) }

                }
                else -> {
                    binding.progressBar.isVisible = false

                    showBottomSheet(message = getString(FirebaseHelper.validError(stateView.message.toString())))
                }
            }

        }
    }

    private fun configData(user: User) {
        binding.apply {
            edtTextName.setText(user.name)
            edtTextTelephone.setText(user.phone)
            edtTextEmail.setText(user.email)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}