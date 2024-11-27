package com.example.condspeak.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.condspeak.data.model.Aviso
import com.example.condspeak.data.model.User
import com.example.condspeak.data.repository.AvisoRepository

class AvisoViewModel : ViewModel() {
    private val repository = AvisoRepository()
    private val _saveDataStatus = MutableLiveData<String>()
    val saveDataStatus: LiveData<String> get() = _saveDataStatus
    fun saveAvisoData(aviso: Aviso) {
        repository.saveAviso(aviso){success, message ->
        if (success) {
            _saveDataStatus.value = "Dados salvos com sucesso!"
        } else {
            _saveDataStatus.value = "Erro ao salvar dados: $message"
        }
    }
    }

}