package com.example.banksampah

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.banksampah.databinding.ActivityMainBinding
import com.example.banksampah.ui.StartActivity
import com.example.banksampah.ui.model.MainViewModel
import com.example.banksampah.ui.model.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.NavGraph

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                )

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        val navView: BottomNavigationView = binding.navView

        mainViewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, StartActivity::class.java))
                finish()
            } else {
                val role = user.role
                Toast.makeText(this, "Login sebagai: $role", Toast.LENGTH_SHORT).show()

                val navInflater = navController.navInflater
                val navGraph: NavGraph = navInflater.inflate(R.navigation.mobile_navigation)

                // 🔀 Set start destination tergantung role
                val startDestination = if (role == "konsumen") {
                    navGraph.findNode(R.id.navigation_konsumen_home)?.id
                        ?: R.id.navigation_konsumen_home
                } else {
                    navGraph.findNode(R.id.navigation_home)?.id
                        ?: R.id.navigation_home
                }
                navGraph.setStartDestination(startDestination)
                navController.graph = navGraph

                // Ganti BottomNavigationView menu sesuai role
                if (role == "konsumen") {
                    navView.menu.clear()
                    navView.inflateMenu(R.menu.bottom_nav_konsumen)
                } else {
                    navView.menu.clear()
                    navView.inflateMenu(R.menu.bottom_nav_menu)
                }

                // Hubungkan navController ke BottomNavigationView
                navView.setupWithNavController(navController)
            }
        }
    }
}
