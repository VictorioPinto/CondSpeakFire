package com.example.condspeak.data.repository

import android.util.Log
import com.example.condspeak.extra.ValorGlobal
import com.example.condspeak.ui.chat.chatcondominio.model.Mensagem

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.util.Date

class ChatRepository {
    private val db = FirebaseFirestore.getInstance()
    private val codigoCondominio = ValorGlobal.CodigoCondominio
    private val usuarioId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    suspend fun obterMensagens(): List<Mensagem> {
        return try {
            val snapshot = db.collection("chats")
                .document(codigoCondominio)
                .collection("mensagens")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .get()
                .await()
            snapshot.toObjects(Mensagem::class.java)
        } catch (e: Exception) {
            Log.e("ChatRepository", "Erro ao obter mensagens", e)
            emptyList()
        }
    }

    suspend fun enviarMensagem(conteudo: String) {
        if (conteudo.isNotBlank()) {
            val mensagem = Mensagem(
                id = db.collection("chats").document().id,
                remetenteId = usuarioId,
                conteudo = conteudo,
                timestamp = Date(System.currentTimeMillis())
            )
            try {
                db.collection("chats").document(codigoCondominio)
                    .collection("mensagens")
                    .document(mensagem.id)
                    .set(mensagem)
                    .await()
            } catch (e: Exception) {
                Log.e("ChatRepository", "Erro ao enviar mensagem", e)
            }
        }
    }

    suspend fun obterMensagensa(codigoCondominio: String, iddapessoa: String, tipoUsuario: String): List<Mensagem> {
        return try {
            val path = if (tipoUsuario == "dono") {
                "${codigoCondominio}_${iddapessoa}_${usuarioId}"
            } else {
                "${codigoCondominio}_${usuarioId}_${iddapessoa}"
            }

            val snapshot = db.collection("chats")
                .document(path)
                .collection("mensagens")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .get()
                .await()
            snapshot.toObjects(Mensagem::class.java)
        } catch (e: Exception) {
            Log.e("ChatRepository", "Erro ao obter mensagens", e)
            emptyList()
        }
    }

    suspend fun enviarMensagema(codigoCondominio: String, iddapessoa: String, tipoUsuario: String, conteudo: String) {
        if (conteudo.isNotBlank()) {
            val mensagem = Mensagem(
                id = db.collection("chats").document().id,
                remetenteId = usuarioId,
                conteudo = conteudo,
                timestamp = Date(System.currentTimeMillis())
            )

            val path = if (tipoUsuario == "dono") {
                "${codigoCondominio}_${iddapessoa}_${usuarioId}"
            } else {
                "${codigoCondominio}_${usuarioId}_${iddapessoa}"
            }

            try {
                db.collection("chats")
                    .document(path)
                    .collection("mensagens")
                    .document(mensagem.id)
                    .set(mensagem)
                    .await()
            } catch (e: Exception) {
                Log.e("ChatRepository", "Erro ao enviar mensagem", e)
            }
        }
    }

}
