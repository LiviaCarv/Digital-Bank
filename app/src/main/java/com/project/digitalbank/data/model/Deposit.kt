package com.project.digitalbank.data.model

import com.google.firebase.database.FirebaseDatabase

data class Deposit(
    var id: String = "",
    val date: Long = 0L,
    val value: Float= 0f,
) {
    init {
        this.id = FirebaseDatabase.getInstance().reference.push().key ?: ""
    }
}