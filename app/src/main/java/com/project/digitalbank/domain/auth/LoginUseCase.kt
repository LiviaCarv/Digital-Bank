package com.project.digitalbank.domain.auth

import com.project.digitalbank.data.repository.auth.AuthFirebaseDataSourceImpl

class LoginUseCase(
    private val authFirebaseDataSourceImpl: AuthFirebaseDataSourceImpl
) {
    suspend operator fun invoke(email: String, password: String) {
        return authFirebaseDataSourceImpl.login(email, password)
    }
}