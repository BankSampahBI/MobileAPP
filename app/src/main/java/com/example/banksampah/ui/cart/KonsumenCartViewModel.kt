package com.example.banksampah.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.banksampah.data.Repository
import com.example.banksampah.data.remote.response.CartItem
import kotlinx.coroutines.launch

class KonsumenCartViewModel (private val repository: Repository) : ViewModel() {

    private val _cartItems = MutableLiveData<List<CartItem>>()
    val cartItems: LiveData<List<CartItem>> = _cartItems

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getSession() = repository.getSession().asLiveData()


    fun getCart(token: String) {
        viewModelScope.launch {
            try {
                val response = repository.getCart(token)
                _cartItems.value = response.data
            } catch (e: Exception) {
                _error.value = e.message ?: "Terjadi kesalahan"
            }
        }
    }
}