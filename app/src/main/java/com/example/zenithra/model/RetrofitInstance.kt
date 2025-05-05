package com.example.zenithra.model

import com.example.zenithra.App
import com.example.zenithra.model.JikanApiService
import com.example.zenithra.mangaScreen.MangaDao
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Log both request and response
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging) // Add logging interceptor here
        .build()

    val api: JikanApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.jikan.moe/v4/")
            .client(client)  // Set OkHttpClient with logging
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(JikanApiService::class.java)
    }

    val dao: MangaDao by lazy {
        App.Companion.instance.appDatabase.mangaDao()
    }
}