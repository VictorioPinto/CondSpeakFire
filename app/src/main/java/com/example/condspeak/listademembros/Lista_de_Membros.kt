package com.example.condspeak.listademembros

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.condspeak.R
import com.example.condspeak.extra.ValorGlobal
import com.google.firebase.firestore.FirebaseFirestore

class Lista_de_Membros : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_de_menbros)
        var codigo = ValorGlobal.Codigo_Condominio
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        seila(codigo)


    }
    fun seila(codigo: String) {
        val membros = mutableListOf<Model_Membros>()
        db.collection("clientescondominio").document(codigo).get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot != null && documentSnapshot.exists()) {
                val codigos = documentSnapshot.get("iddosclientes") as? List<String> ?: emptyList()
                val codigodono = documentSnapshot.get("iddono") as? String ?: ""
                Log.d("TAG", "Códigos encontrados: $codigos")
                Log.d("TAG", "Código do dono: $codigodono")


                db.collection("clientes").document(codigodono).get().addOnSuccessListener { sindicoDocument ->
                    if (sindicoDocument != null && sindicoDocument.exists()) {
                        val nomeSindico = sindicoDocument.getString("nome") ?: ""
                        val emailSindico = sindicoDocument.getString("email") ?: ""
                        membros.add(Model_Membros(codigodono, nomeSindico, emailSindico, "Dono"))
                    }

                    var consultasConcluidas = 0
                    val totalConsultas = codigos.size

                    for (id in codigos) {
                        db.collection("clientes").document(id).get().addOnSuccessListener { document ->
                            if (document != null && document.exists()) {
                                val nome = document.getString("nome") ?: ""
                                val email = document.getString("email") ?: ""
                                val tipo = if (id == codigodono) "Dono" else "Morador"
                                Log.d("TAG", "Nome: $nome, Email: $email, Tipo: $tipo")
                                membros.add(Model_Membros(id, nome, email, tipo))
                            }

                            consultasConcluidas++
                            if (consultasConcluidas == totalConsultas) {
                                val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
                                val adapter = ListaMembrosAdapter(membros)
                                recyclerView.adapter = adapter
                            }
                        }
                    }
                }

            } else {
                Log.d("TAG", "Documento não encontrado ou não existe")
            }
        }
    }

}