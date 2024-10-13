package com.example.condspeak.data.repository


import SelecionaCondominioModel
import android.util.Log
import com.example.condspeak.data.model.Condominio
import com.example.condspeak.data.model.Model_Membros
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await


class CondominioRepository {
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
    fun iniciarFirestoreListener(onDataChange: (List<SelecionaCondominioModel>) -> Unit): ListenerRegistration {
        val user = auth.currentUser?.uid
        val condominioList = mutableListOf<SelecionaCondominioModel>()

        return if (user != null) {
            db.collection("Users").document(user)
                .addSnapshotListener { documentSnapshot, error ->
                    if (error != null) {
                        Log.e("CondominioRepository", "Erro ao carregar dados do Firestore", error)
                        return@addSnapshotListener
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        val codigos = documentSnapshot.get("codigo") as? List<String>
                        val codigosDono = documentSnapshot.get("codigodono") as? List<String>

                        if (codigos != null) {
                            carregarCondominios(codigos, "cliente", condominioList, onDataChange)
                        }

                        if (codigosDono != null) {
                            carregarCondominios(codigosDono, "dono", condominioList, onDataChange)
                        }
                    }
                }
        } else {
            ListenerRegistration { }
        }
    }

    private fun carregarCondominios(
        codigos: List<String>,
        tipo: String,
        condominioList: MutableList<SelecionaCondominioModel>,
        onDataChange: (List<SelecionaCondominioModel>) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("condominios")

        for (condId in codigos) {
            docRef.document(condId).get().addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val condominio = SelecionaCondominioModel(
                        document.getString("nome").toString(),
                        document.getString("CEP").toString(),
                        document.getString("codigo").toString(),
                        tipo
                    )

                    if (!condominioList.contains(condominio)) {
                        condominioList.add(condominio)
                        onDataChange(condominioList)
                    }
                }
            }.addOnFailureListener { error ->
                Log.e("CondominioRepository", "Erro ao carregar condomínio com ID $condId", error)
            }
        }
    }
    suspend fun obterMembros(codigoCondominio: String): List<Model_Membros> {
        val membros = mutableListOf<Model_Membros>()

        val documento = db.collection("UserCondominio").document(codigoCondominio).get().await()
        if (documento.exists()) {
            val codigos = documento.get("iddosclientes") as? List<String> ?: emptyList()
            val codigodono = documento.getString("iddono") ?: ""

            val sindicoDocumento = db.collection("Users").document(codigodono).get().await()
            if (sindicoDocumento.exists()) {
                val nomeSindico = sindicoDocumento.getString("nome") ?: ""
                val emailSindico = sindicoDocumento.getString("email") ?: ""
                membros.add(Model_Membros(codigodono, nomeSindico, emailSindico, "Dono"))
            }

            for (id in codigos) {
                val documentoMembro = db.collection("Users").document(id).get().await()
                if (documentoMembro.exists()) {
                    val nome = documentoMembro.getString("nome") ?: ""
                    val email = documentoMembro.getString("email") ?: ""
                    val tipo = if (id == codigodono) "Dono" else "Morador"
                    membros.add(Model_Membros(id, nome, email, tipo))
                }
            }
        }

        return membros
    }
}
