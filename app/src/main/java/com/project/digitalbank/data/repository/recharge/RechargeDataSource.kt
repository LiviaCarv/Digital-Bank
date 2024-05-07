package com.project.digitalbank.data.repository.recharge

import com.project.digitalbank.data.model.Recharge

interface  RechargeDataSource {
    suspend fun saveRecharge(recharge: Recharge) : Recharge

    suspend fun getRechargeFromDatabase(idRecharge: String) : Recharge
}