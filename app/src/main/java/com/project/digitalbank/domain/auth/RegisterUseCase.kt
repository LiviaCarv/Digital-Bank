package com.project.digitalbank.domain.auth

import com.project.digitalbank.data.model.User
import com.project.digitalbank.data.repository.auth.AuthFirebaseDataSourceImpl
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authFirebaseDataSourceImpl: AuthFirebaseDataSourceImpl
) {
    suspend operator fun invoke(name: String, email: String, phone: String, password: String): User {
        return authFirebaseDataSourceImpl.register(name, email,phone, password)
    }
}