package com.example.condspeak.chat.chatusuariocindico.selecionarpessoa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.condspeak.R

class PessoaAdapter(private val pessoas: List<pessoa>, private val onClickListener: (pessoa) -> Unit) :
    RecyclerView.Adapter<PessoaAdapter.PessoaViewHolder>() {

    class PessoaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeTextView: TextView = itemView.findViewById(R.id.nome)
        val proximaPaginaButton: Button = itemView.findViewById(R.id.proximapagina)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PessoaViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardpessoa, parent, false)
        return PessoaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PessoaViewHolder, position: Int) {
        val pessoa = pessoas[position]
        holder.nomeTextView.text = pessoa.nome
        holder.proximaPaginaButton.setOnClickListener { onClickListener(pessoa) }
    }

    override fun getItemCount(): Int {
        return pessoas.size
    }
}