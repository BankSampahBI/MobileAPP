package com.example.banksampah.ui.checkout

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.banksampah.data.Result
import com.example.banksampah.data.remote.response.CartItem
import com.example.banksampah.databinding.ActivityCheckoutBinding
import com.example.banksampah.ui.adapter.CheckoutAdapter
import com.example.banksampah.ui.model.ViewModelFactory
import com.example.banksampah.utils.getUserToken
import kotlinx.coroutines.launch

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding

    // ✅ Gunakan ViewModelFactory
    private val viewModel: CheckoutViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var adapter: CheckoutAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = CheckoutAdapter(emptyList())
        binding.rvCheckoutItems.layoutManager = LinearLayoutManager(this)
        binding.rvCheckoutItems.adapter = adapter

        // ✅ Ambil data item yang dipilih dari intent
        val selectedItems = intent.getParcelableArrayListExtra<CartItem>("selected_items") ?: arrayListOf()
        viewModel.loadCart(selectedItems)

        // ✅ Ambil token user dari session / preference
        lifecycleScope.launch {
            getUserToken(this@CheckoutActivity)?.let {
                viewModel.setToken(it)
            }
        }

        viewModel.cartItems.observe(this) {
            adapter.updateData(it)
        }

        viewModel.totalPrice.observe(this) {
            binding.tvTotalPrice.text = "Total: Rp$it"
        }

        binding.btnConfirmCheckout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Konfirmasi Checkout")
                .setMessage("Yakin ingin melakukan checkout?")
                .setPositiveButton("Ya") { _, _ -> doCheckout() }
                .setNegativeButton("Batal", null)
                .show()
        }
    }

    private fun doCheckout() {
        lifecycleScope.launch {
            viewModel.checkout().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.btnConfirmCheckout.isEnabled = false
                    }
                    is Result.Success -> {
                        // ✅ Ganti Toast dengan AlertDialog sukses
                        AlertDialog.Builder(this@CheckoutActivity)
                            .setTitle("Sukses")
                            .setMessage("Checkout berhasil. Terima kasih telah berbelanja!")
                            .setPositiveButton("Oke") { _, _ ->
                                finish() // Tutup halaman
                            }
                            .setCancelable(false)
                            .show()
                    }
                    is Result.Error -> {
                        binding.btnConfirmCheckout.isEnabled = true
                        Toast.makeText(
                            this@CheckoutActivity,
                            "Gagal: ${result.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }
}
