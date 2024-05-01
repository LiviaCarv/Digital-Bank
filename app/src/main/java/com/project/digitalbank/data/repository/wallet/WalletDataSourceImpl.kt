package com.project.digitalbank.data.repository.wallet

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.project.digitalbank.data.model.Wallet
import com.project.digitalbank.util.FirebaseHelper
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class WalletDataSourceImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : WalletDataSource {

    private val walletReference = firebaseDatabase.reference
        .child("wallet")
        .child(FirebaseHelper.getUserId())

    override suspend fun initWallet(wallet: Wallet) {
        return suspendCoroutine { continuation ->
            walletReference
                .setValue(wallet)
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

    override suspend fun getWallet(): Wallet {
        return suspendCoroutine { continuation ->
            walletReference
                .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val wallet = snapshot.getValue(Wallet::class.java)

                            wallet?.let {
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

