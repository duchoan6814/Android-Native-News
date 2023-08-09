package com.hoantruong6814.news.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoantruong6814.news.NewsApplication
import com.hoantruong6814.news.model.NewsResponse
import com.hoantruong6814.news.model.Article
import com.hoantruong6814.news.repository.NewsRepository
import com.hoantruong6814.news.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(val app: Application, private val newsRepository: NewsRepository) :
    AndroidViewModel(app) {


    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData();
    var breakingNewsPage = 1;
    private var breakingNewsResponse: NewsResponse? = null;

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData();
    var searchNewsPage = 1;
    private var searchNewsResponse: NewsResponse? = null;


    init {
        getBreakingNews("us");

    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        safeBreakingNewsCall(countryCode);
    }


    fun searchNews(searchParams: String) = viewModelScope.launch {
        safeSearchNewsCall(searchParams);
    }


    private suspend fun safeSearchNewsCall(searchParams: String) {
        breakingNews.postValue(Resource.Loading());
        try {
            if (hasInternetConnection()) {
                val response = newsRepository.getSearchNews(searchParams, searchNewsPage);
                breakingNews.postValue(handleSearchNewsResponse(response));
            } else {
                breakingNews.postValue(Resource.Error("No internet connection!"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> breakingNews.postValue(Resource.Error("Network failure!"))
                else -> breakingNews.postValue(Resource.Error("Conversion Error!"))
            }
        }
    }

    private suspend fun safeBreakingNewsCall(countryCode: String) {
        breakingNews.postValue(Resource.Loading());
        try {
            if (hasInternetConnection()) {
                val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage);
                breakingNews.postValue(handleBreakingNewsResponse(response));
            } else {
                breakingNews.postValue(Resource.Error("No internet connection!"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> breakingNews.postValue(Resource.Error("Network failure!"))
                else -> breakingNews.postValue(Resource.Error("Conversion Error!"))
            }
        }
    }


    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchNewsPage++;
                if (searchNewsResponse == null) {
                    searchNewsResponse = resultResponse;
                } else {
                    val oldArticles = searchNewsResponse?.articles;
                    val newArticle = resultResponse.articles;

                    oldArticles?.addAll(newArticle);
                }
                return Resource.Success(searchNewsResponse ?: resultResponse);
            }
        }

        return Resource.Error(message = response.message());
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingNewsPage++;
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse;
                } else {
                    val oldArticles = breakingNewsResponse?.articles;
                    val newArticle = resultResponse.articles;

                    oldArticles?.addAll(newArticle);
                }
                return Resource.Success(breakingNewsResponse ?: resultResponse);
            }
        }

        return Resource.Error(message = response.message());
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<NewsApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager;

        val activeNetwork = connectivityManager.activeNetwork ?: return false;
        val capabilities =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false;

        return when {
            capabilities.hasTransport(TRANSPORT_WIFI) -> true;
            capabilities.hasTransport(TRANSPORT_CELLULAR) -> true;
            capabilities.hasTransport(TRANSPORT_ETHERNET) -> true;
            else -> false;
        }
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article);
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.delete(article);
    }

    fun getAllNewsSaved() = newsRepository.getSaveNews();

}