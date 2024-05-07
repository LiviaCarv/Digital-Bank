package com.project.digitalbank.ui.features.statement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.project.digitalbank.data.enum.TransactionOperation
import com.project.digitalbank.databinding.FragmentStatementBinding
import com.project.digitalbank.ui.home.HomeFragmentDirections
import com.project.digitalbank.ui.home.TransactionsAdapter
import com.project.digitalbank.util.FirebaseHelper
import com.project.digitalbank.util.StateView
import com.project.digitalbank.util.initToolBar
import com.project.digitalbank.util.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatementFragment : Fragment() {
    private var _binding: FragmentStatementBinding? = null
    private val binding: FragmentStatementBinding get() = _binding!!
    private lateinit var transactionsAdapter: TransactionsAdapter
    private val statementViewModel: StatementViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatementBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar(binding.toolbar)
        configRecyclerView()
        getTransactions()
    }

    private fun configRecyclerView() {
        transactionsAdapter = TransactionsAdapter(requireContext()) {
            when (it.operation) {
                TransactionOperation.DEPOSIT -> {
                    val action = StatementFragmentDirections.actionStatementFragmentToDepositReceiptFragment(it.id)
                    findNavController().navigate(action)
                }
                TransactionOperation.RECHARGE -> {
                    val action = StatementFragmentDirections.actionStatementFragmentToRechargeReceiptFragment(it.id)
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
        statementViewModel.getTransactions().observe(viewLifecycleOwner) { stateView ->
            when(stateView) {
                is StateView.Loading -> { }
                is StateView.Success -> {
                    binding.progressBar.isVisible = false
                    transactionsAdapter.submitList(stateView.data?.reversed())
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