package com.project.digitalbank.data.repository.wallet

import com.google.firebase.database.FirebaseDatabase
import com.project.digitalbank.data.model.Wallet
import com.project.digitalbank.util.FirebaseHelper
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class WalletDataSourceImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : WalletDataSource {

    private val walletPreferences = firebaseDatabase.reference
        .child("wallet")

    override suspend fun initWallet(wallet: Wallet) {
        return suspendCoroutine { continuation ->
            walletPreferences
                .child(wallet.id)
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
}