package com.example.condspeak.ui.Nav_bot.Avisos.CriarAviso

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Switch
import androidx.appcompat.widget.SwitchCompat
import androidx.compose.ui.semantics.text
import androidx.lifecycle.ViewModelProvider


import com.example.condspeak.R
import com.example.condspeak.data.model.Aviso
import com.example.condspeak.viewmodel.AvisoViewModel


import com.example.condspeak.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class Criar_avisos : Fragment() {
    private lateinit var avisoViewModel: AvisoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_criar_avisos, container, false)
        val switch = view.findViewById<SwitchCompat>(R.id.swt)

        val opcoes = arrayOf("Opção 1", "Opção 2", "Opção 3")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opcoes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val titulo = view.findViewById<EditText>(R.id.edttitulo_aviso)
        val mensagem = view.findViewById<EditText>(R.id.edtmessage_aviso)
        avisoViewModel = ViewModelProvider(requireActivity()).get(AvisoViewModel::class.java)
        val spinner = view.findViewById<Spinner>(R.id.seleciona)
        val image = view.findViewById<ImageView>(R.id.imagemseila)
        spinner.adapter = adapter
        val inicio = view.findViewById<EditText>(R.id.edtdateinicio)
        val fim = view.findViewById<EditText>(R.id.edtdatefim)
        val btnCriar = view.findViewById<EditText>(R.id.btnCriar)
        val novoTexto = inicio.text.toString()

        tempofinal(spinner, novoTexto, fim)
        btnCriar.setOnClickListener {
            if (switch.isChecked) {
                val spinnerValue = spinner.selectedItem.toString()
                val tempoinicio = inicio.text.toString()
                val tempofim = fim.text.toString()
                salvardois(spinnerValue, tempoinicio, tempofim) // Chama a função salvar()
            }else{
                val tituloTexto = titulo.text.toString()
                val mensagemTexto = mensagem.text.toString()
                salvar(tituloTexto, mensagemTexto) // Chama a função salvar()
            }
        }

        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                spinner.visibility = View.VISIBLE
                image.visibility = View.VISIBLE
                inicio.visibility = View.VISIBLE
                fim.visibility = View.VISIBLE
                val dia = inicio.text.toString()
                tempofinal(spinner, dia, fim) // Chama a função depois de definir a visibilidade
            } else {
                spinner.visibility = View.GONE
                image.visibility = View.GONE
                inicio.visibility = View.GONE
                fim.visibility = View.GONE
            }
        }
        val formatoData = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val hoje = calendar.time
        inicio.setText(formatoData.format(hoje))
        inicio.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val dia = inicio.text.toString()
                tempofinal(spinner, dia, fim)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })


        return view
    }
    private fun salvar(titulo: String, mensagem: String) {
        if (titulo.isEmpty() || mensagem.isEmpty()) {
            // Exibir mensagem de erro ou realizar alguma ação
        } else {
            val usuarioId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            val timestamp = System.currentTimeMillis()
            val data = Date(timestamp)
            val aviso = Aviso("aviso", titulo, mensagem, data, "", usuarioId, "", "")
            avisoViewModel.saveAvisoData(aviso) // Usa a instância do ViewModel
        }
    }
    private fun salvardois(spinnerValue: String, dateinicio: String, datefim: String) {
        if (spinnerValue.isEmpty() || dateinicio.isEmpty() || datefim.isEmpty()) {
            // Exibir mensagem de erro ou realizar alguma ação
        } else {
            val usuarioId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            val timestamp = System.currentTimeMillis()
            val data = Date(timestamp)
            if (spinnerValue == "Opção 1") {
                val image =
            }
            val aviso = Aviso("aviso", "", "", data, spinnerValue, usuarioId, dateinicio, datefim)
        }
    }
}

private fun tempofinal(spinner: Spinner, novoTexto: String, fim: EditText) {
    if (spinner.selectedItem.toString() == "Opção 1") {
        val formatoData = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        if (novoTexto.matches(Regex("\\d{2}/\\d{2}/\\d{4}"))) {
            try {
                val dataInicial = formatoData.parse(novoTexto)
                if (dataInicial != null) {
                    val calendar = Calendar.getInstance()
                    calendar.time = dataInicial
                    calendar.add(Calendar.DAY_OF_MONTH, 10)
                    val novaData = calendar.time
                    fim.setText(formatoData.format(novaData))
                }
            } catch (e: ParseException) {

            }
        } else {

        }
    }
    if (spinner.selectedItem.toString() == "Opção 2") {
        val formatoData = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        if (novoTexto.matches(Regex("\\d{2}/\\d{2}/\\d{4}"))) {
            try {
                val dataInicial = formatoData.parse(novoTexto)
                if (dataInicial != null) {
                    val calendar = Calendar.getInstance()
                    calendar.time = dataInicial
                    calendar.add(Calendar.DAY_OF_MONTH, 20)
                    val novaData = calendar.time
                    fim.setText(formatoData.format(novaData))
                }
            } catch (e: ParseException) {

            }
        } else {

        }
    }
    if (spinner.selectedItem.toString() == "Opção 3") {
        val formatoData = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        if (novoTexto.matches(Regex("\\d{2}/\\d{2}/\\d{4}"))) {
            try {
                val dataInicial = formatoData.parse(novoTexto)
                if (dataInicial != null) {
                    val calendar = Calendar.getInstance()
                    calendar.time = dataInicial
                    calendar.add(Calendar.DAY_OF_MONTH, 5)
                    val novaData = calendar.time
                    fim.setText(formatoData.format(novaData))
                }
            } catch (e: ParseException) {

            }
        } else {

        }
    }

}