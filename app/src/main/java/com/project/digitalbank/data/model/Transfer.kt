package com.project.digitalbank.data.model

import android.os.Parcelable
import com.google.firebase.database.FirebaseDatabase
import com.project.digitalbank.data.enum.TransactionType
import kotlinx.parcelize.Parcelize

@Parcelize
data class Transfer(
    var id : String = "",
    var idUserRecipient : String = "",
    var idUserSender : String = "",
    val date: Long = 0L,
    val value: Float= 0f,
    var type: TransactionType = TransactionType.CASH_OUT
) : Parcelable {

    init {
        this.id = FirebaseDatabase.getInstance().reference.push().key ?: ""
    }

}