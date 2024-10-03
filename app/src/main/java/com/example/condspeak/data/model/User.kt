package com.example.condspeak.data.model

data class User(
    val nome: String = "",
    val email: String = "",
    val senha: String = "",
    val telefone: String = "",
    val cpf: String = "",
    val codigo: List<String> = emptyList(),
    val codigodono: List<String> = emptyList()
)
