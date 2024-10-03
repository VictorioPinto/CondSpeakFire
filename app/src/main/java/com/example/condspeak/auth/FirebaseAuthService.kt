package com.example.condspeak.auth

import com.google.firebase.auth.FirebaseAuth

object FirebaseAuthService {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getCurrentUser() = auth.currentUser
}
