package com.example.banksampah.ui.autentikasi.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.banksampah.MainActivity
import com.example.banksampah.data.Result
import com.example.banksampah.databinding.ActivityLoginBinding
import com.example.banksampah.ui.Model.UserModel
import com.example.banksampah.ui.Model.ViewModelFactory
import com.example.banksampah.ui.autentikasi.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val AUTH = "Bearer "
    }

    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.tvDaftar.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
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

    private fun setupAction() {
        binding.btnMasuk.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()

            loginViewModel.login(email, password).observe(this) { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.progresBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progresBar.visibility = View.GONE
                        val token = AUTH + result.data.token
                        val userModel = UserModel(email, token, true)
                        loginViewModel.saveSession(userModel)

                        AlertDialog.Builder(this).apply {
                            setTitle("Yeah!")
                            setMessage("Login Berhasil")
                            setPositiveButton("Continue") { _, _ ->
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }
                            create()
                            show()
                        }
                    }

                    is Result.Error -> {
                        binding.progresBar.visibility = View.GONE
                        Toast.makeText(this, "Login Gagal", Toast.LENGTH_SHORT).show()
                    }

                    else -> {}
                }
            }
        }
    }
}
