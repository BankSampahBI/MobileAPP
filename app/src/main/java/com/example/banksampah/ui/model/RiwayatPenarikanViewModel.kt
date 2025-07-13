package com.example.banksampah.ui.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.banksampah.data.Repository
import com.example.banksampah.data.remote.response.DataPenarikan
import kotlinx.coroutines.launch

class RiwayatPenarikanViewModel(private val repository: Repository) : ViewModel() {

    private val _riwayat = MutableLiveData<List<DataPenarikan>>()
    val riwayat: LiveData<List<DataPenarikan>> = _riwayat

    fun getRiwayat() {
        viewModelScope.launch {
            val result = repository.getRiwayatPenarikan()
            _riwayat.value = result ?: emptyList()
        }
    }
}
