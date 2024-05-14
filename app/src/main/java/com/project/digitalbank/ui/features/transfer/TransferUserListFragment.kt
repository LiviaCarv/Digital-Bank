package com.project.digitalbank.ui.features.transfer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ferfalk.simplesearchview.SimpleSearchView
import com.project.digitalbank.R
import com.project.digitalbank.data.model.User
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
    private var profilesList: List<User> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }
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
        configSearchView()
    }

    private fun configSearchView() {

        binding.searchView.setOnQueryTextListener(object : SimpleSearchView.OnQueryTextListener {
            // obter texto em tempo real enquanto usuario digita
            override fun onQueryTextChange(newText: String): Boolean {
                return if (newText.isNotEmpty()) {
                    val newList = profilesList.filter { it.name.contains(newText , true) }
                    transferUserListAdapter.submitList(newList)
                    true
                } else {
                    transferUserListAdapter.submitList(profilesList)
                    false
                }
            }

            // obter texto digitado apos clicar no botao de pesquisa
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            // user limpa a caixa de pesquisa
            override fun onQueryTextCleared(): Boolean {
                return false
            }
        })

        binding.searchView.setOnSearchViewListener(object : SimpleSearchView.SearchViewListener {
            // ao abrir a caixa de pesquisa
            override fun onSearchViewShown() {
            }

            // ao fechar a caixa de pesquisa

            override fun onSearchViewClosed() {
                transferUserListAdapter.submitList(profilesList)
            }

            // animacao de abertura
            override fun onSearchViewShownAnimation() {
            }

            // animacao de fechamento
            override fun onSearchViewClosedAnimation() {
            }
        })
    }

    private fun configRecyclerView() {
        transferUserListAdapter = TransferAdapter(requireContext()) { userSelected ->
            val action = TransferUserListFragmentDirections.actionTransferUserListFragmentToTransferFormFragment(userSelected)
            findNavController().navigate(action)
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
                    profilesList = stateView.data ?: emptyList()
                    transferUserListAdapter.submitList(profilesList)
                }
                else -> {
                    showBottomSheet(message = getString(FirebaseHelper.validError(stateView.message.toString())))
                    binding.progressBar.isVisible = false
                }
            }


        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val item = menu.findItem(R.id.action_search)
        binding.searchView.setMenuItem(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}