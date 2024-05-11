package com.project.digitalbank.data.repository.user_profile

import com.project.digitalbank.data.model.User

interface UserProfileDataSource {

    suspend fun saveProfile(user: User)

    suspend fun getUserProfile() : User

    suspend fun getProfilesList() : List<User>

    suspend fun saveImage(imageProfile: String) : String
}