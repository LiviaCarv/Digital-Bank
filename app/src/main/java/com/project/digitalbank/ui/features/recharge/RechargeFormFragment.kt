package com.project.digitalbank.ui.features.recharge

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.project.digitalbank.R
import com.project.digitalbank.databinding.FragmentRechargeFormBinding
import com.project.digitalbank.util.hideKeyboard
import com.project.digitalbank.util.initToolBar
import com.project.digitalbank.util.showBottomSheet

class RechargeFormFragment : Fragment() {
    private var _binding: FragmentRechargeFormBinding? = null
    private val binding: FragmentRechargeFormBinding get() = _binding!!


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
                Toast.makeText(requireContext(), "beleza", Toast.LENGTH_SHORT).show()
                }
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}