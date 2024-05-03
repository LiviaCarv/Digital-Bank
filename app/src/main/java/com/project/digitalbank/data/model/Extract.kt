package com.project.digitalbank.data.model

import com.google.firebase.database.FirebaseDatabase

data class Extract(
    var id: String = "",
    val operation: String = "",
    val date: Long = 0L,
    val value: Float= 0f,
    val type: String = "",

    ) {
    init {
        this.id = FirebaseDatabase.getInstance().reference.push().key ?: ""
    }
}
