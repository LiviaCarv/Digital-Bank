package com.project.digitalbank.ui.features.transfer.confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.project.digitalbank.R
import com.project.digitalbank.data.enum.TransactionType
import com.project.digitalbank.data.model.Transfer
import com.project.digitalbank.data.model.User
import com.project.digitalbank.databinding.FragmentConfirmTransferBinding
import com.project.digitalbank.util.FirebaseHelper
import com.project.digitalbank.util.GetMask
import com.project.digitalbank.util.StateView
import com.project.digitalbank.util.initToolBar
import com.project.digitalbank.util.showBottomSheet
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmTransferFragment : Fragment() {

    private var _binding: FragmentConfirmTransferBinding? = null
    private val binding get() = _binding!!
    private val confirmTransferViewModel: ConfirmTransferViewModel by viewModels()
    private val args: ConfirmTransferFragmentArgs by navArgs()
    private val tagPicasso = "tagPicasso"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConfirmTransferBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar(binding.toolbar, homeAsUpEnabled = true)
        configData(args.user)
        initListener()

    }

    private fun initListener() {
        binding.btnConfirm.setOnClickListener {
            getBalance()
        }
    }

    private fun getBalance() {
        confirmTransferViewModel.getBalance().observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is StateView.Success -> {
                    val currentBalance = stateView.data ?: 0f
                    if (currentBalance < args.amount) {
                        showBottomSheet(
                            message = getString(R.string.insufficient_balance_to_make_transfer),
                            onClick = { findNavController().popBackStack() })
                    } else {
                        val transfer = Transfer(
                            idUserRecipient = args.user.id,
                            idUserSender = FirebaseHelper.getUserId(),
                            value = args.amount,
                        )
                        saveTransfer(transfer)
                    }
                }

                else -> {
                    binding.progressBar.isVisible = false
                    showBottomSheet(message = getString(FirebaseHelper.validError(stateView.message.toString())))
                }
            }
        }
    }

    private fun saveTransfer(transfer: Transfer) {
        confirmTransferViewModel.saveTransfer(transfer).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                    binding.btnConfirm.isEnabled = false
                }

                is StateView.Success -> {
                    binding.btnConfirm.isEnabled = true
                    binding.progressBar.isVisible = false
                    updateTransfer(transfer)
                }

                else -> {
                    binding.progressBar.isVisible = false
                    showBottomSheet(message = getString(FirebaseHelper.validError(stateView.message.toString())))
                }
            }
        }
    }

    private fun updateTransfer(transfer: Transfer) {
        confirmTransferViewModel.updateTransfer(transfer).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                    binding.btnConfirm.isEnabled = false
                }

                is StateView.Success -> {
                    binding.btnConfirm.isEnabled = true
                    binding.progressBar.isVisible = false
                    val action = ConfirmTransferFragmentDirections.actionConfirmTransferFragmentToTransferReceiptFragment(transfer.id)
                    findNavController().navigate(action)
                }

                else -> {
                    binding.progressBar.isVisible = false
                    showBottomSheet(message = getString(FirebaseHelper.validError(stateView.message.toString())))
                }
            }
        }
    }

    private fun configData(user: User) {
        binding.txtUserTransfer.text = user.name
        binding.txtShowTransferValue.text =
            getString(R.string.text_account_balance_format, GetMask.getFormattedValue(args.amount))
        if (user.imageProfile.isNotEmpty()) {
            Picasso
                .get()
                .load(user.imageProfile)
                .tag(tagPicasso)
                .fit().centerCrop()
                .into(binding.imgDestIcon, object : Callback {
                    override fun onSuccess() {
                        binding.imgDestIcon.isVisible = true
                        binding.imgProgressBar.isVisible = false
                    }

                    override fun onError(e: Exception?) {
                        TODO("Not yet implemented")
                    }
                })
        } else {
            binding.imgDestIcon.setImageResource(R.drawable.ic_user_place_holder)
            binding.imgDestIcon.isVisible = true
            binding.imgProgressBar.isVisible = false
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        Picasso.get().cancelTag(tagPicasso)

        _binding = null
    }

}