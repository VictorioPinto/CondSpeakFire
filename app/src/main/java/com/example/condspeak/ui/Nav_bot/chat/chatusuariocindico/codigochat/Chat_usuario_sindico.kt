package com.example.condspeak.ui.Nav_bot.chat.chatusuariocindico.codigochat

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.condspeak.R
import com.example.condspeak.extra.ValorGlobal
import com.example.condspeak.viewmodel.ChatViewModel

class Chat_usuario_sindico : AppCompatActivity() {

    private lateinit var rvMensagens: RecyclerView
    private lateinit var etMensagem: EditText
    private lateinit var btnEnviar: Button
    private lateinit var adapter: MensagemAdapter

    private val viewModel: ChatViewModel by viewModels()

    private lateinit var codigoCondominio: String
    private lateinit var iddapessoa: String
    private lateinit var tipoUsuario: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_usuario_sindico)

        rvMensagens = findViewById(R.id.rvMensagens)
        etMensagem = findViewById(R.id.etMensagem)
        btnEnviar = findViewById(R.id.btnEnviar)

        adapter = MensagemAdapter(mutableListOf())
        rvMensagens.adapter = adapter
        rvMensagens.layoutManager = LinearLayoutManager(this)

        codigoCondominio = ValorGlobal.CodigoCondominio
        iddapessoa = intent.getStringExtra("idCliente") ?: ""
        tipoUsuario = ValorGlobal.TipoUsuario

        btnEnviar.setOnClickListener {
            val conteudo = etMensagem.text.toString()
            viewModel.enviarMensagema(codigoCondominio, iddapessoa, tipoUsuario, conteudo)
        }

        observarMensagens()
        viewModel.obterMensagensa(codigoCondominio, iddapessoa, tipoUsuario)
    }

    private fun observarMensagens() {
        viewModel.mensagens.observe(this, Observer { mensagens ->
            adapter.updateMensagens(mensagens)
            rvMensagens.scrollToPosition(mensagens.size - 1)
        })
    }
}