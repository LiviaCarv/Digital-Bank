package com.project.digitalbank.ui.features.recharge

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
import com.project.digitalbank.data.enum.TransactionOperation
import com.project.digitalbank.data.enum.TransactionType
import com.project.digitalbank.data.model.Deposit
import com.project.digitalbank.data.model.Recharge
import com.project.digitalbank.data.model.Transaction
import com.project.digitalbank.databinding.FragmentRechargeFormBinding
import com.project.digitalbank.ui.features.deposit.DepositFormFragmentDirections
import com.project.digitalbank.util.FirebaseHelper
import com.project.digitalbank.util.StateView
import com.project.digitalbank.util.hideKeyboard
import com.project.digitalbank.util.initToolBar
import com.project.digitalbank.util.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RechargeFormFragment : Fragment() {
    private var _binding: FragmentRechargeFormBinding? = null
    private val binding: FragmentRechargeFormBinding get() = _binding!!
    private val rechargeViewModel: RechargeViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRechargeFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar(binding.toolbar, light = true)
        initListener()
    }

    private fun initListener() {
        binding.btnConfirm.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        binding.apply {
            val rechargeValue = edtTxtRechargeValue.text.toString().trim()
            val phoneNumber = edtTxtPhoneNumber.unMaskedText

            if (rechargeValue.isEmpty()) {
                showBottomSheet(message = getString(R.string.register_provide_recharge_value))
            }  else if (phoneNumber?.isEmpty() == true) {
                showBottomSheet(message = getString(R.string.register_provide_phone))
            } else if (phoneNumber?.length != 11) {
                showBottomSheet(message = getString(R.string.register_phone_number_invalid))
            } else {
                hideKeyboard()
                binding.progressBar.isVisible = true

                val recharge = Recharge(amount = rechargeValue.toFloat(), phoneNumber = phoneNumber)

                saveRecharge(recharge)
                Toast.makeText(requireContext(), "recharge saved", Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun saveRecharge(recharge: Recharge) {
        rechargeViewModel.saveRecharge(recharge).observe(viewLifecycleOwner) { stateView ->
            when(stateView) {
                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is StateView.Success -> {
                    stateView.data?.let {
                        stateView.data.let { saveTransaction(stateView.data)}
                        StateView.Success(recharge.id)
                    }
                }
                else -> {
                    showBottomSheet(message = getString(FirebaseHelper.validError(stateView.message.toString())))

                }
            }
        }
    }

    private fun saveTransaction(recharge: Recharge) {

        val transaction = Transaction(
            id= recharge.id,
            date=recharge.date,
            value = recharge.amount,
            type = TransactionType.CASH_OUT,
            operation = TransactionOperation.RECHARGE)

        rechargeViewModel.saveTransaction(transaction).observe(viewLifecycleOwner) { stateView ->
            when(stateView) {
                is StateView.Loading -> {}
                is StateView.Success -> {
                    stateView.data?.let {
                        StateView.Success(transaction)
                        Toast.makeText(requireContext(), "TRANSACTION SAVED", Toast.LENGTH_SHORT).show()
                        binding.progressBar.isVisible = false
//                        val action = RechargeFormFragmentDirections.actionRechargeFormFragmentToRechargeReceiptFragment(recharge.id)
//                        findNavController().navigate(action)
                    }
                }
                else -> {
                    showBottomSheet(message = getString(FirebaseHelper.validError(stateView.message.toString())))
                    binding.progressBar.isVisible = false

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}