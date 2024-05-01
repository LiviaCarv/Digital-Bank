package com.project.digitalbank.data.model

import com.google.firebase.database.FirebaseDatabase

data class Wallet(
    var id: String = "",
    val userId: String = "",
    var balance: Float = 0f
) {
    init {
        this.id = FirebaseDatabase.getInstance().reference.push().key ?: ""
    }
}