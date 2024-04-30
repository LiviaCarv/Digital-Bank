package com.project.digitalbank.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.project.digitalbank.data.model.User
import com.project.digitalbank.domain.auth.RegisterUseCase
import com.project.digitalbank.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    fun register(user: User) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            registerUseCase.invoke(user)

            emit(StateView.Success(user, "User registered"))
        } catch (exception: Exception) {
            emit(StateView.Error(exception.message))
        }
    }
}