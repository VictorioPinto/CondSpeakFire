package com.example.condspeak.selcionacond

var condominiolist = mutableListOf<Condominio>()

class Condominio(nome:String, cep: String, codigo: String, tipo: String) { // Construtor com parâmetros
    var nome: String = nome
    var CEP: String = cep
    val codigo: String = codigo
    val tipo: String = tipo
}