package com.example.banksampah.ui.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.banksampah.data.Repository
import com.example.banksampah.data.remote.response.DataItemRiwayatPenjualan
import kotlinx.coroutines.launch

class RiwayatPenjualanViewModel(private val repository: Repository) : ViewModel() {

    private val _riwayat = MutableLiveData<List<DataItemRiwayatPenjualan>>()
    val riwayat: LiveData<List<DataItemRiwayatPenjualan>> = _riwayat

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getRiwayat(token: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getRiwayatPenjualan("Bearer $token")
                _riwayat.value = response.data
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
