package com.project.digitalbank.data.repository.transaction

import com.project.digitalbank.data.model.Transaction

interface TransactionDataSource {
    suspend fun saveTransaction(transaction: Transaction)

    suspend fun getTransactions() : List<Transaction>

    suspend fun getBalance() : Float
}