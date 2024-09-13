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
import com.example.condspeak.chat.chatusuariocindico.Chat_usuario_sindico

class Escolha_Chat : AppCompatActivity() {
    lateinit var btn1 : Button
    lateinit var btn2 : Button
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
        val CodigoCondominio = intent.getStringExtra("CodigoCondominio")
        val tipoUsuario = intent.getStringExtra("tipoUsuario")
        Log.d("Escolha_Chat", "Código do condomínio: $CodigoCondominio $tipoUsuario")
//        if (tipoUsuario == "dono") {
//            btn1.text = "Chat com os moradores "
//            btn1.setOnClickListener {
//                val intent = Intent(this, Tela_Principal::class.java)
//                intent.putExtra("CodigoCondominio", CodigoCondominio)
//                intent.putExtra("tipoUsuario", tipoUsuario)
//                startActivity(intent)
//            }
//        } else if (tipoUsuario == "cliente") {
//            btn1.text = "Chat com o sindico "
//            btn1.setOnClickListener {
//                val intent = Intent(this, Chat_usuario_dono::class.java)
//                intent.putExtra("CodigoCondominio", CodigoCondominio)
//                intent.putExtra("tipoUsuario", tipoUsuario)
//                startActivity(intent)
//        }}
        btn2.text = "chat do condominio"
        btn2.setOnClickListener {
                val intent = Intent(this, Chat_usuario_sindico::class.java)
                intent.putExtra("CodigoCondominio", CodigoCondominio)
                intent.putExtra("tipoUsuario", tipoUsuario)
                startActivity(intent)
        }
    }
}
