package com.example.condspeak.listademembros

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.ui.semantics.text
import androidx.recyclerview.widget.RecyclerView
import com.example.condspeak.R

class ListaMembrosAdapter(private val membros: List<Model_Membros>) :
    RecyclerView.Adapter<ListaMembrosAdapter.MembroViewHolder>() {

    class MembroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeTextView: TextView = itemView.findViewById(R.id.nomeTextView)
        val emailTextView: TextView = itemView.findViewById(R.id.emailTextView)
        val tipoTextView: TextView = itemView.findViewById(R.id.tipoTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembroViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_membro, parent, false)
        return MembroViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MembroViewHolder, position: Int) {
        val membro = membros[position]
        holder.nomeTextView.text = membro.nome
        holder.emailTextView.text = membro.email
        holder.tipoTextView.text = membro.tipo
    }

    override fun getItemCount(): Int {
        return membros.size
    }
}