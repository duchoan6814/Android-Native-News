package com.hoantruong6814.news.repository

import com.hoantruong6814.news.api.RetrofitInstance
import com.hoantruong6814.news.db.ArticleDatabase

class NewsRepository(val db: ArticleDatabase) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber);

}