package com.project.digitalbank.ui.features.deposit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.project.digitalbank.NavigationGraphDirections
import com.project.digitalbank.R
import com.project.digitalbank.data.enum.TransactionOperation
import com.project.digitalbank.data.enum.TransactionType
import com.project.digitalbank.data.model.Deposit
import com.project.digitalbank.data.model.Transaction
import com.project.digitalbank.databinding.FragmentDepositFormBinding
import com.project.digitalbank.util.FirebaseHelper
import com.project.digitalbank.util.MoneyTextWatcher
import com.project.digitalbank.util.StateView
import com.project.digitalbank.util.hideKeyboard
import com.project.digitalbank.util.initToolBar
import com.project.digitalbank.util.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DepositFormFragment : Fragment() {

    private var _binding: FragmentDepositFormBinding? = null
    private val binding get() = _binding!!
    private val depositViewModel: DepositViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDepositFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar(toolbar = binding.toolbar, light = true)
        initListener()
    }

    private fun initListener() {
        with(binding.edtTxtDepositValue) {
            addTextChangedListener(MoneyTextWatcher(this))

            addTextChangedListener {
                if (MoneyTextWatcher.getValueUnMasked(this) > 10_000f) {
                    this.setText(context.getString(R.string.r_0_00))
                }
            }

            doAfterTextChanged {
                text?.length?.let {
                    this.setSelection(it)
                }
            }

        }
        binding.btnConfirm.setOnClickListener { validateDeposit() }
    }

    private fun validateDeposit() {
        val value = MoneyTextWatcher.getValueUnMasked(binding.edtTxtDepositValue)

        if (value > 0f) {
            hideKeyboard()
            val deposit = Deposit(value=value)
            saveDeposit(deposit)
        } else {
            showBottomSheet(message = "Insert a deposit value.")
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
                        val action = NavigationGraphDirections.actionGlobalDepositReceiptFragment(deposit.id)
                        findNavController().navigate(action)
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