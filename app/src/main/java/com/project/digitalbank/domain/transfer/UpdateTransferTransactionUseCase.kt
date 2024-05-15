package com.project.digitalbank.domain.transfer
import com.project.digitalbank.data.model.Transfer
import com.project.digitalbank.data.repository.transfer.TransferDataSourceImpl
import javax.inject.Inject

class UpdateTransferTransactionUseCase @Inject constructor(
    private val transferDataSourceImpl: TransferDataSourceImpl
) {

    suspend operator fun invoke(transfer: Transfer) {
        transferDataSourceImpl.updateTransferTransaction(transfer)
    }
}