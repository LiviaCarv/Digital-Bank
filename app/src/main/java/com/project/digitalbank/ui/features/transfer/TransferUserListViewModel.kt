package com.project.digitalbank.ui.features.transfer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.project.digitalbank.domain.user_profile.GetProfilesListUseCase
import com.project.digitalbank.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class TransferUserListViewModel @Inject constructor(
    private val getProfilesListUseCase: GetProfilesListUseCase
) : ViewModel() {

    fun getProfilesList() = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val usersList = getProfilesListUseCase.invoke()

            emit(StateView.Success(usersList))


        } catch (exception: Exception) {
            emit(StateView.Error(exception.message))
        }
    }
}