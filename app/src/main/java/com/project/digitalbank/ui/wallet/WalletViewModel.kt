package com.project.digitalbank.ui.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.project.digitalbank.data.model.Wallet
import com.project.digitalbank.domain.wallet.InitWalletUseCase
import com.project.digitalbank.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val initWalletUseCase: InitWalletUseCase
) : ViewModel() {

    fun initWallet (wallet: Wallet) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            initWalletUseCase.invoke(wallet)

            emit(StateView.Success(null,null))
        } catch (exception: Exception) {
            emit(StateView.Error(exception.message))
        }
    }
}