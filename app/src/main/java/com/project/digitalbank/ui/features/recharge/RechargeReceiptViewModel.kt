package com.project.digitalbank.ui.features.recharge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.project.digitalbank.domain.recharge.GetRechargeFromDatabaseUseCase
import com.project.digitalbank.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class RechargeReceiptViewModel @Inject constructor(
    private val getRechargeFromDatabaseUseCase: GetRechargeFromDatabaseUseCase
) :ViewModel() {

    fun getRechargeFromDatabase(idRecharge: String) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())
            val recharge = getRechargeFromDatabaseUseCase.invoke(idRecharge)
            emit(StateView.Success(recharge))
        } catch (exception: Exception) {
            emit(StateView.Error(exception.message))
        }
    }
}