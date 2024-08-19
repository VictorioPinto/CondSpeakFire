package com.example.condspeak.selcionacond

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.condspeak.R
import com.example.condspeak.databinding.ActivityTelaPrincipalBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class Tela_Principal : AppCompatActivity(){
    private lateinit var binding: ActivityTelaPrincipalBinding
    private val condominioList = mutableListOf<Condominio>()
    private lateinit var firestoreListener: ListenerRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTelaPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.Recycleview.apply {
            layoutManager = LinearLayoutManager(this@Tela_Principal)
            adapter = CardAdapter(condominioList)
        }

        iniciarFirestoreListener()
    }

    private fun iniciarFirestoreListener() {
        val firebaseFirestore = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser?.uid

        if (user != null) {
            firestoreListener = firebaseFirestore.collection("clientes").document(user)
                .addSnapshotListener { documentSnapshot, error ->if (error != null) {
                    Log.e("Tela_Principal", "Erro ao carregar dados do Firestore", error)
                    return@addSnapshotListener
                }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        val codigos = documentSnapshot.get("codigo") as? List<String>
                        if (codigos != null) {
                            condominioList.clear()
                            carregarCondominios(codigos)
                        }
                    }
                }
        }
    }

    private fun carregarCondominios(codigos: List<String>) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("condominios")

        for (condId in codigos) {
            docRef.document(condId).get().addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val condominio = Condominio(
                        document.getString("nome").toString(),
                        document.getString("CEP").toString(),
                        document.getString("codigo").toString()
                    )
                    condominioList.add(condominio)
                    binding.Recycleview.adapter?.notifyItemInserted(condominioList.size - 1)
                } else {
                    Log.w("Tela_Principal", "Condomínio com ID $condId não encontrado")
                }
            }.addOnFailureListener { error ->
                Log.e("Tela_Principal", "Erro ao carregar condomínio com ID $condId", error)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        firestoreListener.remove()
    }
}