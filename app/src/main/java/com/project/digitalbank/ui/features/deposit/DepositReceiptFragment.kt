package com.project.digitalbank.ui.features.deposit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.project.digitalbank.R
import com.project.digitalbank.data.model.Deposit
import com.project.digitalbank.databinding.FragmentDepositFormBinding
import com.project.digitalbank.databinding.FragmentDepositReceiptBinding
import com.project.digitalbank.util.GetMask

class DepositReceiptFragment : Fragment() {
    private var _binding: FragmentDepositReceiptBinding? = null
    private val binding get() = _binding!!
    private val args : DepositReceiptFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding = FragmentDepositReceiptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configReceipt(args.deposit)
        initListener()
    }

    private fun initListener() {
        binding.btnConfirm.setOnClickListener {
            findNavController().navigate(R.id.action_depositReceiptFragment_to_homeFragment)
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