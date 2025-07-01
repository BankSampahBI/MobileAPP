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
import com.example.banksampah.ui.Model.RiwayatPenarikan
import com.example.banksampah.ui.adapter.ListRiwayatPenarikanAdapter

class RiwayatPenarikanActivity : AppCompatActivity() {

    private lateinit var rvRiwayatPenarikan: RecyclerView
    private val list = ArrayList<RiwayatPenarikan>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_riwayat_penarikan)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rvRiwayatPenarikan = findViewById(R.id.rv_riwayatPenarikan)
        rvRiwayatPenarikan.setHasFixedSize(true)
        rvRiwayatPenarikan.layoutManager = LinearLayoutManager(this)

        list.addAll(getListRiwayatPenarikan())
        rvRiwayatPenarikan.adapter = ListRiwayatPenarikanAdapter(list)

        val btnNext: ImageView = findViewById(R.id.iv_next)
        btnNext.setOnClickListener {
            val intent = Intent(this, RiwayatPenyetoranActivity::class.java)
            startActivity(intent)
        }

        val btnBack: ImageView = findViewById(R.id.iv_back)
        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getListRiwayatPenarikan(): ArrayList<RiwayatPenarikan> {
        val tanggal = resources.getStringArray(R.array.tanggal_penarikan)
        val jumlah = resources.getStringArray(R.array.jumlah_penarikan)

        val listRiwayat = ArrayList<RiwayatPenarikan>()
        for (i in jumlah.indices) {
            listRiwayat.add(RiwayatPenarikan(jumlah[i], tanggal[i]))
        }
        return listRiwayat
    }
}
