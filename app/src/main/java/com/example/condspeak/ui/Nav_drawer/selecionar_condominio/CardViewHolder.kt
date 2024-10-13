package com.example.condspeak.ui.Nav_drawer.selecionar_condominio

import SelecionaCondominioModel
import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.condspeak.ui.Navigation.Drawer_and_Bottom_Nav

import com.example.condspeak.extra.ValorGlobal
import com.example.condspeak.databinding.CardCellBinding
import com.example.condspeak.extra.Telaquaseprincipal

class CardViewHolder(private val cardCellBinding: CardCellBinding) :
    RecyclerView.ViewHolder(cardCellBinding.root) {

    fun bindCondominio(condominio: SelecionaCondominioModel) {
        cardCellBinding.nome.text = condominio.nome
        cardCellBinding.CEP.text = condominio.tipo
        cardCellBinding.proximapagina.setOnClickListener {
            Log.d("CardViewHolder", condominio.codigo + condominio.tipo)
            ValorGlobal.Codigo_Condominio = condominio.codigo
            ValorGlobal.tipoUsuario = condominio.tipo.toString()
            val intent = Intent(itemView.context, Drawer_and_Bottom_Nav::class.java)
            itemView.context.startActivity(intent)
        }
    }
}