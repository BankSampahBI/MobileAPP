package com.example.banksampah.ui.riwayat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.banksampah.data.Repository
import com.example.banksampah.data.pref.UserPreference
import com.example.banksampah.data.remote.retrofit.ApiConfig
import com.example.banksampah.databinding.ActivityRiwayatPembelianBinding
import com.example.banksampah.ui.adapter.RiwayatPembelianAdapter
import com.example.banksampah.ui.detail.DetailRiwayatPembelianActivity
import com.example.banksampah.ui.model.PembelianViewModel
import com.example.banksampah.ui.model.ViewModelFactory

class RiwayatPembelianActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRiwayatPembelianBinding
    private lateinit var viewModel: PembelianViewModel
    private lateinit var adapter: RiwayatPembelianAdapter

    private val Context.dataStore by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiwayatPembelianBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = UserPreference.getInstance(dataStore)
        val apiService = ApiConfig.getApiService()
        val repository = Repository.getInstance(pref, apiService)
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[PembelianViewModel::class.java]

        adapter = RiwayatPembelianAdapter(emptyList()) { pembelianItem ->

            val intent = Intent(this, DetailRiwayatPembelianActivity::class.java)
            intent.putExtra(DetailRiwayatPembelianActivity.EXTRA_ID, pembelianItem.id)
            startActivity(intent)
        }

        binding.rvRiwayat.layoutManager = LinearLayoutManager(this)
        binding.rvRiwayat.adapter = adapter

        viewModel.riwayat.observe(this) { list ->
            adapter.submitList(list)
        }

        viewModel.getSession().observe(this) { user ->
            viewModel.fetchRiwayat(user.token)
        }
    }
}

