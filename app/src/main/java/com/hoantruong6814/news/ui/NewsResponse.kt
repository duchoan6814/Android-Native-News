package com.hoantruong6814.news.ui

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)