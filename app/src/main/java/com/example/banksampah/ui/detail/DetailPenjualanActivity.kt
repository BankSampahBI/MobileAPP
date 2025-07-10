package com.example.banksampah.ui.detail

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.bumptech.glide.Glide
import com.example.banksampah.R
import com.example.banksampah.data.Result
import com.example.banksampah.data.di.Injection
import com.example.banksampah.data.remote.response.PenjualanResponseItem
import com.example.banksampah.databinding.ActivityDetailPenjualanBinding
import com.example.banksampah.ui.cart.CartViewModel
import com.example.banksampah.ui.cart.EditPenjualanActivity
import com.example.banksampah.ui.model.ViewModelFactory

class DetailPenjualanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPenjualanBinding
    private lateinit var penjualan: PenjualanResponseItem
    private lateinit var viewModel: CartViewModel
    private lateinit var token: String

    private val editLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val updated = result.data?.getParcelableExtra<PenjualanResponseItem>("UPDATED_PENJUALAN")
            if (updated != null) {
                penjualan = updated
                updateUI(updated)
                Toast.makeText(this, "Data diperbarui", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPenjualanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Injection.provideUserPreference(this).getSession().asLiveData().observe(this) { user ->
            token = user.token
            val repository = Injection.provideRepository(this, token)
            viewModel = ViewModelProvider(this, ViewModelFactory(repository))[CartViewModel::class.java]
        }

        penjualan = intent.getParcelableExtra("EXTRA_PENJUALAN") ?: return finish()

        updateUI(penjualan)

        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, EditPenjualanActivity::class.java)
            intent.putExtra("EXTRA_PENJUALAN", penjualan)
            editLauncher.launch(intent)
        }

        binding.btnHapus.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Konfirmasi")
                .setMessage("Yakin ingin menghapus barang ini?")
                .setPositiveButton("Hapus") { _: DialogInterface, _: Int ->
                    viewModel.deletePenjualan(token, penjualan.id)
                        .observe(this) { result ->
                            when (result) {
                                is Result.Loading -> {}
                                is Result.Success -> {
                                    Toast.makeText(this, "Berhasil dihapus", Toast.LENGTH_SHORT).show()
                                    setResult(RESULT_OK)
                                    finish()
                                }
                                is Result.Error -> {
                                    Toast.makeText(this, "Gagal menghapus", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                }
                .setNegativeButton("Batal", null)
                .show()
        }
    }

    private fun updateUI(p: PenjualanResponseItem) {
        binding.tvNamaBarang.text = p.namaBarang
        binding.tvDeskripsi.text = p.deskripsi
        binding.tvStok.text = "Stok: ${p.stok}"
        binding.tvHarga.text = "Harga: Rp ${p.harga}"

        when (p.status) {
            "ditolak" -> {
                binding.tvStatus.text = "❌ Ditolak"
                binding.tvStatus.setTextColor(getColor(android.R.color.holo_red_dark))
                binding.tvCatatanAdmin.visibility = View.VISIBLE
                binding.tvCatatanAdmin.text = "Catatan Admin: ${p.catatanAdmin ?: "-"}"
                binding.btnEdit.visibility = View.VISIBLE
            }
            "diterima", "ditampilkan" -> {
                binding.tvStatus.text = "✅ Diterima"
                binding.tvStatus.setTextColor(getColor(android.R.color.holo_green_dark))
                binding.tvCatatanAdmin.visibility = View.GONE
                binding.btnEdit.visibility = View.GONE
            }
            else -> {
                binding.tvStatus.text = "⏳ Menunggu Validasi"
                binding.tvStatus.setTextColor(getColor(android.R.color.darker_gray))
                binding.tvCatatanAdmin.visibility = View.GONE
                binding.btnEdit.visibility = View.VISIBLE
            }
        }

        Glide.with(this)
            .load(p.foto)
            .into(binding.imgBarang)

        // 👉 klik untuk preview besar
        binding.imgBarang.setOnClickListener {
            showImagePreviewDialog(p.foto)
        }
    }

    private fun showImagePreviewDialog(imageUrl: String?) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_image_preview)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        // Tambahkan ini untuk hilangkan background putih
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val imageView = dialog.findViewById<ImageView>(R.id.img_preview)

        Glide.with(this)
            .load(imageUrl)
            .into(imageView)

        imageView.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

}
