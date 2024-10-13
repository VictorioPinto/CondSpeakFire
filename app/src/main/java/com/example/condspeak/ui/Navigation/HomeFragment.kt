package com.example.condspeak.ui.Navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.condspeak.AvisosFragment
import com.example.condspeak.R
import com.example.condspeak.ui.Nav_bot.chat.Escolha_chat_Fragment
import com.example.condspeak.ui.Nav_bot.perfil_user.UserFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view =  inflater.inflate(R.layout.fragment_home, container, false)

        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.AvisoBottomNavigation)
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_avisos -> {

                    replaceFragment(AvisosFragment())

                    activity?.title = "Avisos"
                }
                R.id.bottom_chat -> {

                    replaceFragment(Escolha_chat_Fragment())
                    activity?.title = "Chat"
                }
                R.id.bottom_user -> {

                    replaceFragment(UserFragment())

                    activity?.title = "User"
                }
            }

            true
        }

        replaceFragment(AvisosFragment())
        activity?.title = "Avisos"
        bottomNavigationView.selectedItemId = R.id.bottom_avisos


        return view
    }
    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.bottomFragment, fragment)
            .commit()
    }
}