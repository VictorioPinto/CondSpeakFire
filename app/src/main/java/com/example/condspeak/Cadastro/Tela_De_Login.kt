package com.example.condspeak.Cadastro
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentProviderClient
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.condspeak.R
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.get
import com.example.condspeak.selcionacond.Tela_Principal
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.io.path.exists


class Tela_De_Login : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var senhaEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var criaConta: TextView
    private lateinit var btnForgotPass : TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignClient: GoogleSignInClient



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_de_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val google = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.gcm_defaultSenderId))
            .requestEmail()
            .build()

        googleSignClient = GoogleSignIn.getClient(this, google)



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
                        Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show()
                        proximaTela()
                    } else {
                        Toast.makeText(this, "Erro ao fazer login: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        btnForgotPass.setOnClickListener {
            ForgotPass()
        }

        var botaoGogle : Button = findViewById(R.id.btnGoogle)
        botaoGogle.setOnClickListener{
            LoginGoogle()
        }

    }


    private fun chamaTelaCadastro() {
        startActivity(Intent(this, Tela_de_cadastro::class.java))
    }
    private fun proximaTela() {
        val usuarioId = auth.currentUser?.uid ?: return

        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("clientes").document(usuarioId)

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val codigosCondominios = document.get("codigo") as? List<String> ?: emptyList()
                    val codigosCondominios2 = document.get("codigodono") as? List<String> ?: emptyList()

                    if (codigosCondominios.isNotEmpty()) {
                        val intent = Intent(this, Tela_Principal::class.java)
                        startActivity(intent)
                    }else if(codigosCondominios2.isNotEmpty()){
                        val intent = Intent(this, Tela_Principal::class.java)
                        startActivity(intent)
                    } else {
                        val intent = Intent(this, Codigo_Condominio::class.java)
                        startActivity(intent)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Erro ao verificar condomínios do usuário", exception)
            }
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

    private fun LoginGoogle(){
        val entrar = googleSignClient.signInIntent
        laucher.launch(entrar)


    }

    private val laucher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        try {
            if (result.resultCode == Activity.RESULT_OK)
            {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                VerResultado(task)
            }
        } catch (e: Exception) {
            Log.e("LoginError", "Erro durante o login com Google: ${e.message}")
            Toast.makeText(this, "Erro ao fazer login com o Google", Toast.LENGTH_SHORT).show()
        }
    }

    private fun VerResultado(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener {
                    if (task.isSuccessful)
                    {
                        proximaTela()
                    }
                }
            }
        } else {
            // Exceção durante o Google Sign-In (opcional)
            val exception = task.exception
            // ... (tratar a exceção)
        }
    }

}