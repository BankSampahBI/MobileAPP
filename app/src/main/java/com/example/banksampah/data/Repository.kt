package com.example.banksampah.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.banksampah.data.pref.UserPreference
import com.example.banksampah.data.remote.response.BeritaItem
import com.example.banksampah.data.remote.response.DataBarangResponse
import com.example.banksampah.data.remote.response.DataItem
import com.example.banksampah.data.remote.response.LoginResponse
import com.example.banksampah.data.remote.response.PenjualanResponseItem
import com.example.banksampah.data.remote.response.RegisterResponse
import com.example.banksampah.data.remote.retrofit.ApiConfig
import com.example.banksampah.data.remote.retrofit.ApiService
import com.example.banksampah.ui.model.UserModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Response
import java.io.File

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
            val userModel = UserModel(
                name = response.user.name,
                email = response.user.email,
                token = "Bearer ${response.token}",
                isLogin = true
            )
            saveSession(userModel)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d("Login", e.message.toString())
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getKatalog(): LiveData<Result<List<DataItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getKatalog()
            emit(Result.Success(response.data))
        } catch (e: Exception) {
            Log.e("Katalog", "Error: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getBerita(): LiveData<Result<List<BeritaItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getBerita()
            emit(Result.Success(response.data))
        } catch (e: Exception) {
            Log.e("Berita", "Error: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getPenjualan(token: String): LiveData<Result<List<PenjualanResponseItem>>> = liveData {
        emit(Result.Loading)
        try {
            val apiServiceWithToken = ApiConfig.getApiService(token)
            val response = apiServiceWithToken.getPenjualan()
            emit(Result.Success(response.data))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun submitPenjualan(
        token: String,
        namaBarang: String,
        deskripsi: String,
        stok: Int,
        harga: Int,
        foto: File?
    ): LiveData<Result<Any>> = liveData {
        emit(Result.Loading)
        try {
            val namaBarangPart = namaBarang.toRequestBody("text/plain".toMediaTypeOrNull())
            val deskripsiPart = deskripsi.toRequestBody("text/plain".toMediaTypeOrNull())
            val stokPart = stok.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val hargaPart = harga.toString().toRequestBody("text/plain".toMediaTypeOrNull())

            val fotoPart = foto?.let {
                val requestImageFile = it.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("foto", it.name, requestImageFile)
            }

            val apiServiceWithToken = ApiConfig.getApiService(token)
            val response = apiServiceWithToken.submitPenjualan(
                namaBarang = namaBarangPart,
                deskripsi = deskripsiPart,
                stok = stokPart,
                harga = hargaPart,
                foto = fotoPart
            )
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Gagal submit penjualan"))
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
    ): LiveData<Result<Any>> = liveData {
        emit(Result.Loading)
        try {
            val methodPart = "PUT".toRequestBody("text/plain".toMediaTypeOrNull())
            val namaPart = nama.toRequestBody("text/plain".toMediaTypeOrNull())
            val deskripsiPart = deskripsi.toRequestBody("text/plain".toMediaTypeOrNull())
            val stokPart = stok.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val hargaPart = harga.toString().toRequestBody("text/plain".toMediaTypeOrNull())

            val fotoPart = foto?.let {
                val reqFile = it.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("foto", it.name, reqFile)
            }

            val apiServiceWithToken = ApiConfig.getApiService(token)

            val response = apiServiceWithToken.updatePenjualan(
                id = id,
                method = methodPart,  // <- ini penting
                nama = namaPart,
                deskripsi = deskripsiPart,
                stok = stokPart,
                harga = hargaPart,
                foto = fotoPart
            )
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Gagal update penjualan"))
        }
    }

    suspend fun deletePenjualan(token: String, id: Int): Result<String> {
        return try {
            val response = apiService.deletePenjualan("Bearer $token", id)
            if (response.isSuccessful) {
                val message = JSONObject(response.body()?.string() ?: "").optString("message", "Berhasil dihapus")
                Result.Success(message)
            } else {
                Result.Error(response.message())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Terjadi kesalahan")
        }
    }

    suspend fun getBarangValid(token: String): Response<DataBarangResponse> {
        return apiService.getPenjualanValid(token)
    }



    companion object {
        @Volatile
        private var instance: Repository? = null

        fun getInstance(userPreference: UserPreference, apiService: ApiService): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(userPreference, apiService)
            }.also { instance = it }
    }
}
