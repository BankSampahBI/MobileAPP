package com.example.banksampah.ui.riwayat

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.banksampah.MainActivity
import com.example.banksampah.R
import com.example.banksampah.ui.Model.RiwayatPenyetoran
import com.example.banksampah.ui.adapter.ListRiwayatPenyetoranAdapter

class RiwayatPenyetoranActivity : AppCompatActivity() {

    private lateinit var rvRiwayatPenyetoran: RecyclerView
    private val list = ArrayList<RiwayatPenyetoran>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_riwayat_penyetoran)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rvRiwayatPenyetoran = findViewById(R.id.rv_riwayatPenyetoran)
        rvRiwayatPenyetoran.setHasFixedSize(true)
        rvRiwayatPenyetoran.layoutManager = LinearLayoutManager(this)

        list.addAll(getListRiwayatPenyetoran())
        rvRiwayatPenyetoran.adapter = ListRiwayatPenyetoranAdapter(list)

        val btnBack: ImageView = findViewById(R.id.iv_back)
        btnBack.setOnClickListener {
            val intent = Intent(this, RiwayatPenarikanActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getListRiwayatPenyetoran(): ArrayList<RiwayatPenyetoran> {
        val jenis = resources.getStringArray(R.array.jenis_sampah)
        val berat = resources.getStringArray(R.array.berat_sampah)
        val tanggal = resources.getStringArray(R.array.tanggal_penyetoran)
        val harga = resources.getStringArray(R.array.harga)

        val listRiwayat = ArrayList<RiwayatPenyetoran>()
        for (i in jenis.indices) {
            listRiwayat.add(RiwayatPenyetoran(jenis[i], berat[i], tanggal[i], harga[i]))
        }
        return listRiwayat
    }
}
