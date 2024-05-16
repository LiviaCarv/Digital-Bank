package com.project.digitalbank.ui.features.transfer.receipt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.project.digitalbank.R
import com.project.digitalbank.data.enum.TransactionType
import com.project.digitalbank.data.model.Transfer
import com.project.digitalbank.data.model.User
import com.project.digitalbank.databinding.FragmentTransferReceiptBinding
import com.project.digitalbank.util.FirebaseHelper
import com.project.digitalbank.util.GetMask
import com.project.digitalbank.util.StateView
import com.project.digitalbank.util.initToolBar
import com.project.digitalbank.util.showBottomSheet
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransferReceiptFragment : Fragment() {

    private var _binding: FragmentTransferReceiptBinding? = null
    private val binding get() = _binding!!
    private val args: TransferReceiptFragmentArgs by navArgs()
    private val transferReceiptViewModel: TransferReceiptViewModel by viewModels()
    private val tagPicasso = "tagPicasso"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTransferReceiptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar(toolbar = binding.toolbar, homeAsUpEnabled = false)
        initListener()
        getTransfer()
    }

    private fun initListener() {
        binding.btnConfirm.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun getTransfer() {
        transferReceiptViewModel.getTransfer(args.transferId)
            .observe(viewLifecycleOwner) { stateView ->
                when (stateView) {
                    is StateView.Loading -> {

                    }

                    is StateView.Success -> {
                        stateView.data?.let { transfer ->
                            configTransfer(transfer)
                            val userId = if (transfer.type == TransactionType.CASH_OUT) {
                                transfer.idUserRecipient
                            } else {
                                transfer.idUserSender
                            }
                            configTransfer(transfer)
                            getProfile(userId)
                        }
                    }

                    else -> {
                        showBottomSheet(message = getString(FirebaseHelper.validError(stateView.message.toString())))
                    }
                }
            }
    }

    private fun getProfile(id: String) {
        transferReceiptViewModel.getUserProfile(id).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {

                }

                is StateView.Success -> {
                    stateView.data?.let { configProfile(it) }
                }

                else -> {
                    showBottomSheet(message = getString(FirebaseHelper.validError(stateView.message.toString())))
                }
            }
        }
    }

    private fun configProfile(user: User) {
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


        binding.txtUserTransfer.text = user.name

    }

    private fun configTransfer(transfer: Transfer) {
        binding.txtShowTransactionCode.text = transfer.id
        binding.txtShowTransferDate.text =
            GetMask.getFormattedDate(transfer.date, GetMask.DAY_MONTH_YEAR_HOUR_MINUTE)
        binding.txtShowTransferValue.text =
            getString(
                R.string.text_account_balance_format,
                GetMask.getFormattedValue(transfer.value)
            )

        if (transfer.type == TransactionType.CASH_IN ) {
            binding.txtTransferTo.setText(R.string.transfer_made_by)
            binding.debitSuccess.isVisible  = false
        } else {
            binding.txtTransferTo.setText(R.string.received_transfer)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        Picasso.get().cancelTag(tagPicasso)
        _binding = null
    }
}