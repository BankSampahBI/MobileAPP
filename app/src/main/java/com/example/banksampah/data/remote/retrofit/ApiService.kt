package com.example.banksampah.data.remote.retrofit

import com.example.banksampah.data.remote.response.BeritaResponse
import com.example.banksampah.data.remote.response.DataBarangResponse
import com.example.banksampah.data.remote.response.KatalogResponse
import com.example.banksampah.data.remote.response.LoginResponse
import com.example.banksampah.data.remote.response.PenjualanResponse
import com.example.banksampah.data.remote.response.RegisterResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
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

}
