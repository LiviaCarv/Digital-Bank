package com.project.digitalbank.domain.user_profile

import com.project.digitalbank.data.model.User
import com.project.digitalbank.data.repository.user_profile.UserProfileDataSourceImpl
import javax.inject.Inject

class GetProfilesListUseCase @Inject constructor(
    private val userProfileDataSourceImpl: UserProfileDataSourceImpl
) {
    suspend operator fun invoke() : List<User> {
        return userProfileDataSourceImpl.getProfilesList()
    }
}