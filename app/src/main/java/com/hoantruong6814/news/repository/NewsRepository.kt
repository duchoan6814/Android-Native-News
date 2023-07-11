package com.hoantruong6814.news.repository

import com.hoantruong6814.news.api.RetrofitInstance
import com.hoantruong6814.news.db.ArticleDatabase
import com.hoantruong6814.news.model.Article

class NewsRepository(val db: ArticleDatabase) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber);

    suspend fun getSearchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber);


    suspend fun upsert(article: Article) =
        db.getArticleDao().upsert(article);

    suspend fun delete(article: Article) = db.getArticleDao().deleteArticle(article);

    fun getSaveNews() = db.getArticleDao().getAllArticles();

}