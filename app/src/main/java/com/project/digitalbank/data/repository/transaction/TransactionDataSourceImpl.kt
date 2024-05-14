package com.project.digitalbank.data.repository.transaction

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.project.digitalbank.data.enum.TransactionType
import com.project.digitalbank.data.model.Transaction
import com.project.digitalbank.util.FirebaseHelper
import com.project.digitalbank.util.StateView
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class TransactionDataSourceImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : TransactionDataSource {

    private val transactionReference = firebaseDatabase.reference
        .child("transaction")
        .child(FirebaseHelper.getUserId())

    override suspend fun saveTransaction(transaction: Transaction) {
        return suspendCoroutine { continuation ->
            transactionReference
                .child(transaction.id)
                .setValue(transaction)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        // saving date from database
                        transactionReference
                            .child(transaction.id)
                            .child("date")
                            .setValue(ServerValue.TIMESTAMP)
                            .addOnCompleteListener { result ->
                                if (result.isSuccessful) {
                                    continuation.resumeWith(Result.success(Unit))
                                } else {
                                    task.exception?.let {
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

    override suspend fun getTransactions(): List<Transaction> {
        return suspendCoroutine { continuation ->
            transactionReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val transactions = mutableListOf<Transaction>()
                    for (ds in snapshot.children) {
                        val transaction = ds.getValue(Transaction::class.java) as Transaction
                        transactions.add(transaction)
                    }
                    continuation.resumeWith(Result.success(transactions))
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resumeWith(Result.failure(error.toException()))
                }
            })
        }
    }

    override suspend fun getBalance(): Float {
        var balance = 0f
        val transactionsList = getTransactions()
        for (transaction in transactionsList) {
            if (transaction.type == TransactionType.CASH_IN) {
                balance += transaction.value
            } else {
                balance -= transaction.value
            }
        }

        return balance
    }
}