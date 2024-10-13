package com.example.condspeak.ui.chat.chatcondominio.model

import java.util.Date

data class Mensagem(
    val id: String = "",
    val remetenteId: String = "",
    val conteudo: String = "",
    val timestamp: Date = Date()
)