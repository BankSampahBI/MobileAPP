package com.example.banksampah.ui.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.banksampah.data.Repository
import com.example.banksampah.data.Result
import com.example.banksampah.data.remote.response.Data
import kotlinx.coroutines.launch

class RiwayatPenyetoranViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _riwayatSetoran = MutableLiveData<List<Data>>() // ambil dari API response
    val riwayatSetoran: LiveData<List<Data>> = _riwayatSetoran

    fun fetchRiwayatSetoran(token: String) {
        viewModelScope.launch {
            when (val result = repository.getRiwayatSetoran(token)) {
                is Result.Success -> {
                    _riwayatSetoran.value = result.data?.data ?: emptyList()
                }
                is Result.Error -> {
                    Log.e("RiwayatSetoran", "Gagal ambil data: ${result.message}")
                }
                is Result.Loading -> {
                    // Optional: show loading
                }
            }
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}


