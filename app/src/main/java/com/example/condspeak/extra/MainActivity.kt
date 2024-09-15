package com.example.condspeak.extra

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.condspeak.Cadastro.Codigo_Condominio
import com.example.condspeak.Cadastro.Tela_De_Login
import com.example.condspeak.R
import com.example.condspeak.selcionacond.Tela_Principal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var textView: TextView
    private val handler = Handler(Looper.getMainLooper())
    private var dots = 0
    private val loadingText = "Carregando"

    private val runnable = object : Runnable {
        override fun run() {
            textView.text = "$loadingText${".".repeat(dots)}"
            dots = (dots + 1) % 4
            handler.postDelayed(this, 500)
        }}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Handler(Looper.getMainLooper()).postDelayed({
            val auth = FirebaseAuth.getInstance()
            auth.signOut()
            val currentUser = auth.currentUser

            if (currentUser != null) {
                calmala(currentUser.uid)

            } else {
                val intent = Intent(this, Tela_De_Login::class.java)
                startActivity(intent)
            }
            finish()
        }, 5000)
        textView = findViewById(R.id.Carregando)
        handler.postDelayed(runnable, 500)
    }
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }
    private fun calmala(usuarioId: String) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("clientes").document(usuarioId)

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val codigos = document.get("codigo") as? List<String>
                    if (codigos == null ||codigos.isEmpty()) {
                        // A lista "codigo" é nula ou vazia
                        val intent = Intent(this, Codigo_Condominio::class.java)
                        startActivity(intent)
                    } else {
                       val intent = Intent(this, Tela_Principal::class.java)
                        startActivity(intent)
                    }
                } else {
                    // O documento não existe
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("TAG", "Erro ao ler documento", exception)
                // Tratar o erro, por exemplo, exibindo uma mensagem para o usuário
            }
    }
}