package com.example.banksampah.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.banksampah.data.pref.UserPreference
import com.example.banksampah.data.remote.response.LoginResponse
import com.example.banksampah.data.remote.response.RegisterResponse
import com.example.banksampah.data.remote.retrofit.ApiService
import com.example.banksampah.ui.Model.UserModel
import kotlinx.coroutines.flow.Flow

class Repository(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> = userPreference.getSession()

    suspend fun logout() = userPreference.logout()

    fun register(
        name: String,
        email: String,
        password: String,
        role: String
    ): LiveData<Result<RegisterResponse>> = liveData {
        try {
            val response = apiService.register(name, email, password, role)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d("Register", e.message.toString())
            emit(Result.Error(e.message.toString()))
        }
    }

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        try {
            val response = apiService.login(email, password)
            val userModel = UserModel(email, response.token, true)
            saveSession(userModel)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d("Login", e.message.toString())
            emit(Result.Error(e.message.toString()))
        }
    }


    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(userPreference, apiService)
            }.also { instance = it }
    }
}
