package com.example.condspeak.ui.Nav_bot.chat.chatcondominio

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.condspeak.R

import com.example.condspeak.extra.ValorGlobal
import com.example.condspeak.ui.Nav_bot.chat.chatusuariocindico.codigochat.MensagemAdapter
import com.example.condspeak.ui.chat.chatcondominio.model.Mensagem
import com.example.condspeak.viewmodel.ChatViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.Date

class Chat_condominio : AppCompatActivity() {
    private lateinit var rvMensagens: RecyclerView
    private lateinit var etMensagem: EditText
    private lateinit var btnEnviar: Button
    private lateinit var adapter: MensagemAdapter

    private val chatViewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_condominio)

        rvMensagens = findViewById(R.id.rvMensagens)
        etMensagem = findViewById(R.id.etMensagem)
        btnEnviar = findViewById(R.id.btnEnviar)

        adapter = MensagemAdapter(mutableListOf())
        rvMensagens.adapter = adapter
        rvMensagens.layoutManager = LinearLayoutManager(this)

        btnEnviar.setOnClickListener {
            chatViewModel.enviarMensagem(etMensagem.text.toString())
        }

        observarMensagens()
        chatViewModel.obterMensagens()
    }

    private fun observarMensagens() {
        chatViewModel.mensagens.observe(this, Observer { mensagens ->
            adapter.updateMensagens(mensagens)
            rvMensagens.scrollToPosition(mensagens.size - 1)
        })
    }
}