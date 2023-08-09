package com.hoantruong6814.news.api

import com.hoantruong6814.news.BuildConfig
import com.hoantruong6814.news.model.NewsResponse
import com.hoantruong6814.news.util.Constants.Companion.QUERY_PAGE_SIZE
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewAPI {


    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String = "vi",
        @Query("page")
        pageNumber: Int = 1,
        @Query("apikey")
        apiKey: String = BuildConfig.news_api_key
    ): Response<NewsResponse>


    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber: Int = 1,
        @Query("pageSize")
        pageSize: Int = QUERY_PAGE_SIZE,
        @Query("apikey")
        apiKey: String = BuildConfig.news_api_key
    ): Response<NewsResponse>
}