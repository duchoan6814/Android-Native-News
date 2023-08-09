package com.hoantruong6814.news.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hoantruong6814.news.repository.NewsRepository

class NewsViewModelProviderFactory(val app: Application, private val newsRepository: NewsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(app, newsRepository) as T;
    }
}