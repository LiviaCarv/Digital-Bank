package com.project.digitalbank.domain.auth

import com.project.digitalbank.data.repository.auth.AuthFirebaseDataSourceImpl

class RecoverUseCase(
    private val authFirebaseDataSourceImpl: AuthFirebaseDataSourceImpl
) {
    suspend operator fun invoke(email: String) {
        return authFirebaseDataSourceImpl.recover(email)
    }
}