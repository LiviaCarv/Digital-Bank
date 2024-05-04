package com.project.digitalbank.data.repository.deposit

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.project.digitalbank.data.model.Deposit
import com.project.digitalbank.util.FirebaseHelper
import com.project.digitalbank.util.StateView
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class DepositDataSourceImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : DepositDataSource {

    private val depositReference = firebaseDatabase.reference
        .child("deposit")
        .child(FirebaseHelper.getUserId())

    override suspend fun getDepositFromDatabase(idDeposit: String) : Deposit {
        return suspendCoroutine { continuation ->
            depositReference
                .child(idDeposit)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val deposit = snapshot.getValue(Deposit::class.java) as Deposit
                        continuation.resumeWith(Result.success(deposit))
                    }

                    override fun onCancelled(error: DatabaseError) {
                        continuation.resumeWith(Result.failure(error.toException()))
                    }
                })
        }
    }

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