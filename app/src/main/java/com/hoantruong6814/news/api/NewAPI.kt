package com.hoantruong6814.news.api

import com.hoantruong6814.news.R
import com.hoantruong6814.news.data.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Properties

interface NewAPI {


    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String = "vi",
        @Query("page")
        pageNumber: Int = 1,
        @Query("apikey")
        apiKey: Int = R.string.news_api_key
    ): Response<NewsResponse>


    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber: Int = 1,
        @Query("apikey")
        apiKey: Int = R.string.news_api_key
    ): Response<NewsResponse>
}