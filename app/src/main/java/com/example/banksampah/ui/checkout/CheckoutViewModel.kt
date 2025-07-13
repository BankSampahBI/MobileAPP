package com.example.banksampah.ui.checkout

import androidx.lifecycle.*
import com.example.banksampah.data.Repository
import com.example.banksampah.data.Result
import com.example.banksampah.data.remote.response.CartItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class CheckoutViewModel(private val repository: Repository) : ViewModel() {

    private val _cartItems = MutableLiveData<List<CartItem>>()
    val cartItems: LiveData<List<CartItem>> get() = _cartItems

    private val _totalPrice = MutableLiveData<Int>()
    val totalPrice: LiveData<Int> get() = _totalPrice

    private var token: String = ""

    fun loadCart(items: List<CartItem>) {
        _cartItems.value = items
        calculateTotal(items)
    }

    private fun calculateTotal(items: List<CartItem>) {
        val total = items.sumOf { it.jumlah * it.penjualan.harga }
        _totalPrice.value = total
    }

    fun setToken(token: String) {
        this.token = token
    }

    fun checkout(): Flow<Result<String>> = flow {
        emit(Result.Loading)
        try {
            val result = repository.checkoutCart(token)
            emit(Result.Success("Checkout berhasil"))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Terjadi kesalahan"))
        }
    }
}
