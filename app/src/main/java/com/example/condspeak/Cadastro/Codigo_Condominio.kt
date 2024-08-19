package com.example.condspeak.Cadastro

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
import com.example.condspeak.selcionacond.Tela_Principal
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source

class Codigo_Condominio : AppCompatActivity() {
    // Declaração das variáveis que representam os elementos da tela
    lateinit var code: EditText // Campo para inserir o código do condomínio
    lateinit var entrar: Button // Botão para entrar no condomínio
    lateinit var cadastrarnocondominio: TextView // Texto para ir para a tela de cadastro decondomínio
    lateinit var btnteste : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configura a atividade para exibir conteúdo até as bordas da tela
        enableEdgeToEdge()
        // Define o layout XML da atividade
        setContentView(R.layout.activity_codigo_condominio)

        // Ajusta o padding da tela para acomodar as barras do sistema (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Associa as variáveis aos elementos da tela usando seus IDs
        cadastrarnocondominio = findViewById(R.id.txtCadCond)
        code = findViewById(R.id.edtCode)
        entrar = findViewById(R.id.btnentrar)
        btnteste = findViewById(R.id.button)
        btnteste.setOnClickListener {
            val intent = Intent(this, Tela_Principal::class.java)
            startActivity(intent)
        }
        // Define o que acontece quando o TextView "cadastrarnocondominio" é clicado
        cadastrarnocondominio.setOnClickListener {
            // Chama a função para abrir a tela de cadastro de condomínio
            teladecadastrocondominio()
        }

        // Define o que acontece quando o botão "entrar" é clicado
        entrar.setOnClickListener {
            // Chama a função para tentar entrar no condomínio
            entra()
        }
    }

    // Função para abrir a tela de cadastro de condomínio
    private fun teladecadastrocondominio() {
        startActivity(Intent(this, Cadastro_Condominio::class.java))
    }

    // Função para tentar entrar no condomínio
    private fun entra() {
        // Obtém o código digitado pelo usuário
        val codigo = code.text.toString()
        // Verifica se o campo de código está vazio
        if (codigo.isEmpty()) {
            code.error = "Preencha o código" // Exibe uma mensagem de erro
            return // Sai da função
        }

        // Cria uma referência ao documento do condomínio no Firestore
        val docRef = FirebaseFirestore.getInstance().collection("condominios").document(codigo)
        // Tenta obter o documento do condomínio
        docRef.get(Source.SERVER)
            .addOnSuccessListener { document ->
                // Se o documento existir e não for nulo
                if (document != null && document.exists()) {
                    // Chama a função para criar o documento do cliente no condomínio
                    criarcondominiocomcliente(codigo)
                } else {
                    // Se o documento não existir, exibe uma mensagem de erro
                    code.error = "Código inválido"
                }
            }
            // Trata erros que podem ocorrer durante a busca do documento
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting document", exception)
                // Poderia exibir uma mensagem de erro genérica para o usuário aqui
            }
    }

    // Função para criar o documento do cliente no condomínio
    private fun criarcondominiocomcliente(codigo: String) {
        // Obtém o ID do usuário logado
        val usuarioId = Firebase.auth.currentUser!!.uid
        // Obtém uma instância do Firestore
        val db = FirebaseFirestore.getInstance()
        // Cria uma referência ao documento que armazena a lista de clientes do condomínioval
        val docRef = db.collection("clientescondominio").document(codigo)

        // Tenta obter o documento
        docRef.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                // Se o documento existir, obtém a lista de IDs de clientes
                val idDosClientes = document.get("iddosclientes") as? MutableList<String> ?: mutableListOf()
                // Adiciona o ID do usuário à lista
                idDosClientes.add(usuarioId)
                // Atualiza o documento no Firestore com a nova lista
                docRef.update("iddosclientes", idDosClientes) // Corrigido: usar idDosClientes
                    .addOnSuccessListener {
                        Log.d("TAG", "Usuário adicionado ao documento com sucesso!")
                        verificarlogar(usuarioId, codigo)
                    }
                    .addOnFailureListener { e ->
                        Log.w("TAG", "Erro ao adicionar usuário ao documento", e)
                    }
            } else {
                // TODO: Tratar caso o documento não exista (criar um novo documento)
            }
        }.addOnFailureListener { e ->
            Log.w("TAG","Erro ao obter documento", e)
        }
    }

    private fun verificarlogar(usuarioId: String, codigo: String) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("clientes").document(usuarioId)
        docRef.update("codigo", FieldValue.arrayUnion(codigo))
            .addOnSuccessListener {
                Log.d(TAG, "Itens adicionados à lista de codominios!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Erro ao adicionar itens à lista de codominios", e)
            }
    }
}