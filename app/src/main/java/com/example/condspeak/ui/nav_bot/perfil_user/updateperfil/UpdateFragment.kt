package com.example.condspeak.ui.nav_bot.perfil_user.updateperfil

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.condspeak.R
import com.example.condspeak.data.model.User
import com.example.condspeak.ui.nav_bot.perfil_user.UserFragment
import com.example.condspeak.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class UpdateFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var userViewModel: UserViewModel
    private lateinit var nome: EditText
    private lateinit var email: EditText
    private lateinit var cpf: EditText
    private lateinit var telefone: EditText
    private lateinit var btnsalvar: EditText
    private lateinit var user: User




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_update, container, false)
        initViews(view)
        return view
    }
    private fun initViews(view: View) {
        btnsalvar = view.findViewById(R.id.btnSaveUser)
        nome = view.findViewById(R.id.edtNomePerfil)
        email = view.findViewById(R.id.edtEmailPerfil)
        cpf = view.findViewById(R.id.edtCpfPerfil)
        telefone = view.findViewById(R.id.edtTelefonePerfil)

        auth = FirebaseAuth.getInstance()
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        val usuarioId = auth.currentUser?.uid ?: return
        userViewModel.getUserData(usuarioId) { user: User? ->

            nome.setText(user?.nome)
            email.setText(user?.email)
            cpf.setText(user?.cpf)
            telefone.setText(user?.telefone)

            btnsalvar.setOnClickListener {
                val updatedUser = user?.copy(
                    nome = nome.text.toString(),
                    email = email.text.toString(),
                    cpf = cpf.text.toString(),
                    telefone = telefone.text.toString()
                ) ?: User()

                lifecycleScope.launch {
                    userViewModel.updateUserData(updatedUser) { success, message ->
                        if (success) {

                            replaceFragment(UserFragment())
                            requireActivity().title = "Reclamacao"
                        } else {
                        }
                    }
                }
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.navFragment, fragment)
            .commit()
    }


}