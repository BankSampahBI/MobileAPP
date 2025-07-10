package com.example.banksampah.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.banksampah.data.Repository
import com.example.banksampah.data.remote.response.PenjualanKonsumenItem
import com.example.banksampah.ui.model.UserModel
import kotlinx.coroutines.launch

class KonsumenHomeViewModel(private val repository: Repository) : ViewModel() {

    private val _barangList = MutableLiveData<List<PenjualanKonsumenItem>>()
    val barangList: LiveData<List<PenjualanKonsumenItem>> = _barangList

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getDataBarang(token: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.getBarangValid(token)
                if (response.isSuccessful) {
                    _barangList.postValue(response.body()?.data ?: emptyList())
                } else {
                    _errorMessage.postValue("Gagal mengambil data barang")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Error: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}
