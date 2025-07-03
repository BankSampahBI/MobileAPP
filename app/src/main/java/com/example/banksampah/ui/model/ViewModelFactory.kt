package com.example.banksampah.ui.model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.banksampah.data.Repository
import com.example.banksampah.data.pref.UserPreference
import com.example.banksampah.data.di.Injection
import com.example.banksampah.ui.autentikasi.login.LoginViewModel
import com.example.banksampah.ui.autentikasi.register.RegisterViewModel
import com.example.banksampah.ui.home.HomeViewModel
import com.example.banksampah.ui.profile.ProfileViewModel

class ViewModelFactory(
    private val repository: Repository,
    private val userPreference: UserPreference
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

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    val repo = Injection.provideRepository(context)
                    val pref = Injection.provideUserPreference(context)
                    INSTANCE = ViewModelFactory(repo, pref)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}
