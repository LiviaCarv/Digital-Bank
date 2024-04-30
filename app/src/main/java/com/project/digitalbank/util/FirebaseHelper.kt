package com.project.digitalbank.util

import com.google.firebase.auth.FirebaseAuth

class FirebaseHelper {

    companion object {
        fun isAuthenticated() = FirebaseAuth.getInstance().currentUser != null
    }
}