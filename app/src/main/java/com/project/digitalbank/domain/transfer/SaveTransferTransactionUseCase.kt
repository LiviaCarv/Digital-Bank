package com.project.digitalbank.domain.transfer

import com.project.digitalbank.data.model.Transaction
import com.project.digitalbank.data.model.Transfer
import com.project.digitalbank.data.repository.transaction.TransactionDataSourceImpl
import com.project.digitalbank.data.repository.transfer.TransferDataSource
import com.project.digitalbank.data.repository.transfer.TransferDataSourceImpl
import javax.inject.Inject

class SaveTransferTransactionUseCase @Inject constructor(
    private val transferDataSourceImpl: TransferDataSourceImpl
) {
    suspend operator fun invoke(transfer: Transfer) {
        return transferDataSourceImpl.saveTransferTransaction(transfer)
    }
}