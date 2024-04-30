package com.project.digitalbank.ui.auth.recover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.project.digitalbank.domain.auth.RecoverUseCase
import com.project.digitalbank.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class RecoverViewModel @Inject constructor(
    private val recoverUseCase: RecoverUseCase
): ViewModel() {
    fun login (email: String) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            recoverUseCase.invoke(email)

            emit(StateView.Success(null,null))
        } catch (exception: Exception) {
            emit(StateView.Error(exception.message))
        }
    }
}