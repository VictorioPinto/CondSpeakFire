package com.example.condspeak.chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.condspeak.R
import com.example.condspeak.chat.chatcondominio.Chat_condominio
import com.example.condspeak.chat.chatusuariocindico.codigochat.Chat_usuario_sindico
import com.example.condspeak.chat.chatusuariocindico.selecionarpessoa.sindico_selecao_chat
import com.example.condspeak.extra.ValorGlobal
import com.google.firebase.firestore.FirebaseFirestore

class Escolha_Chat : AppCompatActivity() {
    lateinit var btn1 : Button
    lateinit var btn2 : Button
    private val db = FirebaseFirestore.getInstance()
    private val tipoUsuario = ValorGlobal.tipoUsuario
    private val CodigoCondominio = ValorGlobal.Codigo_Condominio
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_escolha_chat)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btn1 = findViewById(R.id.button2)
        btn2 = findViewById(R.id.button3)

        Log.d("Escolha_Chat", "Código do condomínio: $CodigoCondominio $tipoUsuario")
        if (tipoUsuario == "dono") {
            btn1.text = "Chat com os moradores "
            btn1.setOnClickListener {
                val intent = Intent(this, sindico_selecao_chat::class.java)
                startActivity(intent)
            }
        } else if (tipoUsuario == "cliente") {
            btn1.text = "Chat com o sindico "

            btn1.setOnClickListener {
                    db.collection("clientescondominio").document(CodigoCondominio).get().addOnSuccessListener {
                        val iddosindico = it.get("iddono") as String
                        Log.d("ID do Síndico", iddosindico)

                        val intent = Intent(this, Chat_usuario_sindico::class.java)
                        intent.putExtra("iddapessoa", iddosindico)
                        startActivity(intent)
                   }
        }}
        btn2.text = "chat do condominio"
        btn2.setOnClickListener {
                val intent = Intent(this, Chat_condominio::class.java)
                startActivity(intent)
        }
    }

}
