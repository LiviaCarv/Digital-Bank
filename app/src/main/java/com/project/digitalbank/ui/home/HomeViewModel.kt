package com.project.digitalbank.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.project.digitalbank.domain.transaction.GetTransactionsUseCase
import com.project.digitalbank.domain.wallet.GetWalletUseCase
import com.project.digitalbank.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getWalletUseCase: GetWalletUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase
) : ViewModel() {

    fun getWallet() = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val wallet = getWalletUseCase.invoke()

            emit(StateView.Success(wallet))
        } catch (exception: Exception) {
            emit(StateView.Error(exception.message))
        }
    }

    fun getTransactions() = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val transactions = getTransactionsUseCase.invoke()

            emit(StateView.Success(transactions))
        } catch (exception: Exception) {
            emit(StateView.Error(exception.message))
        }
    }


}