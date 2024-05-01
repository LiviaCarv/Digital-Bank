package com.project.digitalbank.data.model

import com.google.firebase.database.FirebaseDatabase
import java.util.Date

data class Transaction(
    var id: String = "",
    val description: String = "",
    val date: Long = 0L,
    val value: Float= 0f,
) {
    init {
        this.id = FirebaseDatabase.getInstance().reference.push().key ?: ""
    }
}
