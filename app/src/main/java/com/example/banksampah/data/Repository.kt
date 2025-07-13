package com.example.banksampah.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.banksampah.data.pref.UserPreference
import com.example.banksampah.data.remote.Request.BeliLangsungRequest
import com.example.banksampah.data.remote.Request.UpdateCartRequest
import com.example.banksampah.data.remote.response.BeliLangsungResponse
import com.example.banksampah.data.remote.response.BeritaItem
import com.example.banksampah.data.remote.response.CartResponse
import com.example.banksampah.data.remote.response.DataBarangResponse
import com.example.banksampah.data.remote.response.DataItem
import com.example.banksampah.data.remote.response.DataItemRiwayat
import com.example.banksampah.data.remote.response.DataPenarikan
import com.example.banksampah.data.remote.response.LoginResponse
import com.example.banksampah.data.remote.response.PembelianDetailItem
import com.example.banksampah.data.remote.response.PenarikanResponse
import com.example.banksampah.data.remote.response.PenjualanResponseItem
import com.example.banksampah.data.remote.response.RegisterResponse
import com.example.banksampah.data.remote.response.RiwayatPembelianResponse
import com.example.banksampah.data.remote.response.RiwayatPenjualanResponse
import com.example.banksampah.data.remote.response.SaldoResponse
import com.example.banksampah.data.remote.response.SetoranResponse
import com.example.banksampah.data.remote.response.UpdateCartResponse
import com.example.banksampah.data.remote.retrofit.ApiConfig
import com.example.banksampah.data.remote.retrofit.ApiService
import com.example.banksampah.ui.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import kotlinx.coroutines.flow.first

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

    fun addToCart(token: String, penjualanId: Int, jumlah: Int): LiveData<Result<String>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.addToCart("Bearer $token", penjualanId, jumlah)
            emit(Result.Success(response.message))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Gagal menambahkan ke keranjang"))
        }
    }

    suspend fun getCart(token: String): CartResponse {
        return apiService.getCartItems("Bearer $token")
    }

    suspend fun updateCartItem(token: String, barangId: Int, jumlah: Int): retrofit2.Response<UpdateCartResponse> {
        val request = UpdateCartRequest(jumlah)
        return withContext(Dispatchers.IO) {
            apiService.updateCartItem("Bearer $token", barangId, request)
        }
    }

    suspend fun checkoutCart(token: String): String {
        val response = apiService.checkoutCart("Bearer $token")
        return response.message
    }

    fun beliLangsung(token: String, penjualanId: Int, jumlah: Int): LiveData<Result<BeliLangsungResponse>> = liveData {
        emit(Result.Loading)
        try {
            val request = BeliLangsungRequest(penjualanId, jumlah)
            val response = apiService.beliLangsung("Bearer $token", request)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Gagal melakukan pembelian langsung"))
        }
    }

    suspend fun getRiwayatPembelian(token: String): List<DataItemRiwayat>? {
        return try {
            val response = apiService.getRiwayatPembelian(token)
            if (response.isSuccessful) {
                response.body()?.data
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getDetailPembelian(token: String, pembelianId: Int): DataItemRiwayat? {
        return try {
            val response = apiService.getDetailPembelian("Bearer $token", pembelianId)
            if (response.isSuccessful) {
                response.body()?.data // ✅ cukup ambil langsung objeknya
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun batalPembelian(token: String, id: Int): Boolean {
        return try {
            val response = apiService.batalPembelian("Bearer $token", id)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getRiwayatPenjualan(token: String): RiwayatPenjualanResponse {
        return ApiConfig.getApiService().getRiwayatPenjualan(token)
    }

    suspend fun getRiwayatSetoran(token: String): Result<SetoranResponse> {
        return try {
            val response = apiService.getRiwayatSetoran(token)
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Gagal ambil riwayat")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Terjadi kesalahan")
        }
    }

    suspend fun getSaldo(token: String): SaldoResponse {
        return apiService.getSaldo(token)
    }

    suspend fun tambahPenarikan(jumlah: Int): PenarikanResponse? {
        val token = userPreference.getSession().first().token
        return try {
            apiService.tambahPenarikan("Bearer $token", jumlah)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getRiwayatPenarikan(): List<DataPenarikan>? {
        val token = userPreference.getSession().first().token
        val response = apiService.getRiwayatPenarikan("Bearer $token")
        return if (response.isSuccessful) response.body()?.data else null
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
