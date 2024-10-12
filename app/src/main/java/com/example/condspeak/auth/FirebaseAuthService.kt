package com.example.condspeak.auth

import com.google.firebase.auth.FirebaseAuth

object FirebaseAuthService {
    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getCurrentUser() = auth.currentUser
}
