package com.example.condspeak.ui.nav_bot.avisos.quadrodeavisos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.condspeak.data.model.Aviso
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.condspeak.R
import com.example.condspeak.ui.nav_bot.avisos.criarAviso.Criar_avisos
import com.example.condspeak.viewmodel.AvisoViewModel





class AvisosFragment : Fragment() {
    private lateinit var avisoViewModel: AvisoViewModel
    private lateinit var recyclerView: RecyclerView // Declare recyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_avisos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnaddaviso: TextView = view.findViewById(R.id.addaviso)
        recyclerView = view.findViewById(R.id.recyclerViewAviso)
        avisoViewModel = ViewModelProvider(this).get(AvisoViewModel::class.java)
        btnaddaviso.setOnClickListener{
            replaceFragment(Criar_avisos())
            requireActivity().title = "Criar Aviso"
        }


        val adapter = AvisoAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        avisoViewModel.getAvisos().observe(viewLifecycleOwner) { avisos: List<Aviso> ->
            avisos?.let {
                adapter.setAvisos(it)
            }
        }
        avisoViewModel.getAvisos()
    }
    private fun replaceFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.navFragment, fragment)
            .commit()
    }
}