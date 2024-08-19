package com.example.condspeak.selcionacond

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.example.condspeak.Telaquaseprincipal
import com.example.condspeak.databinding.CardCellBinding

class CardViewHolder(private val cardCellBinding: CardCellBinding) : RecyclerView.ViewHolder(cardCellBinding.root)
{
fun bindCondominio(condominio: Condominio){
    cardCellBinding.nome.text = condominio.nome
    cardCellBinding.CEP.text = condominio.CEP
    cardCellBinding.proximapagina.setOnClickListener {
        val intent = Intent(itemView.context, Telaquaseprincipal::class.java)
        intent.putExtra("CodigoCondominio", condominio.codigo)
        itemView.context.startActivity(intent)
    }

}

}
