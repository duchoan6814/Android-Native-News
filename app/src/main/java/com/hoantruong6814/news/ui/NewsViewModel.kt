package com.hoantruong6814.news.ui

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoantruong6814.news.db.NewsResponse
import com.hoantruong6814.news.repository.NewsRepository
import com.hoantruong6814.news.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(val newsRepository: NewsRepository) : ViewModel() {


    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData();
    val breakingNewsPage = 1;

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData();
    val searchNewsPage = 1;


    init {
        getBreakingNews("us");

    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())

        val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage);

        breakingNews.postValue(handleNewsResponse(response));

    }


    fun searchNews(searchParams: String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading());

        val response = newsRepository.getSearchNews(searchParams, searchNewsPage);

        searchNews.postValue(handleNewsResponse(response));

    }

    private fun handleNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body().let { resultResponse ->
                return Resource.Success(resultResponse);
            }
        }

        return Resource.Error(message = response.message());
    }

}