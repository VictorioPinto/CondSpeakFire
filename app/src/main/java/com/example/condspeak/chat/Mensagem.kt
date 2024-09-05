package com.example.condspeak.chat

import com.google.firebase.Timestamp

data class Mensagem(
    val remetente: String = "",
    val destinatario: String = "",
    val texto: String = "",
    val timestamp: Timestamp? = null
)