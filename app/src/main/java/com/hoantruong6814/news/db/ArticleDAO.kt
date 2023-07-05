package com.hoantruong6814.news.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hoantruong6814.news.model.Article

@Dao
interface ArticleDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long;

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>;

    @Delete
    suspend fun deleteArticle(article: Article);

}