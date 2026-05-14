package com.example.quiz.network

import retrofit2.http.GET
import retrofit2.http.Query

interface GNewsApiService {
    @GET("v1/latest-news")
    suspend fun getLatestNews(
        @Query("language") language: String = "en",
        @Query("country") country: String,
        @Query("page_size") pageSize: Int = 10,
        @Query("apiKey") apiKey: String
    ): CurrentsResponse
}

