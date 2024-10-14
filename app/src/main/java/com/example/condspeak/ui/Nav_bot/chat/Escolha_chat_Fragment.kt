
package com.example.condspeak.ui.Nav_bot.chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import androidx.fragment.app.Fragment
import com.example.condspeak.R


import com.example.condspeak.extra.ValorGlobal
import com.example.condspeak.ui.Nav_bot.chat.chatcondominio.Chat_condominio
import com.example.condspeak.ui.Nav_bot.chat.chatusuariocindico.codigochat.Chat_usuario_sindico
import com.google.firebase.firestore.FirebaseFirestore

class Escolha_chat_Fragment : Fragment() {
    lateinit var btn1: Button
    lateinit var btn2: Button
    private val db = FirebaseFirestore.getInstance()
    private val tipoUsuario = ValorGlobal.TipoUsuario
    private val CodigoCondominio = ValorGlobal.CodigoCondominio

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla o layout para este fragment
        val view = inflater.inflate(R.layout.fragment_escolha_chat, container, false)

        // Encontre as views no layout
        btn1 = view.findViewById(R.id.button2)
        btn2 = view.findViewById(R.id.button3)

        Log.d("Escolha_Chat", "Código do condomínio: $CodigoCondominio $tipoUsuario")

        // Ajuste do botão com base no tipo de usuário
        if (tipoUsuario == "dono") {
            btn1.text = "Chat privado com os moradores"
            btn1.setOnClickListener {
                val intent = Intent(context, com.example.condspeak.ui.Nav_bot.chat.chatusuariocindico.selecionarpessoa.sindico_selecao_chat::class.java)
                startActivity(intent)
            }
        } else if (tipoUsuario == "cliente") {
            btn1.text = "Chat com o sindico"
            btn1.setOnClickListener {
                db.collection("clientescondominio").document(CodigoCondominio).get().addOnSuccessListener {
                    val iddosindico = it.get("iddono") as String
                    Log.d("ID do Síndico", iddosindico)

                    val intent = Intent(context, Chat_usuario_sindico::class.java)
                    intent.putExtra("idCliente", iddosindico)
                    startActivity(intent)
                }
            }
        }

        // Configuração do segundo botão
        btn2.text = "chat do condominio"
        btn2.setOnClickListener {
            val intent = Intent(context, Chat_condominio::class.java)
            startActivity(intent)
        }

        return view
    }
}
