package com.example.condspeak.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.condspeak.R
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
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mensagem, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mensagem = mensagens[position]
        holder.remetenteTextView.text = mensagem.remetente
        holder.mensagemTextView.text = mensagem.texto
        // Formatar o timestamp (mensagem.timestamp) antes de exibir
        val timestamp = mensagem.timestamp as com.google.firebase.Timestamp
        val date = timestamp.toDate()
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val formattedTimestamp = formatter.format(date)
        holder.timestampTextView.text = formattedTimestamp
    }

    override fun getItemCount(): Int {
        return mensagens.size
    }
}