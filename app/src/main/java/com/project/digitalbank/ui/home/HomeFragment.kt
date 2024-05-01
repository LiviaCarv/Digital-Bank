package com.project.digitalbank.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.project.digitalbank.R
import com.project.digitalbank.data.model.Wallet
import com.project.digitalbank.databinding.FragmentHomeBinding
import com.project.digitalbank.util.FirebaseHelper
import com.project.digitalbank.util.GetMask
import com.project.digitalbank.util.StateView
import com.project.digitalbank.util.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getWallet()

    }

    private fun getWallet() {
        homeViewModel.getWallet().observe(viewLifecycleOwner) { stateView ->
            when(stateView) {
                is StateView.Loading -> {
                    binding.txtDisplayAccountBalance.text = "Loading..."
                }
                is StateView.Success -> {
                    stateView.data?.let {
                        Log.i("INFOTESTE", "CONFIG BALANCE CHAMADO")
                        configBalance(it)
                    }
                }
                else -> {
                    showBottomSheet(message = getString(FirebaseHelper.validError(stateView.message.toString())))
                }
            }


        }
    }

    private fun configBalance(wallet: Wallet) {
        binding.txtDisplayAccountBalance.text = getString(R.string.text_account_balance_format, GetMask.getFormattedValue(wallet.balance))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}