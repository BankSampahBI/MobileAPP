package com.example.banksampah.ui.autentikasi.register

import androidx.lifecycle.ViewModel
import com.example.banksampah.data.Repository

class RegisterViewModel(private val repository: Repository) : ViewModel() {
    fun register(name: String, email: String, password: String, role: String) =
        repository.register(name, email, password,role)
}