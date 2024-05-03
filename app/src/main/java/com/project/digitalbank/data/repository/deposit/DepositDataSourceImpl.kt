package com.project.digitalbank.data.repository.deposit

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.project.digitalbank.data.model.Deposit
import com.project.digitalbank.util.FirebaseHelper
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class DepositDataSourceImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : DepositDataSource {

    private val depositReference = firebaseDatabase.reference
        .child("deposit")
        .child(FirebaseHelper.getUserId())

    override suspend fun saveDeposit(deposit: Deposit): Deposit {
        return suspendCoroutine { continuation ->
            depositReference
                .child(deposit.id)
                .setValue(deposit)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        depositReference
                            .child(deposit.id)
                            .child("date")
                            .setValue(ServerValue.TIMESTAMP)
                            .addOnCompleteListener { result ->
                                if (result.isSuccessful) {
                                    continuation.resumeWith(Result.success(deposit))
                                } else {
                                    result.exception?.let {
                                        continuation.resumeWith(Result.failure(it))
                                    }
                                }
                            }
                    } else {
                        task.exception?.let {
                            continuation.resumeWith(Result.failure(it))
                        }
                    }
                }
        }
    }
}