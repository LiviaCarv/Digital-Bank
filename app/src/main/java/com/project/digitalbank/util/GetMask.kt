package com.project.digitalbank.util

import java.text.*
import java.text.DateFormat.getDateTimeInstance
import java.util.*


class GetMask {
    companion object {
        const val DAY_MONTH = 0
        const val DAY_MONTH_YEAR = 1
        const val HOUR_MINUTE = 2
        const val DAY_MONTH_YEAR_HOUR_MINUTE = 3

        fun getFormattedDate(date: Long, type: Int): String {
            val locale = Locale("pt", "BR")

            val daySdf = SimpleDateFormat("dd", locale)
            daySdf.timeZone = TimeZone.getDefault()

            val monthSdf = SimpleDateFormat("MM", locale)
            monthSdf.timeZone = TimeZone.getDefault()

            val yearSdf = SimpleDateFormat("yyyy", locale)
            yearSdf.timeZone = TimeZone.getDefault()

            val hourSdf = SimpleDateFormat("HH", locale)
            hourSdf.timeZone = TimeZone.getDefault()

            val minuteSdf = SimpleDateFormat("mm", locale)
            minuteSdf.timeZone = TimeZone.getDefault()

            val dateFormat: DateFormat = getDateTimeInstance()
            val netDate = Date(date)
            dateFormat.format(netDate)

            val hour: String = hourSdf.format(netDate)
            val minute: String = minuteSdf.format(netDate)
            val day: String = daySdf.format(netDate)
            val month: String = monthSdf.format(netDate)
            val year: String = yearSdf.format(netDate)

            val time: String = when (type) {
                DAY_MONTH_YEAR -> "$day/$month/$year"
                HOUR_MINUTE -> "$hour:$minute"
                DAY_MONTH_YEAR_HOUR_MINUTE -> "$day/$month/$year $hour:$minute"
                Companion.DAY_MONTH -> "$day/$month"
                else -> "Erro"
            }
            return time
        }
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