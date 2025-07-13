package com.example.banksampah.ui.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.banksampah.data.Repository
import com.example.banksampah.data.remote.response.DataItemRiwayat
import kotlinx.coroutines.launch

class PembelianViewModel(private val repository: Repository) : ViewModel() {

    private val _riwayat = MutableLiveData<List<DataItemRiwayat>>()
    val riwayat: LiveData<List<DataItemRiwayat>> = _riwayat

    fun fetchRiwayat(token: String) {
        viewModelScope.launch {
            val result = repository.getRiwayatPembelian(token)
            _riwayat.value = result ?: emptyList()
        }
    }
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
    private val _detailPembelian = MutableLiveData<DataItemRiwayat?>()
    val detailPembelian: LiveData<DataItemRiwayat?> = _detailPembelian

    fun fetchDetailPembelian(token: String, pembelianId: Int) {
        viewModelScope.launch {
            val result = repository.getDetailPembelian(token, pembelianId)
            if (result != null) {
                _detailPembelian.value = result
            } else {
                Log.e("DetailPembelian", "Data tidak ditemukan atau gagal load")
            }
        }
    }

    private val _batalStatus = MutableLiveData<Boolean>()
    val batalStatus: LiveData<Boolean> = _batalStatus

    fun batalPembelian(token: String, id: Int) {
        viewModelScope.launch {
            val success = repository.batalPembelian(token, id)
            _batalStatus.value = success
        }
    }
}
