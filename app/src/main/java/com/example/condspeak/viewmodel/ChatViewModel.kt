package com.example.condspeak.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.condspeak.data.repository.ChatRepository
import com.example.condspeak.ui.chat.chatcondominio.model.Mensagem
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    private val repository = ChatRepository()

    private val _mensagens = MutableLiveData<List<Mensagem>>()
    val mensagens: LiveData<List<Mensagem>> get() = _mensagens

    fun obterMensagens() {
        viewModelScope.launch {
            val result = repository.obterMensagens()
            _mensagens.value = result
        }
    }

    fun enviarMensagem(conteudo: String) {
        viewModelScope.launch {
            repository.enviarMensagem(conteudo)
            obterMensagens()  // Atualiza mensagens após o envio
        }
    }
    fun obterMensagensa(codigoCondominio: String, iddapessoa: String, tipoUsuario: String) {
        viewModelScope.launch {
            val result = repository.obterMensagensa(codigoCondominio, iddapessoa, tipoUsuario)
            _mensagens.value = result
        }
    }

    fun enviarMensagema(codigoCondominio: String, iddapessoa: String, tipoUsuario: String, conteudo: String) {
        viewModelScope.launch {
            repository.enviarMensagema(codigoCondominio, iddapessoa, tipoUsuario, conteudo)
            obterMensagensa(codigoCondominio, iddapessoa, tipoUsuario)
        }
    }
}
