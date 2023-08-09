package com.hoantruong6814.news.model

import com.hoantruong6814.news.model.Article

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)