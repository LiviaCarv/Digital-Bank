package com.project.digitalbank.data.repository.transfer

import com.project.digitalbank.data.model.Transfer

interface TransferDataSource {

    suspend fun saveTransfer(transfer: Transfer) : Transfer

    suspend fun updateTransfer(transfer: Transfer)
}