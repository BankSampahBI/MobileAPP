package com.example.banksampah.data.remote.retrofit

import com.example.banksampah.data.remote.response.KatalogResponse
import com.example.banksampah.data.remote.response.LoginResponse
import com.example.banksampah.data.remote.response.RegisterResponse
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("api/register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("role") role: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("api/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("api/katalog")
    suspend fun getKatalog(): KatalogResponse
}
