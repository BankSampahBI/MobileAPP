package com.example.banksampah.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.banksampah.data.Result
import com.example.banksampah.data.remote.response.CartItem
import com.example.banksampah.databinding.ActivityDetailCartBinding
import com.example.banksampah.databinding.DialogJumlahBinding
import com.example.banksampah.ui.model.DetailCartViewModel
import com.example.banksampah.ui.model.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DetailCartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailCartBinding
    private val viewModel: DetailCartViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private var currentItem: CartItem? = null

    companion object {
        const val EXTRA_BARANG_CART = "extra_barang_cart"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentItem = intent.getParcelableExtra(EXTRA_BARANG_CART)

        currentItem?.let { item ->
            binding.tvNamaBarang.text = item.penjualan.namaBarang
            binding.tvHarga.text = "Rp ${item.penjualan.harga}"
            binding.tvDeskripsi.text = item.penjualan.deskripsi
            binding.tvStok.text = "Stok: ${item.penjualan.stok}"
            binding.tvJumlah.text = "Jumlah: ${item.jumlah}"

            Glide.with(this)
                .load(item.penjualan.foto)
                .placeholder(android.R.color.darker_gray)
                .into(binding.ivFoto)

            binding.btnEditJumlah.setOnClickListener {
                showInputJumlahDialog(item)
            }

            binding.btnSimpan.setOnClickListener {
                val newJumlah = currentItem?.jumlah ?: 0
                if (newJumlah <= 0) {
                    showAlert("Jumlah Tidak Valid", "Jumlah harus lebih dari 0")
                    return@setOnClickListener
                }
                lifecycleScope.launch {
                    val user = viewModel.getSession().first()
                    viewModel.updateCart(user.token, item.id, newJumlah)
                        .observe(this@DetailCartActivity) { result ->
                            when (result) {
                                is Result.Loading -> {}
                                is Result.Success -> {
                                    showAlert("Berhasil", result.data)
                                }
                                is Result.Error -> {
                                    showAlert("Gagal", result.message ?: "Terjadi kesalahan")
                                }
                            }
                        }
                }
            }
        }
    }

    private fun showInputJumlahDialog(item: CartItem) {
        val dialogBinding = DialogJumlahBinding.inflate(LayoutInflater.from(this))
        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setTitle("Ubah Jumlah")
            .setPositiveButton("Simpan") { dialog, _ ->
                val inputText = dialogBinding.etJumlah.text.toString()
                val newJumlah = inputText.toIntOrNull() ?: 0

                if (newJumlah <= 0) {
                    showAlert("Jumlah Tidak Valid", "Jumlah harus lebih dari 0")
                    return@setPositiveButton
                }
                if (newJumlah > item.penjualan.stok) {
                    showAlert("Stok Tidak Cukup", "Stok hanya tersedia ${item.penjualan.stok}")
                    return@setPositiveButton
                }

                currentItem = item.copy(jumlah = newJumlah)
                binding.tvJumlah.text = "Jumlah: $newJumlah"
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
            .create()

        alertDialog.show()
    }

    private fun showAlert(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
