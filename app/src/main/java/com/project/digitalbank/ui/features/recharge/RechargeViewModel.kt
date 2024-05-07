package com.project.digitalbank.ui.features.recharge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.project.digitalbank.data.model.Recharge
import com.project.digitalbank.data.model.Transaction
import com.project.digitalbank.domain.recharge.SaveRechargeUseCase
import com.project.digitalbank.domain.transaction.SaveTransactionUseCase
import com.project.digitalbank.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class RechargeViewModel @Inject constructor(
    private val saveRechargeUseCase: SaveRechargeUseCase,
    private val saveTransactionUseCase: SaveTransactionUseCase
) : ViewModel() {

    fun saveRecharge(recharge: Recharge) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())
            val rechargeWithDate = saveRechargeUseCase.invoke(recharge)
            emit(StateView.Success(rechargeWithDate))
        } catch (error: Exception) {
            emit(StateView.Error(error.message))
        }
    }

    fun saveTransaction(transaction: Transaction) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            saveTransactionUseCase.invoke(transaction)

            emit(StateView.Success(Unit))
        } catch (exception: Exception) {
            emit(StateView.Error(exception.message))
        }
    }

}