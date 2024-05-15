package com.project.digitalbank.ui.features.transfer.confirm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.project.digitalbank.data.model.Transfer
import com.project.digitalbank.domain.transaction.GetBalanceUseCase
import com.project.digitalbank.domain.transfer.SaveTransferUseCase
import com.project.digitalbank.domain.transfer.UpdateTransferUseCase
import com.project.digitalbank.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ConfirmTransferViewModel @Inject constructor(
    private val getBalanceUseCase: GetBalanceUseCase,
    private val saveTransferUseCase: SaveTransferUseCase,
    private val updateTransferUseCase: UpdateTransferUseCase
) : ViewModel() {

    fun getBalance() = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val balance = getBalanceUseCase.invoke()

            emit(StateView.Success(balance))
        } catch (exception: Exception) {

            emit(StateView.Error(exception.message))
        }
    }

    fun saveTransfer(transfer: Transfer) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val transferSaved = saveTransferUseCase.invoke(transfer)

            emit(StateView.Success(transferSaved))
        } catch (exception: Exception) {
            emit(StateView.Error(exception.message))
        }
    }

    fun updateTransfer(transfer: Transfer) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            updateTransferUseCase.invoke(transfer)

            emit(StateView.Success(Unit))
        } catch (exception: Exception) {
            emit(StateView.Error(exception.message))
        }
    }
}

