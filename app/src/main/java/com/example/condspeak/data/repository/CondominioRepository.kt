package com.example.condspeak.data.repository


import com.example.condspeak.data.model.Condominio
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue


class CadastroRepository {
     val db = FirebaseFirestore.getInstance()
     val auth = FirebaseAuth.getInstance()

    fun cadastrarCondominio(condominio: Condominio): Task<Void> {
        val docRef = db.collection("condominios").document(condominio.codigo)
        return docRef.set(condominio)
    }

    fun criarCondominioComCliente(codigo: String): Task<Void> {
        val usuarioId = auth.currentUser?.uid ?: return Tasks.forException(NullPointerException("Usuário não autenticado"))
        val user = hashMapOf(
            "codigo" to codigo,
            "iddono" to usuarioId
        )
        val docRef = db.collection("UserCondominio").document(codigo)
        return docRef.set(user)
    }

    fun salvarNoDono(usuarioId: String, codigo: String): Task<Void> {
        val docRef = db.collection("Users").document(usuarioId)
        return docRef.update("codigodono", FieldValue.arrayUnion(codigo))
    }
}
