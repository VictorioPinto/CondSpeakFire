package com.example.condspeak.data.Masks

import android.text.Editable
import android.text.TextWatcher

class TelefoneMask : TextWatcher {

    private var isRunning = false
    private var isDeleting = false

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        isDeleting = count > after
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
        if (isRunning || isDeleting) {
            return
        }
        isRunning = true

        val editable = s?.toString() ?: ""
        val formattedTelefone = formatTelefone(editable)

        s?.replace(0, s.length, formattedTelefone)

        isRunning = false
    }

    private fun formatTelefone(telefone: String): String {
        val cleanedTelefone = telefone.replace("[^0-9]".toRegex(), "")
        return when (cleanedTelefone.length) {
            0 -> ""
            in 1..2 -> cleanedTelefone
            in 3..6 -> "(${cleanedTelefone.substring(0, 2)}) ${cleanedTelefone.substring(2)}"
            in 7..10 -> "(${cleanedTelefone.substring(0, 2)}) ${cleanedTelefone.substring(2, 6)}-${cleanedTelefone.substring(6)}"
            in 11..14 -> "(${cleanedTelefone.substring(0, 2)}) ${cleanedTelefone.substring(2, 7)}-${cleanedTelefone.substring(7)}"
            else -> cleanedTelefone.substring(0, 14) // Limita a 14 dígitos (com DDD e 9 dígitos)
        }
    }
}