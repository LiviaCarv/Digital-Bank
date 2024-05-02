package com.project.digitalbank.ui.features.deposit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.project.digitalbank.R
import com.project.digitalbank.databinding.FragmentDepositFormBinding
import com.project.digitalbank.util.initToolBar

class DepositFormFragment : Fragment() {

    private var _binding: FragmentDepositFormBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDepositFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar(toolbar = binding.toolbar, light = true)
        initListener()
    }

    private fun initListener() {
        binding.btnConfirm.setOnClickListener {
            findNavController().navigate(R.id.action_depositFormFragment_to_depositReceiptFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}