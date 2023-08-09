package com.hoantruong6814.news.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoantruong6814.news.model.NewsResponse
import com.hoantruong6814.news.model.Article
import com.hoantruong6814.news.repository.NewsRepository
import com.hoantruong6814.news.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(val newsRepository: NewsRepository) : ViewModel() {


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
        breakingNews.postValue(Resource.Loading())

        val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage);

        breakingNews.postValue(handleBreakingNewsResponse(response));

    }


    fun searchNews(searchParams: String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading());

        val response = newsRepository.getSearchNews(searchParams, searchNewsPage);

        searchNews.postValue(handleSearchNewsResponse(response));

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

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article);
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.delete(article);
    }

    fun getAllNewsSaved() = newsRepository.getSaveNews();

}