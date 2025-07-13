package com.example.banksampah.ui.detail

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.banksampah.data.Repository
import com.example.banksampah.data.pref.UserPreference
import com.example.banksampah.data.remote.retrofit.ApiConfig
import com.example.banksampah.databinding.ActivityDetailRiwayatPembelianBinding
import com.example.banksampah.ui.adapter.DetailRiwayatPembelianAdapter
import com.example.banksampah.ui.model.PembelianViewModel
import com.example.banksampah.ui.model.ViewModelFactory

class DetailRiwayatPembelianActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailRiwayatPembelianBinding
    private lateinit var viewModel: PembelianViewModel
    private lateinit var adapter: DetailRiwayatPembelianAdapter

    companion object {
        const val EXTRA_ID = "extra_id"
    }

    val Context.dataStore by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRiwayatPembelianBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = UserPreference.getInstance(dataStore)
        val apiService = ApiConfig.getApiService()
        val repository = Repository.getInstance(pref, apiService)
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[PembelianViewModel::class.java]

        viewModel.detailPembelian.observe(this) { dataItemRiwayat ->
            dataItemRiwayat?.let {
                adapter.submitList(it.pembelianDetail)

                // Tambahkan ini:
                binding.tvTanggal.text = "Tanggal: ${it.createdAt.take(10)}"
                binding.tvStatus.text = "Status: ${it.status}"
                binding.tvTotal.text = "Total: Rp ${it.totalHarga}"
            }
        }

        adapter = DetailRiwayatPembelianAdapter(emptyList())
        binding.rvDetail.layoutManager = LinearLayoutManager(this)
        binding.rvDetail.adapter = adapter

        val id = intent.getIntExtra(EXTRA_ID, -1)
        if (id == -1) {
            finish()
            return
        }

        viewModel.getSession().observe(this) { user ->
            if (user.token.isNotEmpty()) {
                viewModel.fetchDetailPembelian(user.token, id)
            }
        }

        viewModel.detailPembelian.observe(this) { dataItemRiwayat ->
            dataItemRiwayat?.let {
                // Kirim list PembelianDetailItem ke adapter
                adapter.submitList(it.pembelianDetail)
            }
        }
        binding.btnBatal.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Konfirmasi")
                .setMessage("Apakah Anda yakin ingin membatalkan pembelian ini?")
                .setPositiveButton("Ya") { _, _ ->
                    viewModel.getSession().observe(this) { user ->
                        viewModel.batalPembelian(user.token, id)
                    }
                }
                .setNegativeButton("Tidak", null)
                .show()
        }

        viewModel.batalStatus.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Pembelian berhasil dibatalkan", Toast.LENGTH_SHORT).show()
                finish() // kembali ke riwayat
            } else {
                Toast.makeText(this, "Gagal membatalkan pembelian", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
