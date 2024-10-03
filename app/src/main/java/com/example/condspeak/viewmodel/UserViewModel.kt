package com.example.condspeak.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.condspeak.data.model.User
import com.example.condspeak.data.repository.UserRepository
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val userRepository = UserRepository()

    private val _registrationStatus = MutableLiveData<String>()
    val registrationStatus: LiveData<String> get() = _registrationStatus

    private val _saveDataStatus = MutableLiveData<String>()
    val saveDataStatus: LiveData<String> get() = _saveDataStatus

    fun registerUser(email: String, senha: String, onComplete: (Boolean, String?) -> Unit) {
        userRepository.registerUser(email, senha, onComplete)
    }

    suspend fun deletarConta(userId: String) {
        userRepository.deleteUser(userId)

    }
    fun saveUserData(user: User) {
        userRepository.saveUserData(user) { success, message ->
            if (success) {
                _saveDataStatus.value = "Dados salvos com sucesso!"
            } else {
                _saveDataStatus.value = "Erro ao salvar dados: $message"
            }
        }
    }

    fun signInWithEmail(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val success = userRepository.signInWithEmailAndPassword(email, password)
            onComplete(success, null)
        }
    }

    fun getUserData(userId: String, onComplete: (User?) -> Unit) {
        viewModelScope.launch {
            try {
                val user = userRepository.getUserData(userId)
                onComplete(user)
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error getting user data", e)
                onComplete(null) // Notify with null and handle error in UI
            }
        }
    }


    fun signInWithGoogle(account: GoogleSignInAccount, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = userRepository.signInWithGoogle(account)
            onComplete(success)
        }
    }

    fun sendPasswordResetEmail(email: String, onComplete: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val success = userRepository.sendPasswordResetEmail(email)
            onComplete(success, null)
        }
    }
}