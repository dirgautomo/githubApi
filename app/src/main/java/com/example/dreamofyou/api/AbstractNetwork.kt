package com.example.dreamofyou.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

abstract class AbstractNetwork {

    private val interceptor = Interceptor { chain ->
        val url = chain.request().url().newBuilder().build()
        val request = chain.request()
            .newBuilder()
            .url(url)
            .build()
        chain.proceed(request)
    }

    private val logging = HttpLoggingInterceptor()

    private val apiClient =
        OkHttpClient().newBuilder()
            .addInterceptor(logging.setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(interceptor).build()

    protected fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .client(apiClient)
            .baseUrl(getBaseUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    abstract fun getBaseUrl(): String

}