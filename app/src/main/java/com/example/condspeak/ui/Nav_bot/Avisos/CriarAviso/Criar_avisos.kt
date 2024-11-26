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
import androidx.compose.ui.semantics.text

import com.example.condspeak.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class Criar_avisos : Fragment() {


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
        val switch = view.findViewById<Switch>(R.id.swt)

        val opcoes = arrayOf("Opção 1", "Opção 2", "Opção 3")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opcoes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val spinner = view.findViewById<Spinner>(R.id.seleciona)
        val image = view.findViewById<ImageView>(R.id.imagemseila)
        spinner.adapter = adapter
        val inicio = view.findViewById<EditText>(R.id.editTextText2)
        val fim = view.findViewById<EditText>(R.id.editTextText3)
        val novoTexto = inicio.text.toString() 
        tempofinal(spinner, novoTexto, fim)

        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val novoTexto = inicio.text.toString()
                tempofinal(spinner, novoTexto, fim)
                spinner.visibility = View.VISIBLE
                image.visibility = View.VISIBLE
                inicio.visibility = View.VISIBLE
                fim.visibility = View.VISIBLE
            } else {
                val novoTexto = inicio.text.toString()
                tempofinal(spinner, novoTexto, fim)
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
                val novoTexto = inicio.text.toString()
                tempofinal(spinner, novoTexto, fim)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })


        return view
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