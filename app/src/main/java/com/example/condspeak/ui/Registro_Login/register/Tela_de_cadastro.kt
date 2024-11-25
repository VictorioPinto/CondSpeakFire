package com.example.condspeak.ui.Registro_Login.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.semantics.text
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.condspeak.data.model.User
import com.example.condspeak.databinding.ActivityTelaDeCadastroBinding
import com.example.condspeak.data.Masks.CpfMask
import com.example.condspeak.data.Masks.TelefoneMask
import com.example.condspeak.ui.Registro_Login.login.TelaDeLogin
import com.example.condspeak.viewmodel.UserViewModel

class Tela_de_cadastro : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()
    private lateinit var binding: ActivityTelaDeCadastroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTelaDeCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.txtVoltarConta.setOnClickListener { voltar() }
        binding.btnCadastrar.setOnClickListener { Cadastrar() }


        userViewModel.registrationStatus.observe(this) { status ->
            Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
        }


        userViewModel.saveDataStatus.observe(this) { status ->
            Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
        }
        binding.edtCpf.addTextChangedListener(CpfMask())
        binding.edttelefone.addTextChangedListener(TelefoneMask())
    }

    private fun voltar() {
        startActivity(Intent(this, TelaDeLogin::class.java))
    }

    private fun Cadastrar() {
        val imagem = "ImagemPerfil/4675159.png"
        val nome = binding.edtNome.text.toString()
        val email = binding.edtEmail.text.toString()
        val senha = binding.edtSenhacliente.text.toString()
        val telefone = binding.edttelefone.text.toString()
        val cpf = binding.edtCpf.text.toString()

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || telefone.isEmpty() || cpf.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
        } else {
            userViewModel.registerUser(email, senha) { success, message ->
                if (success) {
                    val user = User(imagem,nome, email, senha, telefone, cpf)
                    userViewModel.saveUserData(user)
                    startActivity(Intent(this, TelaDeLogin::class.java))
                } else {

                    Toast.makeText(this, "Erro ao cadastrar: $message", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}