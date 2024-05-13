package com.project.digitalbank.util


import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

class MoneyTextWatcher(private val editText: EditText) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        editText.removeTextChangedListener(this)
        val parsed: BigDecimal = parseToBigDecimal(s.toString())
        val formatted: String = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(parsed)
        editText.setText(formatted)
        editText.addTextChangedListener(this)
    }

    override fun afterTextChanged(editable: Editable) {

    }

    private fun parseToBigDecimal(value: String): BigDecimal {
        val replaceable = java.lang.String.format(
            "[%s,.\\s]",
            NumberFormat.getCurrencyInstance(Locale("pt", "BR")).currency?.symbol ?: 0f
        )
        val cleanString = value.replace(replaceable.toRegex(), "")

        if (cleanString.isEmpty()) {
            return BigDecimal.ZERO
        }

        return try {
            BigDecimal(cleanString).setScale(
                2, BigDecimal.ROUND_FLOOR
            ).divide(
                BigDecimal(100), BigDecimal.ROUND_FLOOR
            )
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            BigDecimal.ZERO
        }
    }


    companion object {
        fun getValueUnMasked(editText: EditText): Float {
            val moneyFormatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
            moneyFormatter.currency = Currency.getInstance("BRL")
            val value = if (editText.text.toString().isEmpty()) {
                0f
            } else {
                try {
                    moneyFormatter.parse(editText.text.toString())?.toFloat()
                } catch (e: Exception) {
                    e.printStackTrace()
                    0f
                }
            }
            return value ?: 0f
        }
    }

}