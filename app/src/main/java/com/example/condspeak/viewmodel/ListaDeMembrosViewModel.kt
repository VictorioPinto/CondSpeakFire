package com.example.condspeak.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.condspeak.data.model.Model_Membros
import com.example.condspeak.data.repository.CondominioRepository
import com.example.condspeak.data.repository.UserRepository
import kotlinx.coroutines.launch

class ListaDeMembrosViewModel : ViewModel() {

    private val _membros = MutableLiveData<List<Model_Membros>>()
    val membros: LiveData<List<Model_Membros>> get() = _membros

    private val repository = CondominioRepository()

    fun carregarMembros(codigoCondominio: String) {
        viewModelScope.launch {
            val membros = repository.obterMembros(codigoCondominio)
            _membros.postValue(membros)
        }
    }
}
