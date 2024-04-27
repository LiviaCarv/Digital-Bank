package com.project.digitalbank.data.repository.auth

interface AuthFirebaseDataSource {
    suspend fun login(email: String, password: String)

    suspend fun register(name: String, email: String, phone: String, password: String)

    suspend fun recover(email: String)

}