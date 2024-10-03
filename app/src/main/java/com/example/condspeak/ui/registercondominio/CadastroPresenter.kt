package com.example.condspeak.ui.registercondominio

import com.example.condspeak.data.model.Condominio
import com.example.condspeak.data.repository.CadastroRepository


class CadastroPresenter(private val view: CadastroView, private val repository: CadastroRepository) {

    fun cadastro(nome: String, email: String, CNPJ: String, CEP: String, numero: String) {
        if (nome.isEmpty() || email.isEmpty() || CNPJ.isEmpty() || CEP.isEmpty() || numero.isEmpty()) {
            view.showError("Preencha todos os campos!")
        } else {
            val codigo = gerarCodigo()
            val condominio = Condominio(
                nome = nome,
                email = email,
                CNPJ = CNPJ,
                CEP = CEP,
                numero = numero,
                codigo = codigo
            )

            repository.cadastrarCondominio(condominio)
                .addOnSuccessListener {
                    repository.criarCondominioComCliente(codigo)
                        .addOnSuccessListener {
                            val usuarioId = repository.auth.currentUser!!.uid
                            repository.salvarNoDono(usuarioId, codigo)
                                .addOnSuccessListener {
                                    view.goToMainScreen()
                                }
                                .addOnFailureListener { e -> view.showError("Erro ao adicionar campo")
                                }


                        }
                        .addOnFailureListener { e -> view.showError("Erro ao criar condomínio") }
                }
                .addOnFailureListener { e -> view.showError("Erro ao cadastrar condomínio") }
        }
    }

    private fun gerarCodigo(): String {
        val caracteres = "0123456789ABCDEFGHIJKLMNOPQ"
        val codigo = StringBuilder()
        for (i in 0 until 6) {
            val indice = (caracteres.indices).random()
            codigo.append(caracteres[indice])
        }
        return codigo.toString()
    }
}
