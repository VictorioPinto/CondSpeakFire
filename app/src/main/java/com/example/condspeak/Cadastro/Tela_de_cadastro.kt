package com.example.condspeak.Cadastro


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.condspeak.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore

class Tela_de_cadastro : AppCompatActivity() {

    lateinit var nome: EditText
    lateinit var email: EditText
    lateinit var senha: EditText
    lateinit var telefone: EditText
    lateinit var cpf: EditText
    lateinit var VoltarConta: TextView
    lateinit var CadastrarConta: Button
    lateinit var usuarioid: String

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela_de_cadastro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        nome = findViewById(R.id.edtNome)
        email = findViewById(R.id.edtEmail)
        senha = findViewById(R.id.edtSenhacliente)
        telefone = findViewById(R.id.edttelefone)
        cpf = findViewById(R.id.edtCpf)
        VoltarConta = findViewById(R.id.txtVoltarConta)
        CadastrarConta = findViewById(R.id.btnCadastrar)

        VoltarConta.setOnClickListener { voltar() }
        CadastrarConta.setOnClickListener { Cadastrar() }

    }

    private fun voltar() {
        startActivity(Intent(this, Tela_De_Login::class.java))
    }

    private fun Cadastrar() {
        if (nome.text.isEmpty() || email.text.isEmpty() || senha.text.isEmpty() || telefone.text.isEmpty() || cpf.text.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
        } else {
            Firebase.auth.createUserWithEmailAndPassword(
                email.text.toString(),
                senha.text.toString()
            )
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT)
                            .show()
                        SalvarDados()
                    } else {
                        // Handle sign-up errors here
                        Toast.makeText(
                            this,
                            "Erro ao cadastrar: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
    private fun SalvarDados(){
        val cliente = hashMapOf(
            "nome" to nome.text.toString(),
            "email" to email.text.toString(),
            "senha" to senha.text.toString(),
            "telefone" to telefone.text.toString(),
            "cpf" to cpf.text.toString(),
            "codigo" to emptyList<String>()

        )
        usuarioid = Firebase.auth.currentUser!!.uid
        val docRef = Firebase.firestore.collection("clientes").document(usuarioid)

        // Now you can use docRef to perform operations like setting data
        docRef.set(cliente)
            .addOnSuccessListener {
                Log.d("TAG", "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                Log.d("TAG", "Error writing document", e)
            }
    }
}
