package com.project.digitalbank.data.repository.transfer

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.project.digitalbank.data.enum.TransactionOperation
import com.project.digitalbank.data.enum.TransactionType
import com.project.digitalbank.data.model.Transaction
import com.project.digitalbank.data.model.Transfer
import com.project.digitalbank.util.FirebaseHelper
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class TransferDataSourceImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : TransferDataSource {

    private val transferReference = firebaseDatabase.reference
        .child("transfer")

    private val transactionReference = firebaseDatabase.reference
        .child("transaction")

    override suspend fun saveTransfer(transfer: Transfer): Transfer {
        return suspendCoroutine { continuation ->
            transferReference
                .child(transfer.idUserSender)
                .child(transfer.id)
                .setValue(transfer)
                .addOnCompleteListener { taskSender ->
                    if (taskSender.isSuccessful) {
                        transfer.type = TransactionType.CASH_IN
                        transferReference
                            .child(transfer.idUserRecipient)
                            .child(transfer.id)
                            .setValue(transfer)
                            .addOnCompleteListener { taskRecipient ->
                                if (taskRecipient.isSuccessful) {
                                    continuation.resumeWith(Result.success(transfer))

                                } else {
                                    taskRecipient.exception?.let {
                                        continuation.resumeWith(Result.failure(it))
                                    }
                                }

                            }

                    } else {
                        taskSender.exception?.let {
                            continuation.resumeWith(Result.failure(it))
                        }
                    }
                }
        }
    }

    override suspend fun updateTransfer(transfer: Transfer) {
        return suspendCoroutine { continuation ->
            transferReference
                .child(transfer.idUserSender)
                .child(transfer.id)
                .child("date")
                .setValue(ServerValue.TIMESTAMP)
                .addOnCompleteListener { taskSender ->
                    if (taskSender.isSuccessful) {
                        transferReference
                            .child(transfer.idUserRecipient)
                            .child(transfer.id)
                            .child("date")
                            .setValue(ServerValue.TIMESTAMP)
                            .addOnCompleteListener { taskRecipient ->
                                if (taskRecipient.isSuccessful) {
                                    continuation.resumeWith(Result.success(Unit))
                                } else {
                                    taskRecipient.exception?.let {
                                        continuation.resumeWith(Result.failure(it))
                                    }
                                }
                            }


                    } else {
                        taskSender.exception?.let {
                            continuation.resumeWith(Result.failure(it))
                        }
                    }
                }

        }
    }

    override suspend fun getTransfer(transferID: String): Transfer {
        return suspendCoroutine { continuation ->
            transferReference
                .child(FirebaseHelper.getUserId())
                .child(transferID)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val transfer = snapshot.getValue(Transfer::class.java)
                        transfer?.let { continuation.resumeWith(Result.success(transfer)) }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        continuation.resumeWith(Result.failure(error.toException()))
                    }
                })
        }
    }

    override suspend fun saveTransferTransaction(transfer: Transfer) {
        return suspendCoroutine { continuation ->

            val transactionSender = Transaction(
                id = transfer.id,
                date = transfer.date,
                value = transfer.value,
                type = TransactionType.CASH_OUT,
                operation = TransactionOperation.TRANSFER
            )

            val transactionRecipient = Transaction(
                id = transfer.id,
                date = transfer.date,
                value = transfer.value,
                type = TransactionType.CASH_IN,
                operation = TransactionOperation.TRANSFER
            )

            transactionReference
                .child(transfer.idUserSender)
                .child(transfer.id)
                .setValue(transactionSender)
                .addOnCompleteListener { taskSender ->
                    if (taskSender.isSuccessful) {

                        transactionReference
                            .child(transfer.idUserRecipient)
                            .child(transfer.id)
                            .setValue(transactionRecipient)
                            .addOnCompleteListener { taskRecipient ->
                                if (taskRecipient.isSuccessful) {

                                    continuation.resumeWith(Result.success(Unit))
                                } else {
                                    taskRecipient.exception?.let {
                                        continuation.resumeWith(Result.failure(it))
                                    }
                                }
                            }

                    } else {
                        taskSender.exception?.let {
                            continuation.resumeWith(Result.failure(it))
                        }
                    }
                }
        }
    }

    override suspend fun updateTransferTransaction(transfer: Transfer) {
        return suspendCoroutine { continuation ->
            transactionReference
                .child(transfer.idUserSender)
                .child(transfer.id)
                .child("date")
                .setValue(ServerValue.TIMESTAMP)
                .addOnCompleteListener { taskSender ->
                    if (taskSender.isSuccessful) {
                        transactionReference
                            .child(transfer.idUserRecipient)
                            .child(transfer.id)
                            .child("date")
                            .setValue(ServerValue.TIMESTAMP)
                            .addOnCompleteListener { taskRecipient ->
                                if (taskRecipient.isSuccessful) {
                                    continuation.resumeWith(Result.success(Unit))
                                } else {
                                    taskRecipient.exception?.let {
                                        continuation.resumeWith(Result.failure(it))
                                    }
                                }
                            }


                    } else {
                        taskSender.exception?.let {
                            continuation.resumeWith(Result.failure(it))
                        }
                    }
                }

        }
    }
}