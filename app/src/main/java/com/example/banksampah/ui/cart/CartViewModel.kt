package com.example.banksampah.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.banksampah.data.Repository
import com.example.banksampah.data.Result
import com.example.banksampah.data.remote.response.PenjualanResponseItem
import com.example.banksampah.ui.model.UserModel
import kotlinx.coroutines.launch
import java.io.File

class CartViewModel(private val repository: Repository) : ViewModel() {

    private val _penjualan = MutableLiveData<Result<List<PenjualanResponseItem>>>()
    val penjualan: LiveData<Result<List<PenjualanResponseItem>>> = _penjualan

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun loadPenjualan(token: String) {
        repository.getPenjualan(token).observeForever {
            _penjualan.value = it
        }
    }
    fun updatePenjualan(
        token: String,
        id: Int,
        nama: String,
        deskripsi: String,
        stok: Int,
        harga: Int,
        foto: File?
    ): LiveData<Result<Any>> {
        return repository.updatePenjualan(token, id, nama, deskripsi, stok, harga, foto)
    }
    fun deletePenjualan(token: String, id: Int): LiveData<Result<String>> {
        val resultLiveData = MutableLiveData<Result<String>>()
        viewModelScope.launch {
            resultLiveData.value = repository.deletePenjualan(token, id)
        }
        return resultLiveData
    }


}


