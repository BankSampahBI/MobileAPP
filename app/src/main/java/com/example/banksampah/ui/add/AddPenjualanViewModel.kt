package com.example.banksampah.ui.model

import androidx.lifecycle.ViewModel
import com.example.banksampah.data.Repository
import java.io.File

class AddPenjualanViewModel(
    private val repository: Repository
) : ViewModel() {

    fun submitPenjualan(
        token: String,
        namaBarang: String,
        deskripsi: String,
        stok: Int,
        harga: Int,
        foto: File?
    ) = repository.submitPenjualan(token, namaBarang, deskripsi, stok, harga, foto)

}
