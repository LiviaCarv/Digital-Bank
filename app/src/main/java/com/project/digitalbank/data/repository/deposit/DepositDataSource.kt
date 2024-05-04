package com.project.digitalbank.data.repository.deposit

import com.project.digitalbank.data.model.Deposit

interface DepositDataSource {

    suspend fun saveDeposit(deposit: Deposit) : Deposit

    suspend fun getDepositFromDatabase(idDeposit: String) : Deposit
}