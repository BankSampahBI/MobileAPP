package com.example.banksampah.data.di

import android.content.Context
import com.example.banksampah.data.Repository
import com.example.banksampah.data.pref.UserPreference
import com.example.banksampah.data.pref.dataStore
import com.example.banksampah.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService() // tanpa token
        return Repository.getInstance(pref, apiService)
    }

    fun provideRepository(context: Context, token: String): Repository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService(token) // pakai token
        return Repository(pref, apiService) // bypass singleton
    }

    fun provideUserPreference(context: Context): UserPreference {
        return UserPreference.getInstance(context.dataStore)
    }
}
