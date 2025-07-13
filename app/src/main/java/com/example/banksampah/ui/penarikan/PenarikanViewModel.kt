package com.example.banksampah.ui.penarikan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.banksampah.data.Repository
import com.example.banksampah.data.Result
import com.example.banksampah.data.remote.response.PenarikanResponse
import com.example.banksampah.data.remote.response.SaldoResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PenarikanViewModel(private val repository: Repository) : ViewModel() {

    private val _saldo = MutableLiveData<Result<SaldoResponse>>()
    val saldo: LiveData<Result<SaldoResponse>> get() = _saldo

    private val _penarikanResult = MutableLiveData<Result<String>>()
    val penarikanResult: LiveData<Result<String>> get() = _penarikanResult

    fun getSaldo() {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                try {
                    val result = repository.getSaldo(user.token)
                    _saldo.postValue(Result.Success(result))
                } catch (e: Exception) {
                    _saldo.postValue(Result.Error(e.message ?: "Gagal mengambil saldo"))
                }
            }
        }
    }

    fun ajukanPenarikan(jumlah: Int) {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                try {
                    val result = repository.tambahPenarikan(jumlah)
                    if (result != null) {
                        _penarikanResult.postValue(Result.Success(result.message))
                    } else {
                        _penarikanResult.postValue(Result.Error("Gagal mengajukan penarikan"))
                    }
                } catch (e: Exception) {
                    _penarikanResult.postValue(Result.Error(e.message ?: "Terjadi kesalahan"))
                }
            }
        }
    }
}

