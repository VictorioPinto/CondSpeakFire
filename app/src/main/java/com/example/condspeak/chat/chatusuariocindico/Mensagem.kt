package com.example.condspeak.chat.chatusuariocindico

import java.util.Date

data class Mensagem(
    var id: String = "",
    var remetenteId: String = "",
    var conteudo: String = "",
    var timestamp: Date = Date()
)