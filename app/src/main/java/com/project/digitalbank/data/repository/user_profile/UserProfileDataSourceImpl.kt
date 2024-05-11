package com.project.digitalbank.data.repository.user_profile

import android.net.Uri
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.project.digitalbank.data.model.User
import com.project.digitalbank.util.FirebaseHelper
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class UserProfileDataSourceImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val storage: FirebaseStorage
) : UserProfileDataSource {


    private val profileDataBaseReference = firebaseDatabase.reference
        .child("profile")
        .child(FirebaseHelper.getUserId())

    private val profileStorageReference = storage.reference
        .child("images")
        .child(FirebaseHelper.getUserId())

    override suspend fun saveProfile(user: User) {
        return suspendCoroutine { continuation ->
            profileDataBaseReference
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
            profileDataBaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
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

    override suspend fun getProfilesList(): List<User> {
        return suspendCoroutine { continuation ->
            firebaseDatabase.reference
                .child("profile")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val usersList = mutableListOf<User>()
                        for (ds in snapshot.children) {
                            val user = ds.getValue(User::class.java)
                            if (user?.id != FirebaseHelper.getUserId()) {
                                user?.let {
                                    usersList.add(it)
                                }
                            }

                        }
                        continuation.resumeWith(Result.success(usersList))

                    }

                    override fun onCancelled(error: DatabaseError) {
                        continuation.resumeWith(Result.failure(error.toException()))

                    }
                })

        }
    }
    override suspend fun saveImage(imageProfile: String): String {
        return suspendCoroutine { continuation ->
            val uploadTask = profileStorageReference.putFile(Uri.parse(imageProfile))
            // verify if the image was saved successfully in firebase
            uploadTask.addOnSuccessListener {
                profileStorageReference.downloadUrl.addOnCompleteListener { task ->
                    continuation.resumeWith(Result.success(task.result.toString()))

                }

            }.addOnFailureListener{
                continuation.resumeWith(Result.failure(it))
            }
        }
    }
}