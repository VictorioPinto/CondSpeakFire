package com.example.condspeak.data.Masks

import android.text.Editable
import android.text.TextWatcher

class CpfMask : TextWatcher {

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
        val formattedCpf = formatCpf(editable)

        s?.replace(0, s.length, formattedCpf)

        isRunning = false
    }

    private fun formatCpf(cpf: String): String {
        val cleanedCpf = cpf.replace("[^0-9]".toRegex(), "")
        return when (cleanedCpf.length) {
            0 -> ""
            in 1..3 -> cleanedCpf
            in 4..6 -> "${cleanedCpf.substring(0, 3)}.${cleanedCpf.substring(3)}"
            in 7..9 -> "${cleanedCpf.substring(0, 3)}.${cleanedCpf.substring(3, 6)}.${cleanedCpf.substring(6)}"
            in 10..11 -> "${cleanedCpf.substring(0, 3)}.${cleanedCpf.substring(3, 6)}.${cleanedCpf.substring(6, 9)}-${cleanedCpf.substring(9)}"
            else -> cleanedCpf.substring(0, 11)
        }
    }
}