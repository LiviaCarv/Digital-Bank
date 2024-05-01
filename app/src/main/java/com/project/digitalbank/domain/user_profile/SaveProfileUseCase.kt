package com.project.digitalbank.domain.user_profile

import com.project.digitalbank.data.model.User
import com.project.digitalbank.data.repository.user_profile.UserProfileDataSourceImpl
import javax.inject.Inject

class SaveProfileUseCase @Inject constructor(
    private val userProfileRepositoryImpl: UserProfileDataSourceImpl

) {
    suspend operator fun invoke(user: User) {
        return userProfileRepositoryImpl.saveProfile(user)
    }
}

