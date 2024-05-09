package com.project.digitalbank.ui.features.transfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.project.digitalbank.databinding.FragmentTransferUserListBinding
import com.project.digitalbank.util.FirebaseHelper
import com.project.digitalbank.util.StateView
import com.project.digitalbank.util.initToolBar
import com.project.digitalbank.util.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransferUserListFragment : Fragment() {
    private var _binding: FragmentTransferUserListBinding? = null
    private val binding: FragmentTransferUserListBinding get() = _binding!!
    private val transferUserListViewModel: TransferUserListViewModel by viewModels()
    private lateinit var transferUserListAdapter: TransferAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTransferUserListBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar(binding.toolbar, light = true)
        configRecyclerView()
        getUsersList()
    }

    private fun configRecyclerView() {
        transferUserListAdapter = TransferAdapter(requireContext()) { userSelected ->
            Toast.makeText(requireContext(), userSelected.name, Toast.LENGTH_SHORT).show()
        }

        with(binding.recyclerViewUsersList) {
            setHasFixedSize(true)
            adapter = transferUserListAdapter
        }
    }

    private fun getUsersList() {
        transferUserListViewModel.getProfilesList().observe(viewLifecycleOwner) { stateView ->
            when(stateView) {
                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is StateView.Success -> {
                    binding.progressBar.isVisible = false
                    transferUserListAdapter.submitList(stateView.data)
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