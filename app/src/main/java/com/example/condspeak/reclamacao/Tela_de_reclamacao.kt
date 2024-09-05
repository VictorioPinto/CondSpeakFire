package com.example.condspeak.reclamacao

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.condspeak.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import java.util.Properties
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import javax.mail.PasswordAuthentication


class Tela_de_reclamacao : AppCompatActivity() {
    lateinit var titulo: EditText
    lateinit var descricao: EditText
    lateinit var mensagem: EditText
    lateinit var enviar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela_de_reclemacao)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        titulo = findViewById(R.id.titulo)
        descricao = findViewById(R.id.objetivodareclamacao)
        mensagem = findViewById(R.id.mensagem)
        enviar = findViewById(R.id.botao)
        enviar.setOnClickListener {

            enviar.setOnClickListener {
                val titulo = titulo.text.toString()
                val descricao = descricao.text.toString()
                val mensagem = mensagem.text.toString()

                // Obter dados do cliente e enviar email
                val db = Firebase.firestore
                val usuarioId = Firebase.auth.currentUser!!.uid
                val docRef = db.collection("clientes").document(usuarioId)

                docRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            val nome = document.getString("nome")
                            val email = document.getString("email")
                            val telefone = document.getString("telefone")
                            val cpf = document.getString("cpf")
                            val codigo = document.get("codigo") as? List<String>
                            val codigodono = document.get("codigodono") as? List<String>

                            send_email(titulo, descricao, mensagem, nome, email, telefone, cpf, codigo, codigodono)
                        } else {
                            Log.d("TAG", "No such document")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d("TAG", "get failed with ", exception)
                    }
            }
        }
    }
        private fun send_email(
            titulo: String,
            descricao: String,
            mensagem: String,
            nome: String?,
            email: String?,
            telefone: String?,
            cpf: String?,
            codigo: List<String>?,
            codigodono: List<String>?
        ) {
            val props = Properties()
            props["mail.smtp.auth"] = "true"
            props["mail.smtp.starttls.enable"] = "true"
            props["mail.smtp.host"] = "smtp.gmail.com" 
            props["mail.smtp.port"] = "465" 

            val authenticator: javax.mail.Authenticator = MyAuthenticator()
            val session = Session.getInstance(props, authenticator)
            try {
                val message = MimeMessage(session)
                message.setFrom(InternetAddress("sendemailvictorio@gmail.com")) // Substitua pelo seu email
                message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse("victoriopinto@gmail.com")
                ) 
                message.subject = titulo
                message.setText(
                    "Descrição: $descricao\n\nMensagem: $mensagem\n\n" +
                            "Dados do Cliente:\n" +
                            "Nome: $nome\n" +
                            "Email: $email\n" +
                            "Telefone: $telefone\n" +
                            "CPF: $cpf\n" +
                            "Códigos: ${codigo?.joinToString(", ")}\n" +
                            "Códigos do Dono: ${codigodono?.joinToString(", ")}"
                )

                Transport.send(message)
                // Email enviado com sucesso
            } catch (e: MessagingException) {
                e.printStackTrace()
                // Erro ao enviar email
            }
        }
    }
class MyAuthenticator : javax.mail.Authenticator() {
    override fun getPasswordAuthentication(): javax.mail.PasswordAuthentication {
        return PasswordAuthentication("sendemailvictorio@gmail.com", String("rjpe tinw cxem mrdw".toCharArray()))
    }}
