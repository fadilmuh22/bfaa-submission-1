package com.example.bfaasubmission1.data.remote.retrofit

import com.example.bfaasubmission1.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        private const val BASE_URL = BuildConfig.BASE_URL

        fun getApiService(): ApiService {
            val loggingInterceptor =
                HttpLoggingInterceptor().apply {
                    level =
                        if (BuildConfig.DEBUG) {
                            HttpLoggingInterceptor.Level.BODY
                        } else {
                            HttpLoggingInterceptor.Level.NONE
                        }
                }
            val client =
                OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor { chain ->
                        val newRequest =
                            chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer ${BuildConfig.API_KEY}")
                                .build()
                        chain.proceed(newRequest)
                    }
                    .build()
            val retrofit =
                Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}
