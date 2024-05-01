package com.project.digitalbank.data.repository.user_profile

import com.google.firebase.database.FirebaseDatabase
import com.project.digitalbank.data.model.User
import javax.inject.Inject

interface UserProfileRepository {

    suspend fun saveProfile(user: User)
}