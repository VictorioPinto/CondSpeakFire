package com.example.condspeak.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.condspeak.data.model.Aviso
import com.example.condspeak.data.repository.AvisoRepository

class AvisoViewModel : ViewModel() {
    private val repository = AvisoRepository()
    private val _saveDataStatus = MutableLiveData<String>()
    val saveDataStatus: LiveData<String> get() = _saveDataStatus
    private val _avisos = MutableLiveData<List<Aviso>>()

    fun saveAvisoData(aviso: Aviso) {
        repository.saveAviso(aviso) { success, message ->
            if (success) {
                _saveDataStatus.value = "Dados salvos com sucesso!"
            } else {
                _saveDataStatus.value = "Erro ao salvar dados: $message"
            }
        }
    }
    fun getAvisos(): LiveData<List<Aviso>> {
        repository.getAvisos { avisos, exception ->
            if (exception == null) {
                _avisos.value = avisos ?: emptyList()
            } else {
                // Lidar com a exceção
            }
        }
        return _avisos // Retorna o LiveData
    }
}