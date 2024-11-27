package com.example.condspeak.data.model

import java.util.Date

data class Aviso (
    val tipo: String,
    val titulo: String,
    val descricao: String,
    val data: Date,
    val imagem: String,
    val id: String,
    val datainicio: String,
    val datafim: String
)