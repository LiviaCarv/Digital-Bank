package com.project.digitalbank.data.model

import com.project.digitalbank.data.enum.TransactionOperation
import com.project.digitalbank.data.enum.TransactionType

data class Transaction(
    var id: String = "",
    val operation: TransactionOperation? = null,
    val date: Long = 0L,
    val value: Float= 0f,
    val type: TransactionType? = null,
)