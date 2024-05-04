package com.project.digitalbank.domain.transaction

import com.project.digitalbank.data.model.Transaction
import com.project.digitalbank.data.repository.transaction.TransactionDataSourceImpl
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val transactionDataSourceImpl: TransactionDataSourceImpl
) {

    suspend operator fun invoke(): List<Transaction> {
        return transactionDataSourceImpl.getTransactions()
    }
}