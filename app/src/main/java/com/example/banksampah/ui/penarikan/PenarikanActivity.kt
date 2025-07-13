package com.example.banksampah.ui.penarikan

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.banksampah.R
import com.example.banksampah.data.Result
import com.example.banksampah.ui.model.ViewModelFactory
import java.text.NumberFormat
import java.util.Locale

class PenarikanActivity : AppCompatActivity() {

    private lateinit var viewModel: PenarikanViewModel

    private lateinit var tvSaldo: TextView
    private lateinit var etJumlah: EditText
    private lateinit var btnAjukan: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_penarikan)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Init view
        tvSaldo = findViewById(R.id.tv_saldo)
        etJumlah = findViewById(R.id.et_jumlah)
        btnAjukan = findViewById(R.id.btn_ajukan)

        // Init ViewModel
        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[PenarikanViewModel::class.java]

        // Observe saldo
        viewModel.getSaldo()
        viewModel.saldo.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    tvSaldo.text = "Memuat..."
                }
                is Result.Success -> {
                    val saldo = result.data.saldo
                    val formatted = NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(saldo)
                    tvSaldo.text = formatted
                }
                is Result.Error -> {
                    Toast.makeText(this, "Gagal ambil saldo: ${result.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Button Ajukan
        btnAjukan.setOnClickListener {
            val jumlahText = etJumlah.text.toString()
            if (jumlahText.isNotEmpty()) {
                val jumlah = jumlahText.toIntOrNull()
                if (jumlah != null && jumlah > 0) {
                    viewModel.ajukanPenarikan(jumlah)
                } else {
                    Toast.makeText(this, "Jumlah harus lebih dari 0", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Jumlah tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }

        // Observe hasil penarikan
        viewModel.penarikanResult.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    btnAjukan.isEnabled = false
                    btnAjukan.text = "Mengirim..."
                }
                is Result.Success -> {
                    btnAjukan.isEnabled = true
                    btnAjukan.text = "Ajukan Penarikan"
                    Toast.makeText(this, result.data, Toast.LENGTH_LONG).show()
                    finish() // atau refresh halaman
                }
                is Result.Error -> {
                    btnAjukan.isEnabled = true
                    btnAjukan.text = "Ajukan Penarikan"
                    Toast.makeText(this, "Gagal: ${result.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
