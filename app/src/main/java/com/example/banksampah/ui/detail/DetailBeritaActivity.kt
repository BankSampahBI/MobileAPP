package com.example.banksampah.ui.detail

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.banksampah.MainActivity
import com.example.banksampah.R
import com.example.banksampah.ui.Model.Berita

class DetailBeritaActivity : AppCompatActivity() {

    companion object {
        const val KEY_BERITA = "key_berita"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail_berita)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnBack: ImageView = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val tvJudul: TextView = findViewById(R.id.tv_judulBerita)
        val tvIsi: TextView = findViewById(R.id.tv_isiBerita)
        val ivFoto: ImageView = findViewById(R.id.iv_fotoBerita)

        val dataBerita = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(KEY_BERITA, Berita::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<Berita>(KEY_BERITA)
        }

        dataBerita?.let {
            tvJudul.text = it.judul
            tvIsi.text = it.isi
            ivFoto.setImageResource(it.foto)
        }
    }
}
