package com.example.banksampah.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.banksampah.data.Repository
import com.example.banksampah.data.Result
import com.example.banksampah.data.remote.response.BeritaItem
import com.example.banksampah.data.remote.response.DataItem
import com.example.banksampah.ui.model.UserModel
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository) : ViewModel() {

    private val _saldo = MutableLiveData<Int>()
    val saldo: LiveData<Int> = _saldo

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
    fun getKatalog(): LiveData<Result<List<DataItem>>> {
        return repository.getKatalog()
    }
    fun getBerita(): LiveData<Result<List<BeritaItem>>> {
        return repository.getBerita()
    }
    fun getSaldo(token: String) {
        viewModelScope.launch {
            try {
                val response = repository.getSaldo(token)
                _saldo.value = response.saldo
            } catch (e: Exception) {
                Log.e("HomeViewModel", "getSaldo: ${e.message}")
                _saldo.value = 0
            }
        }
    }
}