package com.project.digitalbank.data.repository.transfer

import com.project.digitalbank.data.model.Transfer

interface TransferDataSource {

    suspend fun saveTransfer(transfer: Transfer) : Transfer

    suspend fun updateTransfer(transfer: Transfer)

    suspend fun getTransfer(transferID: String) : Transfer

    suspend fun saveTransferTransaction(transfer: Transfer)

    suspend fun updateTransferTransaction(transfer: Transfer)

}