package com.example.banksampah.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.example.banksampah.data.Result
import com.example.banksampah.data.remote.response.PenjualanKonsumenItem
import com.example.banksampah.databinding.ActivityDetailBarangBinding
import com.example.banksampah.databinding.DialogJumlahBinding
import com.example.banksampah.ui.detail.viewmodel.DetailBarangViewModel
import com.example.banksampah.ui.model.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DetailBarangActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBarangBinding
    private val viewModel: DetailBarangViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

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

            binding.btnSimpan.setOnClickListener {
                showInputJumlahDialog(barang)
            }

            binding.btnBeli.setOnClickListener {
                showBeliLangsungDialog(barang)
            }
        }
    }

    private fun showInputJumlahDialog(barang: PenjualanKonsumenItem) {
        val dialogBinding = DialogJumlahBinding.inflate(LayoutInflater.from(this))
        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setTitle("Masukkan Jumlah")
            .setPositiveButton("Tambah") { dialog, _ ->
                val jumlahText = dialogBinding.etJumlah.text.toString()
                val jumlah = jumlahText.toIntOrNull() ?: 0

                if (jumlah <= 0) {
                    showAlert("Jumlah Tidak Valid", "Jumlah harus lebih dari 0.")
                    return@setPositiveButton
                }

                if (jumlah > barang.stok) {
                    showAlert("Stok Tidak Cukup", "Tersisa hanya ${barang.stok} buah.")
                    return@setPositiveButton
                }

                lifecycleScope.launch {
                    val user = viewModel.getSession().first()
                    viewModel.addToCart(user.token, barang.id, jumlah).observe(this@DetailBarangActivity) { result ->
                        when (result) {
                            is Result.Loading -> {
                                // Show loading if needed
                            }
                            is Result.Success -> {
                                showAlert("Berhasil", result.data)
                            }
                            is Result.Error -> {
                                showAlert("Gagal", result.message ?: "Terjadi kesalahan")
                            }
                        }
                    }
                }

                dialog.dismiss()
            }
            .setNegativeButton("Batal", null)
            .create()

        alertDialog.show()
    }

    private fun showBeliLangsungDialog(barang: PenjualanKonsumenItem) {
        val dialogBinding = DialogJumlahBinding.inflate(LayoutInflater.from(this))
        AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setTitle("Beli Langsung")
            .setPositiveButton("Beli") { dialog, _ ->
                val jumlah = dialogBinding.etJumlah.text.toString().toIntOrNull() ?: 0
                if (jumlah <= 0 || jumlah > barang.stok) {
                    showAlert("Error", "Jumlah tidak valid atau melebihi stok")
                    return@setPositiveButton
                }

                lifecycleScope.launch {
                    val user = viewModel.getSession().first()
                    viewModel.beliLangsung(user.token, barang.id, jumlah).observe(this@DetailBarangActivity) { result ->
                        when (result) {
                            is Result.Loading -> { /* Optional loading */ }
                            is Result.Success -> showAlert("Berhasil", result.data.message)
                            is Result.Error -> showAlert("Gagal", result.message ?: "Terjadi kesalahan")
                        }
                    }
                }

                dialog.dismiss()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showAlert(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .create()
            .show()
    }

    companion object {
        const val EXTRA_BARANG = "extra_barang"
    }
}
