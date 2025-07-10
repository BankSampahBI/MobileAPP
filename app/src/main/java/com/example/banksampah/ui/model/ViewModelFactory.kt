package com.example.banksampah.ui.model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.banksampah.data.Repository
import com.example.banksampah.data.pref.UserPreference
import com.example.banksampah.data.di.Injection
import com.example.banksampah.ui.autentikasi.login.LoginViewModel
import com.example.banksampah.ui.autentikasi.register.RegisterViewModel
import com.example.banksampah.ui.cart.CartViewModel
import com.example.banksampah.ui.cart.KonsumenCartViewModel
import com.example.banksampah.ui.home.HomeViewModel
import com.example.banksampah.ui.home.KonsumenHomeViewModel
import com.example.banksampah.ui.profile.ProfileViewModel

class ViewModelFactory(
    private val repository: Repository

) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }

            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }

            modelClass.isAssignableFrom(CartViewModel::class.java) -> {
                CartViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AddPenjualanViewModel::class.java) -> {
                AddPenjualanViewModel(repository) as T
            }
            modelClass.isAssignableFrom(KonsumenHomeViewModel::class.java) -> {
                KonsumenHomeViewModel(repository) as T
            }
//            modelClass.isAssignableFrom(KonsumenCartViewModel::class.java) -> {
//                KonsumenCartViewModel(repository) as T
//            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                val repo = Injection.provideRepository(context)
                val pref = Injection.provideUserPreference(context)
                ViewModelFactory(repo).also { INSTANCE = it }
            }
        }
    }
}
