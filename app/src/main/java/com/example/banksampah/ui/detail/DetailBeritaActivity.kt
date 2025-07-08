package com.example.banksampah.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.banksampah.data.remote.response.BeritaItem
import com.example.banksampah.databinding.ActivityDetailBeritaBinding

class DetailBeritaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBeritaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBeritaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val berita = intent.getParcelableExtra<BeritaItem>(EXTRA_BERITA)
        if (berita != null) {
            binding.tvJudulBerita.text = berita.judul
            binding.tvIsiBerita.text = berita.isi
            Glide.with(this)
                .load(berita.foto)
                .into(binding.ivFotoBerita)
        }
    }

    companion object {
        const val EXTRA_BERITA = "extra_berita"
    }
}