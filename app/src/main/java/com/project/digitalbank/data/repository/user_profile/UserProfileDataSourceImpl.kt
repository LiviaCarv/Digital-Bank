package com.project.digitalbank.data.repository.user_profile

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.project.digitalbank.data.model.User
import com.project.digitalbank.util.FirebaseHelper
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class UserProfileDataSourceImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : UserProfileDataSource{


    private val profileReference = firebaseDatabase.reference
        .child("profile")
        .child(FirebaseHelper.getUserId())

    override suspend fun saveProfile(user: User) {
        return suspendCoroutine { continuation ->
           profileReference
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

    override suspend fun getUserProfile(): User {
        return suspendCoroutine { continuation ->
            profileReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userProfile = snapshot.getValue(User::class.java)
                userProfile?.let {
                    continuation.resumeWith(Result.success(it))
                }
            }
            override fun onCancelled(error: DatabaseError) {
                continuation.resumeWith(Result.failure(error.toException()))

            }
        })

        }
    }

}