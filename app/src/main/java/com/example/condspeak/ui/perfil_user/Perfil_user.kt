package com.example.condspeak.ui.perfil_user

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.launch
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import com.example.condspeak.R
import com.example.condspeak.data.model.User
import com.example.condspeak.ui.login.TelaDeLogin
import com.example.condspeak.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class Perfil_user : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var userViewModel: UserViewModel
    private lateinit var nome: TextView
    private lateinit var email: TextView
    private lateinit var cpf: TextView
    private lateinit var telefone: TextView
    private lateinit var btndeletar: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil_user)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        btndeletar = findViewById(R.id.btnDeletarUser)
        nome = findViewById(R.id.txtNomePerfil)
        email = findViewById(R.id.txtEmailPerfil)
        cpf = findViewById(R.id.txtCpfPerfil)
        telefone = findViewById(R.id.txtTelefonePerfil)
        auth = FirebaseAuth.getInstance()
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        val usuarioId = auth.currentUser?.uid ?: return
        userget(usuarioId)
        btndeletar.setOnClickListener {
            lifecycleScope.launch { // Launch a coroutine
                deletaConta(usuarioId)
            }
        }
    }
    private fun userget(userId: String) {
        userViewModel.getUserData(userId) { user: User? ->
            nome.text = user?.nome
            email.text = user?.email
            cpf.text = user?.cpf
            telefone.text = user?.telefone
        }
    }
    private suspend fun deletaConta(userId: String) {

        userViewModel.deletarConta(userId)
        startActivity(Intent(this, TelaDeLogin::class.java))
    }

}