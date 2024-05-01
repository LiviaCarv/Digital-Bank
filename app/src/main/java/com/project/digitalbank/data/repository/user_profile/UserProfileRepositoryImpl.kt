package com.project.digitalbank.data.repository.user_profile

import com.google.firebase.database.FirebaseDatabase
import com.project.digitalbank.data.model.User
import com.project.digitalbank.util.FirebaseHelper
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class UserProfileRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : UserProfileRepository{


    private val profilePreferences = firebaseDatabase.reference
        .child("profile")
        .child(FirebaseHelper.getUserId())

    override suspend fun saveProfile(user: User) {
        return suspendCoroutine { continuation ->
           profilePreferences
                .setValue(user)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resumeWith(Result.success(Unit))
                    } else {
                        task.exception?.let {
                            continuation.resumeWith(Result.failure(it))
                        }
                    }
                }
        }
    }

}