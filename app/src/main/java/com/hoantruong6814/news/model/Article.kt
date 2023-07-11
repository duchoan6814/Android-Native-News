package com.hoantruong6814.news.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hoantruong6814.news.model.Source

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
): java.io.Serializable {
    override fun hashCode(): Int {
        var result = id.hashCode()
        if(url.isNullOrEmpty()){
            result = 31 * result + url.hashCode()
        }
        return result
    }
};