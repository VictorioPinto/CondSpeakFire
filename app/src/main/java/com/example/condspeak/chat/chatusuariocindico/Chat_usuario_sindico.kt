package com.example.condspeak.chat.chatusuariocindico

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.condspeak.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.Date

class Chat_usuario_sindico : AppCompatActivity() {

    private lateinit var rvMensagens: RecyclerView
    private lateinit var etMensagem: EditText
    private lateinit var btnEnviar: Button
    private lateinit var adapter: MensagemAdapter

    private val mensagens = mutableListOf<Mensagem>()
    private lateinit var codigoCondominio: String
    private lateinit var iddapessoa: String
    private lateinit var usuarioId: String
    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_usuario_sindico)

        rvMensagens = findViewById(R.id.rvMensagens)
        etMensagem = findViewById(R.id.etMensagem)
        btnEnviar = findViewById(R.id.btnEnviar)


        codigoCondominio = intent.getStringExtra("CodigoCondominio") ?: "seila"
        Log.d("Chat", "Código do condomínio: $codigoCondominio")
        usuarioId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        if (
            usuarioId == "XF5wOJbjThVWGQtBz1c38Pw6Vby2"
        ){
            iddapessoa = "CTG3eaRcuwhLmUj3qF2vfymt0CW2"
//            iddapessoa = intent.getStringExtra("iddapessoa") ?: "CTG3eaRcuwhLmUj3qF2vfymt0CW2"
        } else{
            iddapessoa = "XF5wOJbjThVWGQtBz1c38Pw6Vby2"
//            iddapessoa = intent.getStringExtra("iddapessoa") ?: "XF5wOJbjThVWGQtBz1c38Pw6Vby2"
        }


        adapter = MensagemAdapter(mensagens)
        rvMensagens.adapter = adapter
        rvMensagens.layoutManager = LinearLayoutManager(this)

        btnEnviar.setOnClickListener { enviarMensagem() }

        // Obter mensagens do Firestore
        obterMensagens()
    }

    private fun obterMensagens() {
        val tipoUsuario = intent.getStringExtra("tipoUsuario")  ?: "cliente"
        if (tipoUsuario == "dono") {
            db.collection("chats")
                .document("${codigoCondominio}_${iddapessoa}_${usuarioId}")
                .collection("mensagens")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        Log.w("Chat", "Erro ao obter mensagens", exception)
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        mensagens.clear()
                        for (document in snapshot) {
                            val mensagem = document.toObject(Mensagem::class.java)
                            mensagens.add(mensagem)
                        }
                        adapter.notifyDataSetChanged()
                        rvMensagens.scrollToPosition(mensagens.size - 1)
                    }
                }
        }else if (tipoUsuario == "cliente"){
            db.collection("chats")
                .document("${codigoCondominio}_${usuarioId}_${iddapessoa}")
                .collection("mensagens")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        Log.w("Chat", "Erro ao obter mensagens", exception)
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        mensagens.clear()
                        for (document in snapshot) {
                            val mensagem = document.toObject(Mensagem::class.java)
                            mensagens.add(mensagem)
                        }
                        adapter.notifyDataSetChanged()
                        rvMensagens.scrollToPosition(mensagens.size - 1)
                    }
                }
        }else{

        }
    }

    private fun enviarMensagem() {
        val tipoUsuario = intent.getStringExtra("tipoUsuario")  ?: "cliente"
        val conteudo = etMensagem.text.toString()
        if (conteudo.isNotBlank()) {
            val mensagem = Mensagem(
                id = db.collection("chats").document().id,
                remetenteId = usuarioId,
                conteudo = conteudo,
                timestamp = Date(System.currentTimeMillis()) // Converte Long para Date
            )
            if (tipoUsuario == "dono") {
                db.collection("chats").document("${codigoCondominio}_${iddapessoa}_${usuarioId}")
                    .collection("mensagens")
                    .document(mensagem.id)
                    .set(mensagem)
                    .addOnSuccessListener {
                        etMensagem.text.clear()
                    }
                    .addOnFailureListener { e ->
                        Log.w("Chat", "Erro ao enviar mensagem", e)
                    }
            }
            else if (tipoUsuario == "cliente"){
                db.collection("chats").document("${codigoCondominio}_${usuarioId}_${iddapessoa}")
                    .collection("mensagens")
                    .document(mensagem.id)
                    .set(mensagem)
                    .addOnSuccessListener {
                        etMensagem.text.clear()
                    }
                    .addOnFailureListener { e ->
                        Log.w("Chat", "Erro ao enviar mensagem", e)
                    }
            }else{
                Log.w("cliente", "Erro ao o valor do cliente")
            }
        }
    }
}