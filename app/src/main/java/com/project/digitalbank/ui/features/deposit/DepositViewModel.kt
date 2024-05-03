package com.project.digitalbank.ui.features.deposit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.project.digitalbank.data.model.Deposit
import com.project.digitalbank.domain.deposit.SaveDepositUseCase
import com.project.digitalbank.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class DepositViewModel @Inject constructor(
    private val saveDepositUseCase: SaveDepositUseCase
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
}