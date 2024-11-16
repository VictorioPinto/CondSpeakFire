package com.example.condspeak.ui.Navigation

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import coil.load
import com.example.condspeak.R
import com.example.condspeak.auth.FirebaseAuthService
import com.example.condspeak.data.model.User
import com.example.condspeak.ui.Nav_drawer.lista_membros.Lista_MembrosFragment
import com.example.condspeak.ui.Nav_drawer.reclamacao.Reclamacao_Fragment
import com.example.condspeak.viewmodel.UserViewModel
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.storage

class Drawer_and_Bottom_Nav : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val onBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            onBackPressedMethod()
        }

    }

    private fun onBackPressedMethod() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }else{
            finish()
        }
    }
    private lateinit var userViewModel: UserViewModel
    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer_and_bottom_nav)
        onBackPressedDispatcher.addCallback(this,onBackPressedCallback)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawerLayout)

        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        val header = navigationView.getHeaderView(0)
        val userNameTxt = header.findViewById<TextView>(R.id.userNameTxta)
        val emailTxt = header.findViewById<TextView>(R.id.emailTxta)
        val profileImg = header.findViewById<ImageView>(R.id.profileImg)
        userViewModel = UserViewModel()
        FirebaseAuthService.auth = FirebaseAuth.getInstance()
        val params = LinearLayout.LayoutParams(200, 200)
        profileImg.layoutParams = params
        FirebaseAuthService.auth.currentUser?.let {
            userViewModel.getUserData(it.uid) { user: User? ->
                userNameTxt.text = user?.nome
                emailTxt.text = user?.email

                // Obtém a referência da imagem usando child
                val storageRef = Firebase.storage.reference.child(user?.imagem ?: "")

                // Obtém a URL da imagem
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()


                    // Carrega a imagem usando Coil
                    profileImg.load(imageUrl) {
                        crossfade(true)
                        placeholder(R.drawable.eletricista) // Substitua pelo seu placeholder
                        error(R.drawable.ic_launcher_background) // Substitua pelo seu drawable de erro
                    }
                }.addOnFailureListener { exception ->
                    // Lidar com erros de download
                    Log.e("Drawer_and_Bottom_Nav", "Erro ao baixar imagem", exception)
                }
            }
        }


        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.open_drawer, R.string.Close_drawer
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        replaceFragment(HomeFragment())
        navigationView.setCheckedItem(R.id.nav_home)
    }


    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.navFragment,fragment)
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_home -> {
                replaceFragment(HomeFragment())

            }

            R.id.nav_lista_membros -> {
                replaceFragment(Lista_MembrosFragment())
                title = "Lista_de_membros"
            }

            R.id.nav_reclamacoes -> {
                replaceFragment(Reclamacao_Fragment())
                title = "Reclamacao"
            }

            R.id.nav_share -> {
                Toast.makeText(this, "Share Clicked", Toast.LENGTH_LONG).show()
            }

            R.id.nav_logout -> {
                Toast.makeText(this, "Logout Clicked", Toast.LENGTH_LONG).show()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}