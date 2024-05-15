package com.project.digitalbank.domain.transfer

import com.project.digitalbank.data.model.Transfer
import com.project.digitalbank.data.repository.transfer.TransferDataSourceImpl
import javax.inject.Inject

class GetTransferUseCase @Inject constructor(
    private val transferDataSourceImpl: TransferDataSourceImpl
) {

    suspend operator fun invoke(transferId: String) : Transfer {
        return transferDataSourceImpl.getTransfer(transferId)
    }
}