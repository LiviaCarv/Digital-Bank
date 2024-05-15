package com.project.digitalbank.ui.features.transfer.receipt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.project.digitalbank.data.model.Transfer
import com.project.digitalbank.domain.transfer.GetTransferUseCase
import com.project.digitalbank.domain.transfer.SaveTransferUseCase
import com.project.digitalbank.domain.user_profile.GetUserProfileUseCase
import com.project.digitalbank.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class TransferReceiptViewModel @Inject constructor(
    private val getTransferUseCase: GetTransferUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel(){

    fun getTransfer(transferId: String) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val transfer = getTransferUseCase.invoke(transferId)

            emit(StateView.Success(transfer))
        } catch (exception: Exception) {

            emit(StateView.Error(exception.message))
        }
    }
    fun getUserProfile(id: String) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val profile = getUserProfileUseCase.invoke(id)

            emit(StateView.Success(profile))
        } catch (exception: Exception) {

            emit(StateView.Error(exception.message))
        }
    }

}