package com.project.digitalbank.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.project.digitalbank.domain.transaction.GetTransactionsUseCase
import com.project.digitalbank.domain.user_profile.GetUserProfileUseCase
import com.project.digitalbank.util.FirebaseHelper
import com.project.digitalbank.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase
    ) : ViewModel() {



    fun getUserProfile () = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())
            val user = getUserProfileUseCase.invoke(FirebaseHelper.getUserId())
            emit(StateView.Success(user))
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