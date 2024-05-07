package com.project.digitalbank.domain.recharge

import com.project.digitalbank.data.model.Recharge
import com.project.digitalbank.data.repository.recharge.RechargeDataSourceImpl
import javax.inject.Inject

class GetRechargeFromDatabaseUseCase @Inject constructor(
    private val rechargeDataSourceImpl: RechargeDataSourceImpl
) {
    suspend operator fun invoke(idRecharge: String) : Recharge {
        return rechargeDataSourceImpl.getRechargeFromDatabase(idRecharge)
    }
}