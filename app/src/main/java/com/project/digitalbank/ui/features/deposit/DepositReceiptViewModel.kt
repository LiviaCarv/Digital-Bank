package com.project.digitalbank.ui.features.deposit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.project.digitalbank.domain.deposit.GetDepositFromDatabaseUseCase
import com.project.digitalbank.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class DepositReceiptViewModel @Inject constructor(
    private val getDepositFromDatabaseUseCase: GetDepositFromDatabaseUseCase
) : ViewModel() {

    fun getDepositFromDatabase(idDeposit: String) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val deposit = getDepositFromDatabaseUseCase.invoke(idDeposit)

            emit(StateView.Success(deposit))
        } catch (exception: Exception) {
            emit(StateView.Error(exception.message))
        }
    }
}