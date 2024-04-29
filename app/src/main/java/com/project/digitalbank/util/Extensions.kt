package com.project.digitalbank.util

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.project.digitalbank.R

fun Fragment.initToolBar(toolbar: Toolbar, homeAsUpEnabled: Boolean = true) {
    ((activity) as AppCompatActivity).apply {
        setSupportActionBar(toolbar)
        title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(homeAsUpEnabled)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
    }
    toolbar.setNavigationOnClickListener { activity?.onBackPressedDispatcher?.onBackPressed() }
}