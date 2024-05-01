package com.project.digitalbank.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale

class GetMask {
    companion object {
        fun getFormattedValue(value: Float): String? {
            val nf: NumberFormat = DecimalFormat(
                "#,##0.00", DecimalFormatSymbols(
                    Locale("pt", "BR")
                )
            )
            return nf.format(value)
        }
    }
}