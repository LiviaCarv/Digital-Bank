package com.project.digitalbank.ui.features.transfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.project.digitalbank.R
import com.project.digitalbank.data.model.User
import com.project.digitalbank.databinding.FragmentConfirmTransferBinding
import com.project.digitalbank.databinding.FragmentDepositReceiptBinding
import com.project.digitalbank.util.GetMask
import com.project.digitalbank.util.initToolBar
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class ConfirmTransferFragment : Fragment() {


    private var _binding: FragmentConfirmTransferBinding? = null
    private val binding get() = _binding!!
    private val args: ConfirmTransferFragmentArgs by navArgs()
    private val tagPicasso = "tagPicasso"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConfirmTransferBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar(binding.toolbar, homeAsUpEnabled = true)
        configData(args.user)

    }

    private fun configData(user: User) {
        binding.imgDestIcon.isVisible = true
        binding.txtUserTransfer.text = user.name
        binding.txtShowTransferValue.text = getString(R.string.text_account_balance_format, GetMask.getFormattedValue(args.amount))
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Picasso.get().cancelTag(tagPicasso)

        _binding = null
    }

}