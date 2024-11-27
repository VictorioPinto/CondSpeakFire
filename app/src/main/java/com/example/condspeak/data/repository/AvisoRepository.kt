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
        val docRef = firestore.collection("Avisos").document(codigoCondominio)
        docRef.set(aviso)
            .addOnSuccessListener {
                onComplete(true, null)
            }
            .addOnFailureListener { e ->
                onComplete(false, e.message)
            }
    }
}