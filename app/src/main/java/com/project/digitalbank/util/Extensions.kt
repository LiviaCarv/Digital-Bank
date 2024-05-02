package com.project.digitalbank.util

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.project.digitalbank.R
import com.project.digitalbank.databinding.LayoutBottomSheetBinding

fun Fragment.initToolBar(toolbar: Toolbar, homeAsUpEnabled: Boolean = true, light: Boolean = false) {
    ((activity) as AppCompatActivity).apply {
        setSupportActionBar(toolbar)
        title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(homeAsUpEnabled)
        supportActionBar?.setHomeAsUpIndicator(if (light) R.drawable.ic_arrow_back_white else R.drawable.ic_arrow_back)
    }
    toolbar.setNavigationOnClickListener { activity?.onBackPressedDispatcher?.onBackPressed() }
}

fun Fragment.showBottomSheet(
    titleDialog: Int? = null,
    titleButton: Int? = null,
    message: String,
    onClick: () -> Unit = {}
) {
    val bottomSheetDialog = BottomSheetDialog(requireContext())
    val binding: LayoutBottomSheetBinding =
        LayoutBottomSheetBinding.inflate(layoutInflater, null, false)
    binding.txtTitle.text = getText(titleDialog ?: R.string.txt_bottom_sheet_warning)
    binding.btnOk.text = getText(titleButton ?: R.string.txt_bottom_sheet_ok)
    binding.txtMessage.text = message

    binding.btnOk.setOnClickListener {
        onClick()
        bottomSheetDialog.dismiss()
    }
    bottomSheetDialog.setContentView(binding.root)
    bottomSheetDialog.show()

}