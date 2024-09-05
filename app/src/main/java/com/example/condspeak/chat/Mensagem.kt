package com.example.condspeak.chat

import com.google.firebase.firestore.FieldValue

data class Mensagem(
    val remetente: String = "",
    val destinatario: String= "",
    val texto: String = "",
    val timestamp: FieldValue = FieldValue.serverTimestamp()
)