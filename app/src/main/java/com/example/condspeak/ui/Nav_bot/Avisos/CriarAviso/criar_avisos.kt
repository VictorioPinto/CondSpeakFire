package com.example.condspeak.ui.Nav_bot.Avisos.CriarAviso

import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.compose.ui.semantics.setText
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.intl.Locale
import androidx.fragment.app.add
import com.example.condspeak.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.text.format


class criar_avisos : Fragment() {


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
        val opcoes = arrayOf("Opção 1", "Opção 2", "Opção 3")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opcoes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val spinner = view.findViewById<Spinner>(R.id.seleciona)
        spinner.adapter = adapter
        val inicio = view.findViewById<EditText>(R.id.editTextText2)
        val fim = view.findViewById<EditText>(R.id.editTextText3)
        inicio.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Não precisa fazer nada aqui
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val novoTexto = inicio.text.toString()
                if (spinner.selectedItem.toString() == "Opção 1") {
                    val formatoData = SimpleDateFormat("dd/MM/yyyy",
                        Locale.getDefault())
                    try {
                        val dataInicial = formatoData.parse(novoTexto)
                        if (dataInicial != null) {
                            val calendar = Calendar.getInstance()
                            calendar.time = dataInicial
                            calendar.add(Calendar.DAY_OF_MONTH, 10) // Adiciona 10 dias
                            val novaData = calendar.time
                            fim.setText(formatoData.format(novaData))
                        }
                    } catch (e: ParseException) {
                        // Lidar com erro de parsing
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Não precisa fazer nada aqui
            }
        })


        return view
    }
}