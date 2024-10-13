package com.example.condspeak.ui.Nav_drawer.reclamacao

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.condspeak.R
import com.example.condspeak.data.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Properties
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import javax.mail.PasswordAuthentication

class Reclamacao_Fragment : Fragment() {

    lateinit var titulo: EditText
    lateinit var descricao: EditText
    lateinit var mensagem: EditText
    lateinit var enviar: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reclamacao_, container, false)

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        titulo = view.findViewById(R.id.titulo)
        descricao = view.findViewById(R.id.objetivodareclamacao)
        mensagem = view.findViewById(R.id.mensagem)
        enviar = view.findViewById(R.id.botao)

        enviar.setOnClickListener {
            val tituloText = titulo.text.toString()
            val descricaoText = descricao.text.toString()
            val mensagemText = mensagem.text.toString()

            val db = Firebase.firestore
            val usuarioId = Firebase.auth.currentUser!!.uid

            val docRef = db.collection("Users").document(usuarioId)
            docRef.get().addOnSuccessListener { documentSnapshot ->
                val user = documentSnapshot.toObject(User::class.java)
                if (user != null) {
                    sendEmail(tituloText, descricaoText, mensagemText, user)
                } else {
                    Log.d("TAG", "Usuário não encontrado")
                }
            }.addOnFailureListener { exception ->
                Log.d("TAG", "Erro ao obter o documento: ", exception)
            }
        }

        return view
    }

    private fun sendEmail(
        titulo: String,
        descricao: String,
        mensagem: String,
        user: User
    ) {
        val props = Properties().apply {
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")
            put("mail.smtp.host", "smtp.gmail.com")
            put("mail.smtp.port", "465")
        }

        val authenticator = MyAuthenticator()
        val session = Session.getInstance(props, authenticator)

        try {
            val message = MimeMessage(session).apply {
                setFrom(InternetAddress("sendemailvictorio@gmail.com")) // Substitua pelo seu email
                setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse("victoriopinto@gmail.com")
                ) // Destinatário
                subject = titulo
                setText(
                    "Descrição: $descricao\n\nMensagem: $mensagem\n\n" +
                            "Dados do Cliente:\n" +
                            "Nome: ${user.nome}\n" +
                            "Email: ${user.email}\n" +
                            "Telefone: ${user.telefone}\n" +
                            "CPF: ${user.cpf}\n" +
                            "Códigos: ${user.codigo.joinToString(", ")}\n" +
                            "Códigos do Dono: ${user.codigodono.joinToString(", ")}"
                )
            }
            Transport.send(message)
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
    }
}

class MyAuthenticator : javax.mail.Authenticator() {
    override fun getPasswordAuthentication(): PasswordAuthentication {
        return PasswordAuthentication("sendemailvictorio@gmail.com", "rjpe tinw cxem mrdw")
    }
}
