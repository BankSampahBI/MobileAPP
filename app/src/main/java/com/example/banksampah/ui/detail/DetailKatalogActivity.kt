package com.example.banksampah.ui.detail

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.banksampah.R
import com.example.banksampah.data.remote.response.DataItem

class DetailKatalogActivity : AppCompatActivity() {

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
        btnBack.setOnClickListener { finish() }

        val textViewNama: TextView = findViewById(R.id.tv_sampah)
        val textViewHarga: TextView = findViewById(R.id.tv_harga)
        val textViewDeskripsi: TextView = findViewById(R.id.tv_detailSampah)
        val imageViewFoto: ImageView = findViewById(R.id.iv_sampah)

        val katalog = intent.getParcelableExtra<DataItem>("extra_katalog")
        katalog?.let {
            textViewNama.text = it.nama
            textViewHarga.text = "Rp. ${it.harga}"
            textViewDeskripsi.text = it.deskripsi

            Glide.with(this)
                .load(katalog.foto) // ganti ke domain kamu
                .into(imageViewFoto)
        }
    }
}