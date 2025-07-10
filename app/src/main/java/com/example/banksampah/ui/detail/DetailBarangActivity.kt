package com.example.banksampah.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.banksampah.data.remote.response.PenjualanKonsumenItem
import com.example.banksampah.databinding.ActivityDetailBarangBinding

class DetailBarangActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBarangBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBarangBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val barang = intent.getParcelableExtra<PenjualanKonsumenItem>(EXTRA_BARANG)
        if (barang != null) {
            binding.tvDetailNamaBarang.text = barang.namaBarang
            binding.tvDetailHarga.text = "Rp ${barang.harga}"
            binding.tvDetailDeskripsi.text = barang.deskripsi

            Glide.with(this)
                .load(barang.foto)
                .placeholder(android.R.color.darker_gray)
                .into(binding.ivDetailFoto)

            binding.btnBeli.setOnClickListener {
                // TODO: proses pembelian atau tambah ke keranjang
            }
        }
    }

    companion object {
        const val EXTRA_BARANG = "extra_barang"
    }
}
