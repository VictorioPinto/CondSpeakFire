package com.example.condspeak.data.repository

import com.example.condspeak.data.model.Aviso
import com.example.condspeak.data.model.User
import com.example.condspeak.extra.ValorGlobal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AvisoRepository {
    private val auth: FirebaseAuth = Firebase.auth
    private val firestore: FirebaseFirestore = Firebase.firestore
    private val db = FirebaseFirestore.getInstance()
    private val codigoCondominio = ValorGlobal.CodigoCondominio
    private val usuarioId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    fun saveAviso(aviso: Aviso, onComplete: (Boolean, String?) -> Unit) {
        val avisosCollection = firestore.collection("Avisos").document(codigoCondominio).collection("avisos") // Subcoleção para avisos
        avisosCollection.add(aviso) // Usa add() para criar um novo documento
            .addOnSuccessListener {
                onComplete(true, null)
            }
            .addOnFailureListener { e ->
                onComplete(false, e.message)
            }
    }

    fun getAvisos(onComplete: (List<Aviso>?, Exception?) -> Unit) {
        val avisosCollection = firestore.collection("Avisos").document(codigoCondominio).collection("avisos") // Subcoleção para avisos
        avisosCollection.get() // Usa get() para obter todos os documentos da subcoleção
            .addOnSuccessListener { querySnapshot ->
                val avisos = querySnapshot.documents.mapNotNull { it.toObject(Aviso::class.java) } // Converte os documentos em objetos Aviso
                onComplete(avisos, null)
            }
            .addOnFailureListener { exception ->
                onComplete(null, exception)
            }
    }
}