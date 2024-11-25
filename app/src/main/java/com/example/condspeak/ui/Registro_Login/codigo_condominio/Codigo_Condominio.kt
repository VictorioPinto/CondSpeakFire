package com.example.condspeak.ui.Registro_Login.codigo_condominio

import com.example.condspeak.ui.Cadastro.CodigoCondominioViewModel
import com.example.condspeak.ui.Registro_Login.registercondominio.CadastroCondominioActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.condspeak.R
import com.example.condspeak.data.repository.CondominioRepository
import com.example.condspeak.ui.Nav_drawer.selecionar_condominio.SelecionaCondominio

class Codigo_Condominio : AppCompatActivity() {
    private val repository = CondominioRepository() // Instanciando o repositório diretamente
    private val viewModel = CodigoCondominioViewModel(repository) // Instanciando o ViewModel diretamente

    private lateinit var code: EditText
    private lateinit var entrar: EditText
    private lateinit var cadastrarnocondominio: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_codigo_condominio)

        cadastrarnocondominio = findViewById(R.id.txtCadCond)
        code = findViewById(R.id.edtCode)
        entrar = findViewById(R.id.btnentrar)


        cadastrarnocondominio.setOnClickListener {
            teladecadastrocondominio()
        }

        entrar.setOnClickListener {
            entra()
        }

        viewModel.isCondominioValid.observe(this, Observer { isValid ->
            if (isValid) {
                criarcondominiocomcliente(code.text.toString())
            } else {
                code.error = "Código inválido"
            }
        })

        viewModel.usuarioAdicionado.observe(this, Observer { added ->
            if (added) {
                val intent = Intent(this, SelecionaCondominio::class.java)
                startActivity(intent)
            } else {
                Log.d("TAG", "Usuário já está na lista do condomínio.")
            }
        })
    }

    private fun teladecadastrocondominio() {
        startActivity(Intent(this, CadastroCondominioActivity::class.java))
    }

    private fun entra() {
        val codigo = code.text.toString()

        if (codigo.isEmpty()) {
            code.error = "Preencha o código"
            return
        }

        viewModel.verificaCondominio(codigo)
    }

    private fun criarcondominiocomcliente(codigo: String) {
        viewModel.adicionarUsuario(codigo)
    }
}