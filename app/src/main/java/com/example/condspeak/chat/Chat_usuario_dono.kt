package com.example.condspeak.chat

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.condspeak.R
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore



class Chat_usuario_dono : AppCompatActivity() {
    private lateinit var donoId: String
    private lateinit var mensagensRecyclerView: RecyclerView
    private lateinit var mensagemEditText: EditText
    private lateinit var enviarButton: Button
    private lateinit var mensagensAdapter: MensagensAdapter

    private val usuarioLogadoId = com.google.firebase.Firebase.auth.currentUser?.uid ?: ""
    private var conversaId = ""
    private val mensagens = mutableListOf<Mensagem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat_usuario_dono)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mensagensRecyclerView = findViewById(R.id.mensagensRecyclerView)
        mensagemEditText = findViewById(R.id.mensagemEditText)
        enviarButton = findViewById(R.id.enviarButton)

        mensagensAdapter = MensagensAdapter(mensagens)
        mensagensRecyclerView.adapter = mensagensAdapter
        mensagensRecyclerView.layoutManager = LinearLayoutManager(this)

        val codigoCondominio = intent.getStringExtra("CodigoCondominio") ?: ""
        val tipoUsuario = intent.getStringExtra("tipoUsuario") ?: ""

        if (codigoCondominio.isNotEmpty()) {
            encontrarDonoCondominio(codigoCondominio)
        }

        enviarButton.setOnClickListener {
            val texto = mensagemEditText.text.toString()
            if (texto.isNotEmpty() && conversaId.isNotEmpty() && ::donoId.isInitialized) {
                enviarMensagem(conversaId, usuarioLogadoId, donoId, texto) // Passar donoId aqui
                mensagemEditText.text.clear()
            }
        }
    }

    private fun encontrarDonoCondominio(codigoCondominio: String) {
        val db = com.google.firebase.Firebase.firestore
        val condominioRef = db.collection("clientescondominio").document(codigoCondominio)
        condominioRef.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                donoId = document.getString("iddono") ?: ""
                if (donoId != null) {
                    encontrarConversa(usuarioLogadoId, donoId)
                }
            }
        }
    }

    private fun encontrarConversa(usuario1Id: String, usuario2Id: String) {
        val db = com.google.firebase.Firebase.firestore
        val conversasRef = db.collection("conversas")
        conversasRef
            .whereArrayContains("participantes", usuario1Id)
            .whereArrayContains("participantes", usuario2Id)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    criarConversa(usuario1Id, usuario2Id)
                } else {
                    conversaId = documents.documents[0].id
                    escutarMensagens(conversaId)
                }
            }
    }

    private fun criarConversa(usuario1Id: String, usuario2Id: String) {
        val db = com.google.firebase.Firebase.firestore
        val conversasRef = db.collection("conversas")
        val participantes = listOf(usuario1Id, usuario2Id)
        val conversa = hashMapOf("participantes" to participantes)
        conversasRef.add(conversa).addOnSuccessListener { documentReference ->
            conversaId = documentReference.id
            escutarMensagens(conversaId)
        }
    }

    private fun enviarMensagem(conversaId: String, remetenteId: String, destinatarioId: String, texto: String) {
        val db = com.google.firebase.Firebase.firestore
        val mensagensRef = db.collection("conversas").document(conversaId).collection("mensagens")
        val mensagem = hashMapOf(
            "remetente" to remetenteId,
            "destinatario" to destinatarioId,
            "texto" to texto,
            "timestamp" to FieldValue.serverTimestamp()
        )
        mensagensRef.add(mensagem).addOnSuccessListener {
            // Mensagem enviada com sucesso
        }
    }

    private fun escutarMensagens(conversaId: String) {
        val db = com.google.firebase.Firebase.firestore
        val mensagensRef = db.collection("conversas").document(conversaId).collection("mensagens")
        mensagensRef.orderBy("timestamp").addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("Chat", "Erro ao escutar mensagens", e)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                for (dc in snapshot.documentChanges) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        val mensagem = dc.document.toObject(Mensagem::class.java)
                        mensagens.add(mensagem)
                        mensagensAdapter.notifyItemInserted(mensagens.size - 1)
                        mensagensRecyclerView.smoothScrollToPosition(mensagens.size - 1)
                    }
                }
            }
        }}}
