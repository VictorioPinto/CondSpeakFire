package com.example.condspeak.ui.nav_bot.avisos.quadrodeavisos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale
import com.example.condspeak.R
import com.example.condspeak.data.model.Aviso


class AvisoAdapter : RecyclerView.Adapter<AvisoAdapter.AvisoViewHolder>() {

    private var avisos: List<Aviso> = emptyList()

    class AvisoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tituloTextView: TextView = itemView.findViewById(R.id.tituloAvisoTextView)
        val mensagemTextView: TextView = itemView.findViewById(R.id.mensagemAvisoTextView)
        val dataTextView: TextView = itemView.findViewById(R.id.dataAvisoTextView)
        val tipoAvisoTextView: TextView = itemView.findViewById(R.id.tipoAvisoTextView)
        val imagemAvisoImageView: ImageView = itemView.findViewById(R.id.imagemAvisoImageView)
        val dateInicioTextView: TextView = itemView.findViewById(R.id.dateInicioTextView)
        val dateFimTextView: TextView = itemView.findViewById(R.id.dateFimTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvisoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_aviso, parent, false)
        return AvisoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AvisoViewHolder, position: Int) {
        val aviso = avisos[position]
        holder.tituloTextView.text = aviso.titulo
        holder.mensagemTextView.text = aviso.descricao
        val formatoData = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        holder.dataTextView.text = formatoData.format(aviso.data.toDate())
        holder.tipoAvisoTextView.text = aviso.valueSpinner
        holder.dateInicioTextView.text = aviso.datainicio
        holder.dateFimTextView.text = aviso.datafim
        holder.imagemAvisoImageView.setImageResource(R.drawable.logo_com_nome)
    }

    override fun getItemCount(): Int {
        return avisos.size
    }

    fun setAvisos(newAvisos: List<Aviso>) {
        avisos = newAvisos
        notifyDataSetChanged() // Use notifyDataSetChanged() se não estiver usando DiffUtil
    }
}
