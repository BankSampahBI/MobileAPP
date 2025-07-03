package com.example.banksampah.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.banksampah.MainActivity
import com.example.banksampah.R
import com.example.banksampah.ui.model.ViewModelFactory
import com.example.banksampah.ui.autentikasi.login.LoginViewModel

class SplashActivity : AppCompatActivity() {

    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val splash: ImageView = findViewById(R.id.iv_splash)
        splash.alpha = 0f
        splash.animate().setDuration(300).alpha(1f).withEndAction {
            loginViewModel.getSession().observe(this) { user ->
                val intent = if (user.isLogin) {
                    Intent(this, MainActivity::class.java)
                } else {
                    Intent(this, StartActivity::class.java)
                }
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
        }

        supportActionBar?.hide()
    }
}
