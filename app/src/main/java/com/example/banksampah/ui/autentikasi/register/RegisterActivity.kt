package com.example.banksampah.ui.autentikasi.register

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.banksampah.data.Result
import com.example.banksampah.databinding.ActivityRegisterBinding
import com.example.banksampah.ui.Model.ViewModelFactory
import com.example.banksampah.ui.autentikasi.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        setupView()
        setupSpinner()
        setupAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupSpinner() {
        val roles = listOf("Pilih peran", "nasabah", "konsumen")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerRole.adapter = adapter
    }

    private fun setupAction() {
        binding.btnDaftar.setOnClickListener {
            val name = binding.inputUsername.text.toString().trim()
            val email = binding.inputEmail.text.toString().trim()
            val password = binding.inputPassword.text.toString().trim()
            val role = binding.spinnerRole.selectedItem.toString() // ⬅ Ambil nilai dari Spinner
            Log.d("RegisterDebug", "Role terpilih: $role")
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || role == "Pilih peran") {
                Toast.makeText(this, "Semua kolom wajib diisi dan peran harus dipilih", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registerViewModel.register(name, email, password, role).observe(this) { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.progresBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progresBar.visibility = View.GONE
                        AlertDialog.Builder(this).apply {
                            setTitle("Yeay!")
                            setMessage("Akun dengan email $email berhasil dibuat. Silahkan login!")
                            setPositiveButton("OK") { _, _ ->
                                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            create()
                            show()
                        }
                    }

                    is Result.Error -> {
                        binding.progresBar.visibility = View.GONE
                        Toast.makeText(this, "Register Gagal", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
