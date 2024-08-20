package com.example.condspeak.Cadastro
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.condspeak.R
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth



class Tela_De_Login : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var senhaEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var criaConta: TextView
    private lateinit var btnForgotPass : TextView
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_de_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        criaConta = findViewById(R.id.txtCriaConta2)
        emailEditText = findViewById(R.id.edtLogin)
        senhaEditText = findViewById(R.id.edtCep)
        loginButton = findViewById(R.id.btnLogar)
        btnForgotPass = findViewById(R.id.txtSenha)
        auth = FirebaseAuth.getInstance()
        criaConta.setOnClickListener { chamaTelaCadastro() }
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val senha = senhaEditText.text.toString()

            if(email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Login bem-sucedido
                        val user = auth.currentUser
                        val userId = user?.uid ?: ""
                        Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show()
                        proximaTela(userId)
                    } else {
                        // Falha no login
                        Toast.makeText(this, "Erro ao fazer login: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        btnForgotPass.setOnClickListener {
            ForgotPass()
        }

    }
    private fun chamaTelaCadastro() {
        startActivity(Intent(this, Tela_de_cadastro::class.java))
    }
    private fun proximaTela(userId: String) {
        val intent = Intent(this, Codigo_Condominio::class.java)
        intent.putExtra("cliente_id", userId)
        startActivity(intent)
    }

    private fun ForgotPass()
    {
        if (!emailEditText.text.isEmpty())
        {
            FirebaseAuth.getInstance()
                .sendPasswordResetEmail(emailEditText.text.toString()).addOnFailureListener {
                    Toast.makeText(this@Tela_De_Login, "Email nao cadastrado", Toast.LENGTH_SHORT).show()
                }.addOnSuccessListener {
                    Toast.makeText(this, "Email enviado com sucesso", Toast.LENGTH_SHORT).show()
                }


        } else{
            Toast.makeText(this, "Email vazio", Toast.LENGTH_SHORT).show()
        }
    }

}