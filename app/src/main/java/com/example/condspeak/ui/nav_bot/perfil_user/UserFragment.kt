package com.example.condspeak.ui.nav_bot.perfil_user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.condspeak.R
import com.example.condspeak.R.id.btnEditarPerfil
import com.example.condspeak.data.model.User
import com.example.condspeak.ui.nav_bot.perfil_user.updateperfil.UpdateFragment
import com.example.condspeak.ui.Registro_Login.login.TelaDeLogin
import com.example.condspeak.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class UserFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var userViewModel: UserViewModel
    private lateinit var nome: TextView
    private lateinit var email: TextView
    private lateinit var cpf: TextView
    private lateinit var telefone: TextView
    private lateinit var btndeletar: EditText
    private lateinit var btneditar: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        btneditar = view.findViewById(btnEditarPerfil)
        btndeletar = view.findViewById(R.id.btnDeletarUser)
        nome = view.findViewById(R.id.txtNomePerfil)
        email = view.findViewById(R.id.txtEmailPerfil)
        cpf = view.findViewById(R.id.txtCpfPerfil)
        telefone = view.findViewById(R.id.txtTelefonePerfil)

        auth = FirebaseAuth.getInstance()
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        val usuarioId = auth.currentUser?.uid ?: return
        userget(usuarioId)

        btndeletar.setOnClickListener {
            lifecycleScope.launch {
                deletaConta(usuarioId)
            }
        }
        btneditar.setOnClickListener {
            replaceFragment(UpdateFragment())
            requireActivity().title = "Reclamacao"
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.navFragment, fragment)
            .commit()
    }
    private fun userget(userId: String) {
        userViewModel.getUserData(userId) { user: User? ->
            nome.text = user?.nome
            email.text = user?.email
            cpf.text = user?.cpf
            telefone.text = user?.telefone
        }
    }

    private suspend fun deletaConta(userId: String) {
        userViewModel.deletarConta(userId)
        startActivity(Intent(requireActivity(), TelaDeLogin::class.java))
    }
}
