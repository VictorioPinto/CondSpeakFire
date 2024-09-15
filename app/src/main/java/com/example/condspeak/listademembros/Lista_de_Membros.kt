package com.example.condspeak.listademembros

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.condspeak.R
import com.example.condspeak.extra.ValorGlobal
import com.google.firebase.firestore.FirebaseFirestore

class Lista_de_Membros : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_de_menbros)
        var tipousuario = ValorGlobal.tipoUsuario
        var codigo = ValorGlobal.Codigo_Condominio
        seila(codigo)


    }
    fun seila(codigo: String): List<String>? {
        var codigos: List<String>? = null
        db.collection("clientescondominio").document(codigo).get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot != null && documentSnapshot.exists()) {
                codigos = documentSnapshot.get("codigo") as? List<String>
            }
        }
        return codigos // Retorna a lista de códigos ou null se não encontrar
    }

}