package com.example.condspeak.data.model

import com.google.firebase.Timestamp
import java.util.Date

data class Aviso (
    val tipo: String,
    val titulo: String,
    val descricao: String,
    val data: Timestamp = Timestamp.now(),
    val imagem: String,
    val valueSpinner: String,
    val datainicio: String,
    val datafim: String,
    val id: String,
){
    constructor() : this("", "", "", Timestamp.now(), "", "", "", "", "")
}