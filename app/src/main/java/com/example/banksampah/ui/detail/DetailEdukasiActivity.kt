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
import com.example.banksampah.ui.Model.Edukasi

class DetailEdukasiActivity : AppCompatActivity() {

    companion object {
        const val KEY_EDUKASI = "key_edukasi"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail_edukasi)

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

        val tvJenis: TextView = findViewById(R.id.tv_sampah)
        val tvDeskripsi: TextView = findViewById(R.id.tv_detailSampah)
        val tvHarga: TextView = findViewById(R.id.tv_harga)
        val ivgambar: ImageView = findViewById(R.id.iv_sampah)

        val dataEdukasi = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(KEY_EDUKASI, Edukasi::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<Edukasi>(KEY_EDUKASI)
        }

        if (dataEdukasi != null) {
            tvJenis.text = dataEdukasi.jenis
            tvDeskripsi.text = dataEdukasi.deskripsi
            tvHarga.text = dataEdukasi.harga
            ivgambar.setImageResource(dataEdukasi.gambar)
        }
    }
}
