package com.example.condspeak.viewmodel

import SelecionaCondominioModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ListenerRegistration
import com.example.condspeak.data.repository.CondominioRepository
class CondominioViewModel : ViewModel() {
    private val repository = CondominioRepository()
    private val _condominioList = MutableLiveData<List<SelecionaCondominioModel>>()
    val condominioList: LiveData<List<SelecionaCondominioModel>> get() = _condominioList
    private lateinit var firestoreListener: ListenerRegistration  // Corrected type

    fun iniciarFirestoreListener() {
        firestoreListener = repository.iniciarFirestoreListener { condominios: List<SelecionaCondominioModel> ->
            _condominioList.value = condominios
        }
    }

    fun removerFirestoreListener() {
        if (::firestoreListener.isInitialized) {
            firestoreListener.remove()
        }
    }
}