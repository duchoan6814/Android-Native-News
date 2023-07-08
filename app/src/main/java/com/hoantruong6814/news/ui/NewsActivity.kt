package com.hoantruong6814.news.ui

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.hoantruong6814.news.R
import com.hoantruong6814.news.databinding.ActivityNewsBinding
import com.hoantruong6814.news.db.ArticleDatabase
import com.hoantruong6814.news.repository.NewsRepository

class NewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsBinding
    lateinit var viewModel: NewsViewModel;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val db = ArticleDatabase(this);
        val newsRepository = NewsRepository(db);
        val newsViewModelProviderFactory = NewsViewModelProviderFactory(newsRepository);

        viewModel = ViewModelProvider(this, newsViewModelProviderFactory).get(NewsViewModel::class.java);

        val newsNavHostFragment =
            supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment;
        binding.bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())

    }


}