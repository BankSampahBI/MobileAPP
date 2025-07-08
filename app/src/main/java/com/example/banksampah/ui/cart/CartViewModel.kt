package com.example.banksampah.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.banksampah.data.Repository
import com.example.banksampah.data.Result
import com.example.banksampah.data.remote.response.PenjualanResponseItem
import com.example.banksampah.ui.model.UserModel

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
}


