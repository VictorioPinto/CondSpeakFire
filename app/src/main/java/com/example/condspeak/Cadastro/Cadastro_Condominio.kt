package com.example.condspeak.Cadastro

import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.condspeak.R
import com.example.condspeak.selcionacond.Tela_Principal
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore


class Cadastro_Condominio : AppCompatActivity() {
    private lateinit var email: EditText
    private lateinit var CEP: EditText
    private lateinit var CNPJ: EditText
    private lateinit var numero: EditText
    private lateinit var Cadastrar: Button
    private lateinit var nome: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cadastro_condominio)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        nome = findViewById(R.id.edtnome)
        email = findViewById(R.id.edtEmail)
        CEP = findViewById(R.id.edtCep)
        CNPJ = findViewById(R.id.edtCnpj)
        numero = findViewById(R.id.edtNum)
        Cadastrar = findViewById(R.id.btnCadastrar)




        Cadastrar.setOnClickListener {
            cadastro()
        }
    }

    private fun cadastro() { if (nome.text.isEmpty() || email.text.isEmpty() || CEP.text.isEmpty() || CNPJ.text.isEmpty() || numero.text.isEmpty()) {
        Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
    } else {
        val codigo = gerarCodigo()
        val condominio = hashMapOf(
            "nome" to nome.text.toString(),
            "email" to email.text.toString(),
            "CNPJ" to CNPJ.text.toString(),
            "CEP" to CEP.text.toString(),
            "numero" to numero.text.toString(),
            "codigo" to codigo

        )
        val docRef = Firebase.firestore.collection("condominios").document(codigo)

        // Now you can use docRef to perform operations like setting data
        docRef.set(condominio)
            .addOnSuccessListener {
                Log.d("TAG", "DocumentSnapshot successfully written!")
                criarcondominiocomcliente(codigo)
            }
            .addOnFailureListener { e ->
                Log.d("TAG", "Error writing document", e)
            }
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
    private fun criarcondominiocomcliente(codigo: String) {
        val usuarioId = Firebase.auth.currentUser!!.uid
        val cliente = hashMapOf(
            "codigo" to codigo,
            "iddono" to usuarioId,
        )
        val docRef = Firebase.firestore.collection("clientescondominio").document(codigo)
        docRef.set(cliente)
            .addOnSuccessListener {
                salvarnodono(usuarioId, codigo)
                Log.d("TAG", "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                Log.d("TAG", "Error writing document", e)
            }
    }
    private fun salvarnodono(usuarioId: String, codigo: String) {
        val docRef = Firebase.firestore.collection("clientes").document(usuarioId)
        docRef.update("codigodono", FieldValue.arrayUnion(codigo))
            .addOnSuccessListener {
                Intent(this, Tela_Principal::class.java)
                startActivity(intent)
                Log.d("Firestore", "Campo adicionado com sucesso!")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Erro ao adicionar campo", e)
            }
    }

}