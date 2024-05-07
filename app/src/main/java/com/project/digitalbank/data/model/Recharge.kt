package com.project.digitalbank.data.model

import android.os.Parcelable
import com.google.firebase.database.FirebaseDatabase
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recharge(
    var id: String = "",
    var date: Long = 0L,
    var amount: Float = 0f,
    var phoneNumber: String = ""
) : Parcelable {
    init {
        this.id = FirebaseDatabase.getInstance().reference.push().key ?: ""
    }
}