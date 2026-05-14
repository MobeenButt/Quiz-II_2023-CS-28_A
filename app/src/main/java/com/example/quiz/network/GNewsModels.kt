package com.example.quiz.network

import com.google.gson.annotations.SerializedName

data class CurrentsResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("news")
    val news: List<CurrentsArticleDto>
)

data class CurrentsArticleDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("url")
    val url: String,
    @SerializedName("author")
    val author: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("language")
    val language: String,
    @SerializedName("category")
    val category: List<String>,
    @SerializedName("published")
    val published: String
)

