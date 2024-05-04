package com.project.digitalbank.ui.features.deposit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.project.digitalbank.R
import com.project.digitalbank.data.model.Deposit
import com.project.digitalbank.databinding.FragmentDepositReceiptBinding
import com.project.digitalbank.util.FirebaseHelper
import com.project.digitalbank.util.GetMask
import com.project.digitalbank.util.StateView
import com.project.digitalbank.util.initToolBar
import com.project.digitalbank.util.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DepositReceiptFragment : Fragment() {
    private var _binding: FragmentDepositReceiptBinding? = null
    private val binding get() = _binding!!
    private val args : DepositReceiptFragmentArgs by navArgs()
    private val depositReceiptViewModel: DepositReceiptViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding = FragmentDepositReceiptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar(binding.toolbar, homeAsUpEnabled = false)
        getDepositFromDatabase(args.idDeposit)
        initListener()
    }

    private fun initListener() {
        binding.btnConfirm.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun getDepositFromDatabase(idDeposit: String) {
        depositReceiptViewModel.getDepositFromDatabase(idDeposit).observe(viewLifecycleOwner) { stateView ->
            when(stateView) {
                is StateView.Loading -> {
                }
                is StateView.Success -> {
                    stateView.data?.let {
                        StateView.Success(stateView.data)
                        configReceipt(it)
                    }
                }
                else -> {
                    showBottomSheet(message = getString(FirebaseHelper.validError(stateView.message.toString())))

                }
            }
        }

    }
    private fun configReceipt(deposit: Deposit) {
        binding.apply {
            txtShowTransactionCode.text = deposit.id
            txtShowDepositDate.text = GetMask.getFormattedDate(deposit.date, GetMask.DAY_MONTH_YEAR_HOUR_MINUTE)
            txtShowDepositValue.text = getString(R.string.text_account_balance_format, GetMask.getFormattedValue(deposit.value))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}