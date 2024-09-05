package com.example.condspeak.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.condspeak.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Locale

class MensagensAdapter(private val mensagens: List<Mensagem>) :
    RecyclerView.Adapter<MensagensAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val remetenteTextView: TextView = itemView.findViewById(R.id.remetenteTextView)
        val mensagemTextView: TextView = itemView.findViewById(R.id.mensagemTextView)
        val timestampTextView: TextView = itemView.findViewById(R.id.timestampTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = if (viewType == MENSAGEM_DIREITA) {
            R.layout.item_mensagem_direita
        } else {
            R.layout.item_mensagem_esquerda
        }
        val itemView = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ViewHolder(itemView)
    }

    companion object {
        private const val MENSAGEM_DIREITA = 0
        private const val MENSAGEM_ESQUERDA = 1
    }

    override fun getItemViewType(position: Int): Int {
        val mensagem = mensagens[position]
        val usuarioAtual = Firebase.auth.currentUser?.uid ?: ""

        return if (mensagem.remetente == usuarioAtual) {
            MENSAGEM_DIREITA
        } else {
            MENSAGEM_ESQUERDA
        }
    } // This closing brace was missing

    override fun onBindViewHolder(holder: ViewHolder,position: Int) {
        val mensagem = mensagens[position]
        holder.remetenteTextView.text = mensagem.remetente
        holder.mensagemTextView.text = mensagem.texto

        mensagem.timestamp?.let { timestamp ->
            val date = timestamp.toDate()
            val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val formattedTimestamp = formatter.format(date)
            holder.timestampTextView.text = formattedTimestamp
        }
    }

    override fun getItemCount(): Int {
        return mensagens.size
    }
}