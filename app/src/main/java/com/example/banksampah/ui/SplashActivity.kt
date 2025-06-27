package com.example.banksampah.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.banksampah.R

class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val splash: ImageView = findViewById(R.id.iv_splash)
        splash.alpha = 0f
        splash.animate().setDuration(2000).alpha(1f).withEndAction {
            val intent = Intent(this,StartActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            finish()


        }
        supportActionBar?.hide()
    }
}