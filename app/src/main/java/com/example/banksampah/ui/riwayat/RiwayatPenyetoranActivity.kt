package com.example.banksampah.ui.riwayat

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.banksampah.R
import com.example.banksampah.data.remote.response.DetailsItem
import com.example.banksampah.data.remote.response.Data
import com.example.banksampah.ui.adapter.ListRiwayatPenyetoranAdapter
import com.example.banksampah.ui.model.RiwayatPenyetoranViewModel
import com.example.banksampah.ui.model.ViewModelFactory

class RiwayatPenyetoranActivity : AppCompatActivity() {

    private lateinit var rvRiwayatPenyetoran: RecyclerView
    private lateinit var adapter: ListRiwayatPenyetoranAdapter
    private lateinit var viewModel: RiwayatPenyetoranViewModel

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
        rvRiwayatPenyetoran.layoutManager = LinearLayoutManager(this)
        adapter = ListRiwayatPenyetoranAdapter()
        rvRiwayatPenyetoran.adapter = adapter

        val factory = ViewModelFactory.getInstance(applicationContext)
        viewModel = ViewModelProvider(this, factory)[RiwayatPenyetoranViewModel::class.java]

        viewModel.getSession().observe(this) { user ->
            viewModel.fetchRiwayatSetoran(user.token)
        }

        viewModel.riwayatSetoran.observe(this) { dataList ->
            val allDetails = mutableListOf<Triple<DetailsItem, String, Int>>() // detail, tanggal, saldo
            dataList.forEach { data: Data ->
                data.details.forEach { detail ->
                    allDetails.add(Triple(detail, data.tanggal, data.saldo))
                }
            }
            adapter.setData(allDetails)
        }

        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            finish()
        }
    }
}
