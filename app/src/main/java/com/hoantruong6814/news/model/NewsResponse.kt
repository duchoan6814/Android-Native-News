package com.hoantruong6814.news.db

import com.hoantruong6814.news.model.Article

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)