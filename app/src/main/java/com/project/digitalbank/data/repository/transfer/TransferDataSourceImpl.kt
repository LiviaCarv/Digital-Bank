package com.project.digitalbank.data.repository.transfer

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.project.digitalbank.data.model.Transfer
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class TransferDataSourceImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : TransferDataSource {

    private val transferReference = firebaseDatabase.reference
        .child("transfer")

    override suspend fun saveTransfer(transfer: Transfer): Transfer {
        return suspendCoroutine { continuation ->
            transferReference
                .child(transfer.idUserSender)
                .child(transfer.id)
                .setValue(transfer)
                .addOnCompleteListener { taskSender ->
                    if (taskSender.isSuccessful) {

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
}