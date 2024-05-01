package com.project.digitalbank.data.repository.auth

import com.google.firebase.auth.FirebaseAuth
import com.project.digitalbank.data.model.User
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class AuthFirebaseDataSourceImpl  @Inject constructor(
    private val firebaseAuth: FirebaseAuth
)
    : AuthFirebaseDataSource {
    override suspend fun login(email: String, password: String) {
        return suspendCoroutine { continuation ->

            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resumeWith(Result.success(Unit))

                    } else {
                        // If sign in fails, display a message to the user.
                        task.exception?.let {
                            continuation.resumeWith(Result.failure(it))
                        }

                    }

                }
        }
    }

    override suspend fun register(name: String, email: String, phone: String, password: String) : User {
        return suspendCoroutine { continuation ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = task.result.user?.uid ?: ""
                        val user = User(userId,name,email,phone, password)
                        continuation.resumeWith(Result.success(user))
                    } else {
                        // If sign in fails, display a message to the user.
                        task.exception?.let {
                            continuation.resumeWith(Result.failure(it))
                        }

                    }

                }
        }
    }

    override suspend fun recover(email: String) {
        return suspendCoroutine { continuation ->
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resumeWith(Result.success(Unit))

                    } else {
                        // If sign in fails, display a message to the user.
                        task.exception?.let {
                            continuation.resumeWith(Result.failure(it))
                        }

                    }

                }
        }
    }
}