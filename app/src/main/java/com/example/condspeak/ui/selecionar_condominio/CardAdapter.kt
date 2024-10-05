package com.example.condspeak.ui.selecionar_condominio

import SelecionaCondominioModel
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.condspeak.databinding.CardCellBinding

class CardAdapter(private var condominios: List<SelecionaCondominioModel>) :
    RecyclerView.Adapter<CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = CardCellBinding.inflate(from, parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bindCondominio(condominios[position])
    }

    override fun getItemCount(): Int = condominios.size

    fun updateCondominios(newCondominios: List<SelecionaCondominioModel>) {
        condominios = newCondominios
        notifyDataSetChanged()
    }
}
