package com.project.digitalbank.data.repository.auth

import com.project.digitalbank.data.model.User

interface AuthFirebaseDataSource {
    suspend fun login(email: String, password: String)

    suspend fun register(user: User) : User

    suspend fun recover(email: String)

}