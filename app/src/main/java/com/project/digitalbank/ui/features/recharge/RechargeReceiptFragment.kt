package com.project.digitalbank.ui.features.recharge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.project.digitalbank.R
import com.project.digitalbank.data.model.Recharge
import com.project.digitalbank.databinding.FragmentRechargeReceiptBinding
import com.project.digitalbank.util.FirebaseHelper
import com.project.digitalbank.util.GetMask
import com.project.digitalbank.util.StateView
import com.project.digitalbank.util.initToolBar
import com.project.digitalbank.util.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RechargeReceiptFragment : Fragment() {
    private var _binding: FragmentRechargeReceiptBinding? = null
    private val binding get() = _binding!!
    private val rechargeReceiptViewModel: RechargeReceiptViewModel by viewModels()
    private val args : RechargeReceiptFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRechargeReceiptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar(binding.toolbar, homeAsUpEnabled = false)
        getRechargeFromDatabase()
        initListener()
    }

    private fun initListener() {
        binding.btnConfirm.setOnClickListener {
            val navOptions: NavOptions = NavOptions.Builder().setPopUpTo(R.id.rechargeFormFragment,true).build()
            findNavController().navigate(R.id.action_global_homeFragment, null, navOptions)
        }
    }

    private fun getRechargeFromDatabase() {
        rechargeReceiptViewModel.getRechargeFromDatabase(args.idRecharge).observe(viewLifecycleOwner) { stateView ->
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
    private fun configReceipt(recharge: Recharge) {
        binding.apply {
            txtShowTransactionCode.text = recharge.id
            txtShowRechargeDate.text = GetMask.getFormattedDate(recharge.date, GetMask.DAY_MONTH_YEAR_HOUR_MINUTE)
            txtShowRechargeValue.text = getString(R.string.text_account_balance_format, GetMask.getFormattedValue(recharge.amount))
            txtShowPhoneNumber.setText(recharge.phoneNumber)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}