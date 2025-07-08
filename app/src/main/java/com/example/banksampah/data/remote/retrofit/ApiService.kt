package com.example.banksampah.data.remote.retrofit

import com.example.banksampah.data.remote.response.BeritaResponse
import com.example.banksampah.data.remote.response.KatalogResponse
import com.example.banksampah.data.remote.response.LoginResponse
import com.example.banksampah.data.remote.response.PenjualanResponse
import com.example.banksampah.data.remote.response.PenjualanResponseItem
import com.example.banksampah.data.remote.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

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
}
