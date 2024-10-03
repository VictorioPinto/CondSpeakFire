package com.example.condspeak.ui.registercondominio

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.condspeak.R
import com.example.condspeak.data.repository.CadastroRepository
import com.example.condspeak.selcionacond.Tela_Principal

class CadastroCondominioActivity : AppCompatActivity(), CadastroView {

    private lateinit var presenter: CadastroPresenter
    private lateinit var email: EditText
    private lateinit var CEP: EditText
    private lateinit var CNPJ: EditText
    private lateinit var numero: EditText
    private lateinit var Cadastrar: Button
    private lateinit var nome: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_condominio)

        nome = findViewById(R.id.edtnome)
        email = findViewById(R.id.edtEmail)
        CEP = findViewById(R.id.edtCep)
        CNPJ = findViewById(R.id.edtCnpj)
        numero = findViewById(R.id.edtNum)
        Cadastrar = findViewById(R.id.btnCadastrar)

        val repository = CadastroRepository()
        presenter = CadastroPresenter(this, repository)

        Cadastrar.setOnClickListener {
            presenter.cadastro(
                nome.text.toString(),
                email.text.toString(),
                CNPJ.text.toString(),
                CEP.text.toString(),
                numero.text.toString()
            )
        }
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun goToMainScreen() {
        val intent = Intent(this, Tela_Principal::class.java)
        startActivity(intent)
    }
}
