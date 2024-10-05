package com.example.condspeak.ui.selecionar_condominio

import SelecionaCondominioModel
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.condspeak.R
import com.example.condspeak.databinding.ActivityTelaPrincipalBinding
import com.example.condspeak.viewmodel.CondominioViewModel

class SelecionaCondominio : AppCompatActivity() {
    private lateinit var binding: ActivityTelaPrincipalBinding
    private lateinit var viewModel: CondominioViewModel
    private val cardAdapter = CardAdapter(mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTelaPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Gerenciamento de insets de tela
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configuração do RecyclerView
        binding.Recycleview.apply {
            layoutManager = LinearLayoutManager(this@SelecionaCondominio)
            adapter = cardAdapter
        }

        // Configurando o ViewModel
        viewModel = ViewModelProvider(this).get(CondominioViewModel::class.java)
        observeViewModel()

        // Iniciando o listener de Firestore
        viewModel.iniciarFirestoreListener()
    }

    private fun observeViewModel() {
        viewModel.condominioList.observe(this) { condominios: List<SelecionaCondominioModel> ->
            cardAdapter.updateCondominios(condominios)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        viewModel.removerFirestoreListener()
    }
}
