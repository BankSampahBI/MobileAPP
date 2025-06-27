package com.example.banksampah.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.banksampah.R
import com.example.banksampah.databinding.ActivityStartBinding
import com.example.banksampah.ui.autentikasi.LoginActivity
import com.example.banksampah.ui.autentikasi.RegisterActivity

class StartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Styling text
        val text = "Bank Sampah bukan hanya tempat menabung, tapi tempat belajar mencintai lingkungan."
        val spannable = SpannableString(text)

        val mountainGreen = ContextCompat.getColor(this, R.color.mountain_green)
        spannable.setSpan(
            ForegroundColorSpan(mountainGreen),
            text.indexOf("Bank Sampah"),
            text.indexOf("Bank Sampah") + "Bank Sampah".length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            ForegroundColorSpan(mountainGreen),
            text.indexOf("mencintai lingkungan"),
            text.indexOf("mencintai lingkungan") + "mencintai lingkungan".length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.textViewQuote.text = spannable

        // Klik tombol
        binding.btnDaftar.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
//            finish()
        }

        binding.btnMasuk.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
//            finish()
        }
    }
}
