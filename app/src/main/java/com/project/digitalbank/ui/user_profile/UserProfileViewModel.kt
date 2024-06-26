package com.project.digitalbank.ui.user_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.project.digitalbank.data.model.User
import com.project.digitalbank.domain.user_profile.GetUserProfileUseCase
import com.project.digitalbank.domain.user_profile.SaveImageProfileUseCase
import com.project.digitalbank.domain.user_profile.SaveProfileUseCase
import com.project.digitalbank.util.FirebaseHelper
import com.project.digitalbank.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val saveProfileUseCase: SaveProfileUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val saveImageProfileUseCase: SaveImageProfileUseCase
) : ViewModel() {
    fun saveProfile (user: User) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            saveProfileUseCase.invoke(user)

            emit(StateView.Success(null,null))
        } catch (exception: Exception) {
            emit(StateView.Error(exception.message))
        }
    }

    fun getUserProfile () = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())
            val user = getUserProfileUseCase.invoke(FirebaseHelper.getUserId())
            emit(StateView.Success(user))
        } catch (exception: Exception) {
            emit(StateView.Error(exception.message))
        }
    }

    fun saveImageProfile (imageProfile: String) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())
            val imageUrl = saveImageProfileUseCase.invoke(imageProfile)
            emit(StateView.Success(imageUrl))
        } catch (exception: Exception) {
            emit(StateView.Error(exception.message))
        }
    }
}