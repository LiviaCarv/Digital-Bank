package com.project.digitalbank.ui.features.deposit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.project.digitalbank.data.enum.TransactionOperation
import com.project.digitalbank.data.enum.TransactionType
import com.project.digitalbank.data.model.Deposit
import com.project.digitalbank.data.model.Transaction
import com.project.digitalbank.domain.deposit.SaveDepositUseCase
import com.project.digitalbank.domain.transaction.SaveTransactionUseCase
import com.project.digitalbank.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class DepositViewModel @Inject constructor(
    private val saveDepositUseCase: SaveDepositUseCase,
    private val saveTransactionUseCase: SaveTransactionUseCase
) : ViewModel() {

    fun saveDeposit(deposit: Deposit) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val depositWithDate = saveDepositUseCase.invoke(deposit)

            emit(StateView.Success(depositWithDate))
        } catch (exception: Exception) {
            emit(StateView.Error(exception.message))
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