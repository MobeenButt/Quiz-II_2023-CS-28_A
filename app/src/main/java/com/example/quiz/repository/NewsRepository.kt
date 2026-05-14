package com.example.quiz.repository

import com.example.quiz.BuildConfig
import com.example.quiz.model.NewsArticle
import com.example.quiz.network.NetworkModule

class NewsRepository {

    suspend fun getTopHeadlines(countryCode: String): List<NewsArticle> {
        val response = NetworkModule.gNewsApiService.getLatestNews(
            country = countryCode,
            apiKey = BuildConfig.GNEWS_API_KEY
        )

        return response.news.map { dto ->
            NewsArticle(
                title = dto.title,
                description = dto.description,
                content = null,                          // Currents API has no separate content field
                image = dto.image?.takeIf { it != "None" },
                publishedAt = dto.published,
                sourceName = dto.author ?: "Unknown",
                url = dto.url
            )
        }
    }
}

