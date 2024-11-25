package com.example.condspeak.ui.Registro_Login.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.condspeak.ui.Registro_Login.codigo_condominio.Codigo_Condominio
import com.example.condspeak.R
import com.example.condspeak.data.model.User
import com.example.condspeak.databinding.ActivityTelaDeLoginBinding
import com.example.condspeak.ui.Registro_Login.register.Tela_de_cadastro
import com.example.condspeak.ui.Nav_drawer.selecionar_condominio.SelecionaCondominio
import com.example.condspeak.viewmodel.UserViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class TelaDeLogin : AppCompatActivity() {

    private lateinit var binding: ActivityTelaDeLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignClient: GoogleSignInClient
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTelaDeLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        // Define os ouvintes de clique
        binding.txtCriaConta2.setOnClickListener { chamaTelaCadastro() }
        binding.btnLogar.setOnClickListener { fazLoginComEmailESenha() }
        binding.txtSenha.setOnClickListener { esqueceuSenha() }

    }

    private fun fazLoginComEmailESenha() {
        val email = binding.edtLogin.text.toString()
        val senha = binding.edtCep.text.toString()

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show()
                    navigateToNextScreen()
                } else {
                    Toast.makeText(this, "Erro ao fazer login: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun esqueceuSenha() {
        val email = binding.edtLogin.text.toString()
        if (email.isNotEmpty()) {
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Email enviado com sucesso", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Erro: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Email vazio", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loginGoogle() {
        val signInIntent = googleSignClient.signInIntent
        resultLauncher.launch(signInIntent)
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success")
                    navigateToNextScreen()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun chamaTelaCadastro() {
        startActivity(Intent(this, Tela_de_cadastro::class.java))
    }
    private fun navigateToNextScreen() {
        val usuarioId = auth.currentUser?.uid ?: return

        userViewModel.getUserData(usuarioId) { user: User? ->
            Log.d("TelaDeLogin", "Usuário recuperado: $user")
            if (user == null || (user.codigo.isNullOrEmpty() && user.codigodono.isNullOrEmpty())) {
                startActivity(Intent(this, Codigo_Condominio::class.java))
            } else {
                startActivity(Intent(this, SelecionaCondominio::class.java))
            }
        }
    }
}
