package com.project.digitalbank.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.project.digitalbank.R
import com.project.digitalbank.data.enum.TransactionOperation
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
        binding.cardStatement.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_statementFragment)
        }
        binding.btnShowAllActivities.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_statementFragment)
        }
        binding.cardProfile.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_userProfileFragment)
        }
        logoutListener()
    }

    private fun logoutListener() {
        binding.btnLogout.setOnClickListener {
            showBottomSheet(
                message = getString(R.string.text_message_logout),
                titleDialog = R.string.title_dialog_confirm_logout,
                titleButton = R.string.txt_bottom_sheet_ok,
                onClick = {
                    FirebaseHelper.getAuth().signOut()
                    findNavController().navigate(R.id.action_homeFragment_to_authentication)
                }
            )
        }
    }

    private fun configRecyclerView() {
        transactionsAdapter = TransactionsAdapter(requireContext()) {
           when (it.operation) {
               TransactionOperation.DEPOSIT -> {
                   val action = HomeFragmentDirections.actionHomeFragmentToDepositReceiptFragment(it.id)
                   findNavController().navigate(action)
               }
               else -> {}
           }
        }
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
                    transactionsAdapter.submitList(stateView.data?.reversed()?.take(5))
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