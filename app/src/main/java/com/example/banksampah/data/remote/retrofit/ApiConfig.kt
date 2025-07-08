package com.example.banksampah.data.remote.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        fun getApiService(token: String? = null): ApiService {
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            val clientBuilder = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor { chain ->
                    val requestBuilder = chain.request().newBuilder()
                        .addHeader("Accept", "application/json")

                    if (token != null) {
                        requestBuilder.addHeader("Authorization", "Bearer $token")
                    }

                    chain.proceed(requestBuilder.build())
                }

            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.18.195:8000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(clientBuilder.build())
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}


