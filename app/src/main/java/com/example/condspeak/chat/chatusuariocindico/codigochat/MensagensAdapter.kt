package com.example.condspeak.chat.chatusuariocindico.codigochat

import android.annotation.SuppressLint
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.condspeak.R
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Locale

class MensagemAdapter(private val mensagens: List<Mensagem>) :
    RecyclerView.Adapter<MensagemAdapter.MensagemViewHolder>() {

    private val usuarioId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    class MensagemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMensagem: TextView = itemView.findViewById(R.id.tvMensagem)
        val llMensagem: LinearLayout = itemView.findViewById(R.id.llMensagem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MensagemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mensagem, parent, false)
        return MensagemViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MensagemViewHolder, position: Int) {
        val mensagem = mensagens[position]


        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val dataHoraFormatada = dateFormat.format(mensagem.timestamp)

        holder.tvMensagem.text = "  ${dataHoraFormatada} /n ${mensagem.conteudo}"
        Log.d("MensagemAdapter", "remetenteId: ${mensagem.remetenteId}, usuarioId: $usuarioId")

        if (mensagem.remetenteId == usuarioId) {
            holder.llMensagem.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
            (holder.llMensagem.layoutParams as LinearLayout.LayoutParams).gravity = Gravity.END
        } else {
            holder.llMensagem.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
            (holder.llMensagem.layoutParams as LinearLayout.LayoutParams).gravity = Gravity.START
        }
    }

    override fun getItemCount(): Int {
        return mensagens.size
    }
}