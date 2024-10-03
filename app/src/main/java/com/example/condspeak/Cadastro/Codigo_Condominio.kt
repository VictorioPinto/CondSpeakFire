package com.example.condspeak.Cadastro

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.condspeak.R
import com.example.condspeak.selcionacond.Tela_Principal
import com.example.condspeak.ui.registercondominio.CadastroCondominioActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source

class Codigo_Condominio : AppCompatActivity() {

    lateinit var code: EditText
    lateinit var entrar: Button
    lateinit var cadastrarnocondominio: TextView
    lateinit var btnteste : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_codigo_condominio)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        cadastrarnocondominio = findViewById(R.id.txtCadCond)
        code = findViewById(R.id.edtCode)
        entrar = findViewById(R.id.btnentrar)
        btnteste = findViewById(R.id.button)
        btnteste.setOnClickListener {
            val intent = Intent(this, Tela_Principal::class.java)
            startActivity(intent)
        }

        cadastrarnocondominio.setOnClickListener {

            teladecadastrocondominio()
        }


        entrar.setOnClickListener {

            entra()
        }
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


        val docRef = FirebaseFirestore.getInstance().collection("condominios").document(codigo)

        docRef.get(Source.SERVER)
            .addOnSuccessListener { document ->

                if (document != null && document.exists()) {

                    criarcondominiocomcliente(codigo)
                } else {

                    code.error = "Código inválido"
                }
            }

            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting document", exception)

            }
    }


    private fun criarcondominiocomcliente(codigo: String) {

        val usuarioId = Firebase.auth.currentUser!!.uid

        val db = FirebaseFirestore.getInstance()

        val docRef = db.collection("clientescondominio").document(codigo)


        docRef.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val idDosClientes = document.get("iddosclientes") as? MutableList<String> ?: mutableListOf()

                if (!idDosClientes.contains(usuarioId)) { // Verifica se o ID já existe
                    idDosClientes.add(usuarioId)

                    docRef.update("iddosclientes", idDosClientes)
                        .addOnSuccessListener {
                            Log.d("TAG", "Usuário adicionado ao documento com sucesso!")
                            verificarlogar(usuarioId, codigo)
                            val intent = Intent(this, Tela_Principal::class.java)
                            startActivity(intent)
                        }
                        .addOnFailureListener { e ->
                            Log.w("TAG", "Erro ao adicionar usuário ao documento", e)
                        }
                } else {
                    Log.d("TAG", "Usuário já está na lista do condomínio.")
                }
            } else {
                // TODO: Tratar caso o documento não exista (criar um novo documento)
            }
        }.addOnFailureListener { e ->
            Log.w("TAG","Erro ao obter documento", e)
        }
    }

    private fun verificarlogar(usuarioId: String, codigo: String) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("clientes").document(usuarioId)
        docRef.update("codigo", FieldValue.arrayUnion(codigo))
            .addOnSuccessListener {
                Log.d(TAG, "Itens adicionados à lista de codominios!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Erro ao adicionar itens à lista de codominios", e)
            }
    }
}