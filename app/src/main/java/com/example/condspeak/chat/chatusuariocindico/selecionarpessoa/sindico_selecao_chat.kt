package com.example.condspeak.chat.chatusuariocindico.selecionarpessoa

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.get
import androidx.recyclerview.widget.RecyclerView
import com.example.condspeak.R
import com.example.condspeak.chat.chatusuariocindico.codigochat.Chat_usuario_sindico
import com.example.condspeak.extra.ValorGlobal
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.io.path.exists

class sindico_selecao_chat : AppCompatActivity() {
    private lateinit var codigoCondominio: String
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sindico_selecao_chat)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        recyclerView = findViewById(R.id.Recycleview)

        codigoCondominio = ValorGlobal.Codigo_Condominio
        buscarClientes(codigoCondominio)
    }

    private fun buscarClientes(codigoCondominio: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("clientescondominio")
            .document(codigoCondominio)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val idDosClientes = document.get("iddosclientes") as? List<String> ?: listOf()
                    buscarDadosClientes(idDosClientes)
                }
            }
            .addOnFailureListener { exception ->

            }
    }

    private fun buscarDadosClientes(idsClientes: List<String>) {
        val db = FirebaseFirestore.getInstance()
        val pessoas = mutableListOf<pessoa>()
        for (id in idsClientes) {
            db.collection("clientes")
                .document(id)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val nome = document.getString("nome") ?: ""
                        val codigo = document.id
                        pessoas.add(pessoa(nome, codigo))
                        atualizarRecyclerView(pessoas)
                    }
                }
                .addOnFailureListener { exception ->
                }
        }
    }

    private fun atualizarRecyclerView(pessoas: List<pessoa>) {
        val adapter = PessoaAdapter(pessoas) { pessoa ->
            val intent = Intent(this, Chat_usuario_sindico::class.java)
            intent.putExtra("idCliente", pessoa.codigo)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }
}