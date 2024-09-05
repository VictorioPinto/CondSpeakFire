package com.example.condspeak.chat

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.condspeak.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class Chat_condominio : AppCompatActivity() {

    private lateinit var rvMensagens: RecyclerView
    private lateinit var etMensagem: EditText
    private lateinit var btnEnviar: Button

    private lateinit var mensagensAdapter:MensagensAdapter
    private val mensagens = mutableListOf<Mensagem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_condominio)

        rvMensagens = findViewById(R.id.rvMensagens)
        etMensagem = findViewById(R.id.etMensagem)
        btnEnviar = findViewById(R.id.btnEnviar)

        mensagensAdapter = MensagensAdapter(mensagens)
        rvMensagens.adapter = mensagensAdapter
        rvMensagens.layoutManager = LinearLayoutManager(this)

        btnEnviar.setOnClickListener {
            enviarMensagem()
        }


        // Obter o código do condomínio da Intent
        val codigoCondominio = intent.getStringExtra("CodigoCondominio") ?: ""

        // Buscar mensagens do banco de dados
        buscarMensagens(codigoCondominio)
    }

    private fun buscarMensagens(codigoCondominio: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("chats")
            .document(codigoCondominio)
            .collection("mensagens")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("Chat", "Erro ao obter mensagens", e)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    mensagens.clear()
                    for (document in snapshot) {
                        val mensagem = document.toObject(Mensagem::class.java)
                        mensagens.add(mensagem)
                    }
                    mensagensAdapter.notifyDataSetChanged()
                    rvMensagens.scrollToPosition(mensagens.size - 1)
                }
            }
    }

    private fun enviarMensagem() {
        val textoMensagem = etMensagem.text.toString()
        if (textoMensagem.isBlank()) return

        val codigoCondominio = intent.getStringExtra("CodigoCondominio") ?: ""
        val usuarioId = Firebase.auth.currentUser?.uid ?: ""

        val db = FirebaseFirestore.getInstance()

        db.collection("clientes")
            .document(usuarioId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val nomeCliente = document.getString("nome") ?: ""

                    val mensagem = Mensagem(
                        texto = textoMensagem,
                        remetente = nomeCliente,
                        timestamp = FieldValue.serverTimestamp()
                    )

                    db.collection("chats")
                        .document(codigoCondominio)
                        .collection("mensagens")
                        .add(mensagem)
                        .addOnSuccessListener {
                            etMensagem.text.clear()
                        }
                        .addOnFailureListener { e ->
                            Log.w("Chat", "Erro ao enviar mensagem", e)
                        }
                } else {
                    // Lidar com o caso em que o usuário não é encontrado
                }
            }
            .addOnFailureListener { e ->
                // Lidar com o erro
            }
    }
}
