package com.project.digitalbank.ui.features.transfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.project.digitalbank.R
import com.project.digitalbank.data.enum.TransactionOperation
import com.project.digitalbank.data.enum.TransactionType
import com.project.digitalbank.data.model.Deposit
import com.project.digitalbank.data.model.Transaction
import com.project.digitalbank.databinding.FragmentTransferFormBinding
import com.project.digitalbank.ui.features.deposit.DepositViewModel
import com.project.digitalbank.util.FirebaseHelper
import com.project.digitalbank.util.MoneyTextWatcher
import com.project.digitalbank.util.StateView
import com.project.digitalbank.util.hideKeyboard
import com.project.digitalbank.util.initToolBar
import com.project.digitalbank.util.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransferFormFragment : Fragment() {

    private var _binding: FragmentTransferFormBinding? = null
    private val binding get() = _binding!!
    private val depositViewModel: DepositViewModel by viewModels()
    private val args: TransferFormFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTransferFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar(toolbar = binding.toolbar, light = true)
        initListener()
    }

    private fun initListener() {
        with(binding.edtTxtTransferValue) {
            addTextChangedListener(MoneyTextWatcher(this))

            addTextChangedListener {
                if (MoneyTextWatcher.getValueUnMasked(this) > 10_000F) {
                    this.setText(context.getString(R.string.r_0_00))
                }
            }

            doAfterTextChanged {
                text?.length?.let {
                    this.setSelection(it)
                }
            }

        }
        binding.btnConfirm.setOnClickListener { validateAmount() }
    }

    private fun validateAmount() {
        val value = MoneyTextWatcher.getValueUnMasked(binding.edtTxtTransferValue)

        if (value > 0f) {
            hideKeyboard()
            val action = TransferFormFragmentDirections.actionTransferFormFragmentToConfirmTransferFragment(
                args.user,
                value
            )
            findNavController().navigate(action)

        } else {
            showBottomSheet(message = "Insert a transfer value.")
        }
    }

    private fun saveDeposit(deposit: Deposit) {
        depositViewModel.saveDeposit(deposit).observe(viewLifecycleOwner) { stateView ->
            when(stateView) {
                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is StateView.Success -> {
                    stateView.data?.let {
                        stateView.data.let { saveTransaction(stateView.data)}
                        StateView.Success(deposit.id)
                    }
                }
                else -> {
                    showBottomSheet(message = getString(FirebaseHelper.validError(stateView.message.toString())))

                }
            }
        }
    }

    private fun saveTransaction(deposit: Deposit) {

        val transaction = Transaction(
            id= deposit.id,
            date=deposit.date,
            value = deposit.value,
            type = TransactionType.CASH_IN,
            operation = TransactionOperation.DEPOSIT)

        depositViewModel.saveTransaction(transaction).observe(viewLifecycleOwner) { stateView ->
            when(stateView) {
                is StateView.Loading -> {}
                is StateView.Success -> {
                    stateView.data?.let {
                        StateView.Success(transaction)
                        binding.progressBar.isVisible = false
//                        val action = FragmentTransferFormBinding.actionDepositFormFragmentToDepositReceiptFragment(deposit.id)
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