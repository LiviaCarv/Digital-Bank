package com.project.digitalbank.ui.features.transfer.receipt

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.project.digitalbank.R
import com.project.digitalbank.data.model.User
import com.project.digitalbank.databinding.FragmentTransferFormBinding
import com.project.digitalbank.databinding.FragmentTransferReceiptBinding
import com.project.digitalbank.util.GetMask
import com.project.digitalbank.util.initToolBar
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class TransferReceiptFragment : Fragment() {

    private var _binding: FragmentTransferReceiptBinding? = null
    private val binding get() = _binding!!
    private val args: TransferReceiptFragmentArgs by navArgs()
    private val transferReceiptViewModel: TransferReceiptViewModel by viewModels()
    private val tagPicasso = "tagPicasso"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTransferReceiptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar(toolbar = binding.toolbar)
        initListener()
//        configData()
    }

    private fun initListener() {
        binding.btnConfirm.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun configData(user: User) {
        binding.txtUserTransfer.text = user.name
//        binding.txtShowTransferValue.text =
//            getString(R.string.text_account_balance_format, GetMask.getFormattedValue(args.amount))
        if (user.imageProfile.isNotEmpty()) {
            Picasso
                .get()
                .load(user.imageProfile)
                .tag(tagPicasso)
                .fit().centerCrop()
                .into(binding.imgDestIcon, object : Callback {
                    override fun onSuccess() {
                        binding.imgDestIcon.isVisible = true
                        binding.imgProgressBar.isVisible = false
                    }

                    override fun onError(e: Exception?) {
                        TODO("Not yet implemented")
                    }
                })
        } else {
            binding.imgDestIcon.setImageResource(R.drawable.ic_user_place_holder)
            binding.imgDestIcon.isVisible = true
            binding.imgProgressBar.isVisible = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Picasso.get().cancelTag(tagPicasso)
        _binding = null
    }
}