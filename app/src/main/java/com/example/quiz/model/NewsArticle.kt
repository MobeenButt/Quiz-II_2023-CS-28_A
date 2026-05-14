package com.example.quiz.model

import java.io.Serializable

data class NewsArticle(
    val title: String,
    val description: String?,
    val content: String?,
    val image: String?,
    val publishedAt: String,
    val sourceName: String,
    val url: String
) : Serializable
