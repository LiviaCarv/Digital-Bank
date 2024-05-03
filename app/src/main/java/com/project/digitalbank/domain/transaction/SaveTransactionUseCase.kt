package com.project.digitalbank.domain.transaction

import com.project.digitalbank.data.model.Transaction
import com.project.digitalbank.data.repository.transaction.TransactionDataSourceImpl
import javax.inject.Inject

class SaveTransactionUseCase @Inject constructor(
    private val transactionDataSourceImpl: TransactionDataSourceImpl
) {
    suspend operator fun invoke(transaction: Transaction) {
        return transactionDataSourceImpl.saveTransaction(transaction)
    }
}