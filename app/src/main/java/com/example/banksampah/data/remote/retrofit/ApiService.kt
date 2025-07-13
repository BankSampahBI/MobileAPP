package com.example.banksampah.data.remote.retrofit

import com.example.banksampah.data.remote.Request.BeliLangsungRequest
import com.example.banksampah.data.remote.Request.UpdateCartRequest
import com.example.banksampah.data.remote.response.BeliLangsungResponse
import com.example.banksampah.data.remote.response.BeritaResponse
import com.example.banksampah.data.remote.response.CartResponse
import com.example.banksampah.data.remote.response.DataBarangResponse
import com.example.banksampah.data.remote.response.DataItemRiwayat
import com.example.banksampah.data.remote.response.DataPenarikan
import com.example.banksampah.data.remote.response.DetailPembelianResponse
import com.example.banksampah.data.remote.response.KatalogResponse
import com.example.banksampah.data.remote.response.LoginResponse
import com.example.banksampah.data.remote.response.PenarikanResponse
import com.example.banksampah.data.remote.response.PenjualanResponse
import com.example.banksampah.data.remote.response.RegisterResponse
import com.example.banksampah.data.remote.response.RiwayatPembelianResponse
import com.example.banksampah.data.remote.response.RiwayatPenarikanResponse
import com.example.banksampah.data.remote.response.RiwayatPenjualanResponse
import com.example.banksampah.data.remote.response.SaldoResponse
import com.example.banksampah.data.remote.response.SetoranResponse
import com.example.banksampah.data.remote.response.UpdateCartResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("role") role: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("katalog")
    suspend fun getKatalog(): KatalogResponse

    @GET("berita")
    suspend fun getBerita(): BeritaResponse

    @GET("penjualan")
    suspend fun getPenjualan(): PenjualanResponse

    @Multipart
    @POST("penjualan")
    suspend fun submitPenjualan(
        @Part("nama_barang") namaBarang: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part("stok") stok: RequestBody,
        @Part("harga") harga: RequestBody,
        @Part foto: MultipartBody.Part?
    ): Any

    @Multipart
    @POST("penjualan/{id}")
    suspend fun updatePenjualan(
        @Path("id") id: Int,
        @Part("_method") method: RequestBody = "PUT".toRequestBody("text/plain".toMediaTypeOrNull()),
        @Part("nama_barang") nama: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part("stok") stok: RequestBody,
        @Part("harga") harga: RequestBody,
        @Part foto: MultipartBody.Part? = null
    ): Any

    @DELETE("penjualan/{id}")
    suspend fun deletePenjualan(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<ResponseBody>

    @GET("penjualan/valid")
    suspend fun getPenjualanValid(
        @Header("Authorization") token: String
    ): Response<DataBarangResponse>

    @FormUrlEncoded
    @POST("cart/store")
    suspend fun addToCart(
        @Header("Authorization") token: String,
        @Field("penjualan_id") penjualanId: Int,
        @Field("jumlah") jumlah: Int
    ): CartResponse


    @GET("cart")
    suspend fun getCartItems(
        @Header("Authorization") token: String
    ): CartResponse

    @PUT("cart/{id}")
    suspend fun updateCartItem(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body request: UpdateCartRequest
    ): Response<UpdateCartResponse>

    @POST("cart/checkout")
    suspend fun checkoutCart(
        @Header("Authorization") token: String
    ): CartResponse

    @POST("beli-langsung")
    suspend fun beliLangsung(
        @Header("Authorization") token: String,
        @Body request: BeliLangsungRequest
    ): BeliLangsungResponse

    @GET("pembelian")
    suspend fun getRiwayatPembelian(
        @Header("Authorization") token: String
    ): Response<RiwayatPembelianResponse>

    @GET("pembelian/{id}")
    suspend fun getDetailPembelian(
        @Header("Authorization") token: String,
        @Path("id") pembelianId: Int
    ): Response<DetailPembelianResponse>

    @DELETE("pembelian/{id}")
    suspend fun batalPembelian(
        @Header("Authorization") token: String,
        @Path("id") pembelianId: Int
    ): Response<ResponseBody>

    @GET("penjualan/riwayat-penjualan")
    suspend fun getRiwayatPenjualan(
        @Header("Authorization") token: String,
    ): RiwayatPenjualanResponse

    @GET("setor-sampah/riwayat")
    suspend fun getRiwayatSetoran(
        @Header("Authorization") token: String,
    ): Response<SetoranResponse>

    @GET("saldo")
    suspend fun getSaldo(
        @Header("Authorization") token: String
    ): SaldoResponse

    @FormUrlEncoded
    @POST("penarikan")
    suspend fun tambahPenarikan(
        @Header("Authorization") token: String,
        @Field("jumlah") jumlah: Int
    ): PenarikanResponse

    @GET("penarikan")
    suspend fun getRiwayatPenarikan(
        @Header("Authorization") token: String
    ): Response<RiwayatPenarikanResponse>

}
