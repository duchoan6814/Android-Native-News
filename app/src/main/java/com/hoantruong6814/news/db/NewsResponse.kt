package com.hoantruong6814.news.db

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)