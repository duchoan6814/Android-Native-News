package com.hoantruong6814.news.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("articles")
data class Article(
    @PrimaryKey(true)
    var id: Int? = null,
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String
)