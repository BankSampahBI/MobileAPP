package com.example.banksampah.ui.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.banksampah.data.Repository
import com.example.banksampah.data.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailCartViewModel(private val repository: Repository) : ViewModel() {

    private val _updateResult = MutableLiveData<Result<String>>()
    val updateResult: LiveData<Result<String>> get() = _updateResult

    fun updateCart(token: String, cartItemId: Int, jumlah: Int): LiveData<Result<String>> {
        viewModelScope.launch(Dispatchers.IO) {
            _updateResult.postValue(Result.Loading)
            try {
                val response = repository.updateCartItem(token, cartItemId, jumlah)
                if (response.isSuccessful) {
                    _updateResult.postValue(Result.Success("Jumlah berhasil diupdate"))
                } else {
                    _updateResult.postValue(Result.Error("Gagal update jumlah"))
                }
            } catch (e: Exception) {
                _updateResult.postValue(Result.Error("Error: ${e.message}"))
            }
        }
        return _updateResult
    }

    fun getSession() = repository.getSession()
}