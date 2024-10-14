package com.example.condspeak.ui.Nav_drawer.lista_membros

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.condspeak.R
import com.example.condspeak.extra.ValorGlobal
import com.example.condspeak.viewmodel.ListaDeMembrosViewModel

class Lista_MembrosFragment : Fragment(R.layout.fragment_lista_membros) {

    private val viewModel: ListaDeMembrosViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.membros.observe(viewLifecycleOwner, Observer { membros ->
            val adapter = ListaMembrosAdapter(membros)
            recyclerView.adapter = adapter
        })

        val codigoCondominio = ValorGlobal.CodigoCondominio
        viewModel.carregarMembros(codigoCondominio)
    }
}
