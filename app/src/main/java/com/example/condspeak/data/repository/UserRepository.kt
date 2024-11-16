package com.example.condspeak.data.repository

import android.util.Log
import com.example.condspeak.data.model.User
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserRepository {

    private val auth: FirebaseAuth = Firebase.auth
    private val firestore: FirebaseFirestore = Firebase.firestore

    fun registerUser(email: String, senha: String, onComplete: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null) // Registration successful
                } else {
                    onComplete(false, task.exception?.message) // Registration failed
                }
            }
    }

    fun saveUserData(user: User, onComplete: (Boolean, String?) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        val docRef = firestore.collection("Users").document(userId)

        docRef.set(user)
            .addOnSuccessListener {
                onComplete(true, null)
            }
            .addOnFailureListener { e ->
                onComplete(false, e.message)
            }
    }

    suspend fun signInWithEmailAndPassword(email: String, password: String): Boolean = withContext(Dispatchers.IO) {
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            // Lidar com a exceção (ex: log, mostrar mensagem de erro)
            false
        }
    }

    suspend fun createUserWithEmailAndPassword(email: String, password: String): Boolean = withContext(Dispatchers.IO) {
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            // Lidar com a exceção (ex: log, mostrar mensagem de erro)
            false
        }
    }

    suspend fun sendPasswordResetEmail(email: String): Boolean = withContext(Dispatchers.IO) {
        try {
            auth.sendPasswordResetEmail(email).await()
            true
        } catch (e: Exception) {
            // Lidar com a exceção (ex: log, mostrar mensagem de erro)
            false
        }
    }

    fun signOut() {
        auth.signOut()
    }

    suspend fun signInWithGoogle(account: GoogleSignInAccount): Boolean = withContext(Dispatchers.IO) {
        try {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential).await()
            true
        } catch (e: Exception) {
            // Lidar com a exceção (ex: log, mostrar mensagem de erro)
            false
        }
    }
    suspend fun updateUserData(user: User): Boolean = withContext(Dispatchers.IO) {
        try {
            val userId = auth.currentUser?.uid ?: return@withContext false
            val docRef = firestore.collection("Users").document(userId)
            docRef.set(user).await()
            true
        } catch (e: Exception) {
            // Lidar com a exceção (ex: log, mostrar mensagem de erro)
            false
        }
    }

    suspend fun getUserData(userId: String): User? = withContext(Dispatchers.IO) {
        try {
            val document = firestore.collection("Users").document(userId).get().await()
            if (document.exists()) {
                return@withContext document.toObject(User::class.java)
            } else {
                return@withContext null
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Error getting user data", e)
            return@withContext null
        }
    }
    suspend fun deleteUser(userId: String) {
        try {
            firestore.collection("Users").document(userId).delete().await()
            auth.currentUser?.delete()?.await()
            Log.d("UserRepository", "User deleted successfully")
        } catch (e: Exception) {
            Log.e("UserRepository", "Error deleting user", e)
        }
    }
}