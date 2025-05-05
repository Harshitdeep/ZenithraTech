package com.example.zenithra.model

import com.example.zenithra.mangaScreen.MangaDetailResponse
import com.example.zenithra.mangaScreen.MangaResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JikanApiService {
    @GET("top/manga")
    suspend fun getTopManga(@Query("page") page: Int): MangaResponse

    @GET("manga/{id}")
    suspend fun getMangaDetails(@Path("id") id: String?): MangaDetailResponse
}