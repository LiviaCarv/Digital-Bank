package com.project.digitalbank.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.project.digitalbank.R
import com.project.digitalbank.data.enum.TransactionType
import com.project.digitalbank.data.model.Transaction
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
    private lateinit var transactionsAdapter: TransactionsAdapter

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
        configRecyclerView()
        initListener()
        getTransactions()

    }

    private fun initListener() {
        binding.cardDeposit.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_depositFormFragment)
        }
    }

    private fun configRecyclerView() {
        transactionsAdapter = TransactionsAdapter(requireContext()) {}
        with(binding.recyclerLastTransactions) {
            setHasFixedSize(true)
            adapter= transactionsAdapter
        }
    }


    private fun getTransactions() {
        homeViewModel.getTransactions().observe(viewLifecycleOwner) { stateView ->
            when(stateView) {
                is StateView.Loading -> {
                    binding.txtDisplayAccountBalance.text = "Loading..."
                    binding.progressBar.isVisible = true
                }
                is StateView.Success -> {
                    binding.progressBar.isVisible = false
                    transactionsAdapter.submitList(stateView.data?.reversed())
                    configBalance(stateView.data ?: emptyList())
                }
                else -> {
                    showBottomSheet(message = getString(FirebaseHelper.validError(stateView.message.toString())))
                    binding.progressBar.isVisible = false
                }
            }


        }
    }

    private fun configBalance(transactions: List<Transaction>) {
        var balance = 0f
        for (tr in transactions) {
            if (tr.type == TransactionType.CASH_IN) {
                balance += tr.value
            } else {
                balance -= tr.value
            }
        }
        binding.txtDisplayAccountBalance.text = getString(R.string.text_account_balance_format, GetMask.getFormattedValue(balance))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}