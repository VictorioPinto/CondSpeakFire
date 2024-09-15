package com.example.condspeak.selcionacond

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.example.condspeak.extra.ValorGlobal
import com.example.condspeak.chat.Escolha_Chat
import com.example.condspeak.databinding.CardCellBinding

class CardViewHolder(private val cardCellBinding: CardCellBinding) : RecyclerView.ViewHolder(cardCellBinding.root)
{
fun bindCondominio(condominio: Condominio){
    cardCellBinding.nome.text = condominio.nome
    cardCellBinding.CEP.text = condominio.CEP
    cardCellBinding.proximapagina.setOnClickListener {
        ValorGlobal.Codigo_Condominio = condominio.codigo
        ValorGlobal.tipoUsuario = condominio.tipo
        val intent = Intent(itemView.context, Escolha_Chat::class.java)
        itemView.context.startActivity(intent)
    }

}

}
