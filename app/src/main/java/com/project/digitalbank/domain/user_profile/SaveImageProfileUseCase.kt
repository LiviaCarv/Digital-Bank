package com.project.digitalbank.domain.user_profile

import com.project.digitalbank.data.repository.user_profile.UserProfileDataSourceImpl
import javax.inject.Inject

class SaveImageProfileUseCase @Inject constructor(
    private val userProfileDataSourceImpl: UserProfileDataSourceImpl
) {
    suspend operator fun invoke(imageProfile: String) : String {
        return  userProfileDataSourceImpl.saveImage(imageProfile)
    }
 }