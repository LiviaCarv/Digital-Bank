package com.project.digitalbank.data.repository.recharge

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.project.digitalbank.data.model.Recharge
import com.project.digitalbank.util.FirebaseHelper
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class RechargeDataSourceImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : RechargeDataSource {

    private val rechargeReference = firebaseDatabase.reference
        .child("recharge")
        .child(FirebaseHelper.getUserId())

    override suspend fun saveRecharge(recharge: Recharge): Recharge {
        return suspendCoroutine { continuation ->
            rechargeReference
                .child(recharge.id)
                .setValue(recharge)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        rechargeReference
                            .child(recharge.id)
                            .child("date")
                            .setValue(ServerValue.TIMESTAMP)
                            .addOnCompleteListener { result ->
                                if (result.isSuccessful) {
                                    continuation.resumeWith(Result.success(recharge))
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

    override suspend fun getRechargeFromDatabase(idRecharge: String): Recharge {
        TODO("Not yet implemented")
    }
}