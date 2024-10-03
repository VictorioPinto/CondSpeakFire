package com.example.condspeak.extra

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.condspeak.Avisos.CriarAviso.Criar_avisos
import com.example.condspeak.R
import com.example.condspeak.chat.Escolha_Chat
import com.example.condspeak.listademembros.Lista_de_Membros
import com.example.condspeak.selcionacond.Tela_Principal
import com.example.condspeak.ui.perfil_user.Perfil_user

class Telaquaseprincipal : AppCompatActivity() {

    private lateinit var criarAvisoButton: Button
    private lateinit var escolherChatButton: Button
    private lateinit var listaDeMembrosButton: Button
    private lateinit var atualizarPerfilButton: Button
    private lateinit var selecionaCondominioButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_telaquaseprincipal)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        criarAvisoButton = findViewById(R.id.criaraviso)
        escolherChatButton = findViewById(R.id.escolherchat)
        listaDeMembrosButton = findViewById(R.id.lista_de_membros)
        atualizarPerfilButton = findViewById(R.id.atualizarperfil)
        selecionaCondominioButton = findViewById(R.id.seleciona_condominio)

        criarAvisoButton.setOnClickListener {
            startActivity(Intent(this, Criar_avisos::class.java))
        }

        escolherChatButton.setOnClickListener {
            startActivity(Intent(this, Escolha_Chat::class.java))
        }

        listaDeMembrosButton.setOnClickListener {
            startActivity(Intent(this, Lista_de_Membros::class.java))
        }

        atualizarPerfilButton.setOnClickListener {
            startActivity(Intent(this, Perfil_user::class.java))
        }

        selecionaCondominioButton.setOnClickListener {
            startActivity(Intent(this, Tela_Principal::class.java))
        }

        val CodigoCondominio = ValorGlobal.Codigo_Condominio
        val tipoUsuario = ValorGlobal.tipoUsuario
    }
}