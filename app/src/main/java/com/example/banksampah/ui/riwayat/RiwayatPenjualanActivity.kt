package com.example.banksampah.ui.riwayat

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.banksampah.data.di.Injection
import com.example.banksampah.data.pref.dataStore
import com.example.banksampah.databinding.ActivityRiwayatPenjualanBinding
import com.example.banksampah.ui.adapter.RiwayatPenjualanAdapter
import com.example.banksampah.ui.model.RiwayatPenjualanViewModel
import com.example.banksampah.ui.model.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class RiwayatPenjualanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRiwayatPenjualanBinding
    private lateinit var viewModel: RiwayatPenjualanViewModel
    private lateinit var adapter: RiwayatPenjualanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiwayatPenjualanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi Repository & ViewModelFactory
        val repository = Injection.provideRepository(this)
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[RiwayatPenjualanViewModel::class.java]

        // Setup RecyclerView
        adapter = RiwayatPenjualanAdapter()
        binding.rvRiwayat.layoutManager = LinearLayoutManager(this)
        binding.rvRiwayat.adapter = adapter

        // Ambil token dari UserPreference
        val token = runBlocking {
            Injection.provideUserPreference(this@RiwayatPenjualanActivity)
                .getSession()
                .first()
                .token
        }

        // Panggil API riwayat penjualan
        viewModel.getRiwayat(token)

        // Observe hasil
        viewModel.riwayat.observe(this) { data ->
            adapter.submitList(data)
            binding.tvKosong.visibility = if (data.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
}
