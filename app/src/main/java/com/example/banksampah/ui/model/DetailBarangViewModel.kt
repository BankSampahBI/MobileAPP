package com.example.banksampah.ui.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.banksampah.data.Repository
import com.example.banksampah.data.Result
import com.example.banksampah.data.remote.response.BeliLangsungResponse
import com.example.banksampah.ui.model.UserModel
import kotlinx.coroutines.flow.Flow

class DetailBarangViewModel(private val repository: Repository) : ViewModel() {

    fun addToCart(token: String, penjualanId: Int, jumlah: Int): LiveData<Result<String>> {
        return repository.addToCart(token, penjualanId, jumlah)
    }

    fun beliLangsung(token: String, penjualanId: Int, jumlah: Int): LiveData<Result<BeliLangsungResponse>> {
        return repository.beliLangsung(token, penjualanId, jumlah)
    }

    fun getSession(): Flow<UserModel> = repository.getSession()
}

