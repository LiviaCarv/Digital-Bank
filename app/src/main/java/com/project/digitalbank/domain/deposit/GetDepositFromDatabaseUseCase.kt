package com.project.digitalbank.domain.deposit

import com.project.digitalbank.data.model.Deposit
import com.project.digitalbank.data.repository.deposit.DepositDataSourceImpl
import javax.inject.Inject

class GetDepositFromDatabaseUseCase @Inject constructor(
    private val depositDataSourceImpl: DepositDataSourceImpl
) {
    suspend operator fun invoke(idDeposit: String) : Deposit {
        return depositDataSourceImpl.getDepositFromDatabase(idDeposit)
    }
}