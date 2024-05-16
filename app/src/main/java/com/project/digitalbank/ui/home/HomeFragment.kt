package com.project.digitalbank.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.project.digitalbank.NavigationGraphDirections
import com.project.digitalbank.R
import com.project.digitalbank.data.enum.TransactionOperation
import com.project.digitalbank.data.enum.TransactionType
import com.project.digitalbank.data.model.Transaction
import com.project.digitalbank.data.model.User
import com.project.digitalbank.databinding.FragmentHomeBinding
import com.project.digitalbank.util.FirebaseHelper
import com.project.digitalbank.util.GetMask
import com.project.digitalbank.util.StateView
import com.project.digitalbank.util.showBottomSheet
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var transactionsAdapter: TransactionsAdapter
    private val tagPicasso = "tagPicasso"

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
        getProfile()
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
        binding.cardMobileRecharge.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_rechargeFormFragment)
        }
        binding.btnShowAllActivities.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_statementFragment)
        }
        binding.cardProfile.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_userProfileFragment)
        }
        binding.cardTransaction.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_transferUserListFragment)
        }
        binding.imgUserIcon.setOnClickListener {
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
                    val navOptions: NavOptions = NavOptions.Builder().setPopUpTo(R.id.homeFragment,true).build()
                    findNavController().navigate(R.id.action_global_authentication, null, navOptions)
                }
            )
        }
    }

    private fun configRecyclerView() {
        transactionsAdapter = TransactionsAdapter(requireContext()) {
           when (it.operation) {
               TransactionOperation.DEPOSIT -> {
                   val action = NavigationGraphDirections.actionGlobalDepositReceiptFragment(it.id)
                   findNavController().navigate(action)
               }
               TransactionOperation.RECHARGE -> {
                   val action = NavigationGraphDirections.actionGlobalRechargeReceiptFragment(it.id)
                   findNavController().navigate(action)
               }
               TransactionOperation.TRANSFER -> {
                   val action = NavigationGraphDirections.actionGlobalTransferReceiptFragment(it.id)
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

    private fun getProfile() {
        homeViewModel.getUserProfile().observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                }

                is StateView.Success -> {
                    stateView.data?.let {
                        configData(it)
                    }
                }

                else -> {
                    showBottomSheet(message = getString(FirebaseHelper.validError(stateView.message.toString())))
                }
            }

        }
    }

    private fun configData(user: User) {
        binding.imgUserIcon.isVisible = true
       if (user.imageProfile.isNotEmpty()) {
           Picasso
               .get()
               .load(user.imageProfile)
               .tag(tagPicasso)
               .fit().centerCrop()
               .into(binding.imgUserIcon, object : Callback {
                   override fun onSuccess() {
                       binding.imgUserIcon.isVisible = true
                       binding.iconProgressBar.isVisible = false

                   }
                   override fun onError(e: Exception?) {
                       binding.iconProgressBar.isVisible = false
                       binding.imgUserIcon.setImageResource(R.drawable.ic_user_place_holder)
                   }
               })
       } else {
           binding.imgUserIcon.setImageResource(R.drawable.ic_user_place_holder)
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
                    transactionsAdapter.submitList(stateView.data?.reversed()?.take(10))
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
        Picasso.get().cancelTag(tagPicasso)
        _binding = null
    }
}