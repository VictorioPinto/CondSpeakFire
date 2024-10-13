// ViewModel
package com.example.condspeak.ui.Cadastro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.condspeak.data.repository.CondominioRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CodigoCondominioViewModel(private val repository: CondominioRepository) : ViewModel() {
    private val _isCondominioValid = MutableLiveData<Boolean>()
    val isCondominioValid: LiveData<Boolean> get() = _isCondominioValid

    private val _usuarioAdicionado = MutableLiveData<Boolean>()
    val usuarioAdicionado: LiveData<Boolean> get() = _usuarioAdicionado

    private val _codigoAdicionado = MutableLiveData<Boolean>()
    val codigoAdicionado: LiveData<Boolean> get() = _codigoAdicionado

    fun verificaCondominio(codigo: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val isValid = repository.getCondominio(codigo)
            withContext(Dispatchers.Main) {
                _isCondominioValid.value = isValid
            }
        }
    }

    fun adicionarUsuario(codigo: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val added = repository.addUsuarioToCondominio(codigo)
            withContext(Dispatchers.Main) {
                _usuarioAdicionado.value = added
                if (added) {
                    adicionarCodigoUsuario(codigo)
                }
            }
        }
    }

    private fun adicionarCodigoUsuario(codigo: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val added = repository.adicionarCodigoUsuario(codigo)
            withContext(Dispatchers.Main) {
                _codigoAdicionado.value = added
            }
        }
    }
}
