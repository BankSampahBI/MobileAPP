package com.example.banksampah.ui.riwayat

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.banksampah.MainActivity
import com.example.banksampah.R
import com.example.banksampah.ui.adapter.ListRiwayatPenarikanAdapter
import com.example.banksampah.ui.model.RiwayatPenarikanViewModel
import com.example.banksampah.ui.model.ViewModelFactory

class RiwayatPenarikanActivity : AppCompatActivity() {

    private lateinit var rvRiwayatPenarikan: RecyclerView
    private lateinit var adapter: ListRiwayatPenarikanAdapter
    private lateinit var viewModel: RiwayatPenarikanViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat_penarikan)

        // Init adapter
        adapter = ListRiwayatPenarikanAdapter()
        rvRiwayatPenarikan = findViewById(R.id.rv_riwayatPenarikan)
        rvRiwayatPenarikan.layoutManager = LinearLayoutManager(this)
        rvRiwayatPenarikan.adapter = adapter

        // Init ViewModel pakai factory
        viewModel = ViewModelFactory.getInstance(this)
            .create(RiwayatPenarikanViewModel::class.java)

        // Observe data dari ViewModel
        viewModel.riwayat.observe(this) { list ->
            if (list.isNotEmpty()) {
                adapter.setData(list)
            }
        }

        viewModel.getRiwayat()

        // Tombol next dan back
        findViewById<ImageView>(R.id.iv_next).setOnClickListener {
            startActivity(Intent(this, RiwayatPenyetoranActivity::class.java))
        }

        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
