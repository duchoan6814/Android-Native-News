package com.hoantruong6814.news.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.hoantruong6814.news.R
import com.hoantruong6814.news.ui.NewsActivity
import com.hoantruong6814.news.ui.NewsViewModel

class SaveNewsFragment : Fragment(R.layout.fragment_save_news) {

    private lateinit var viewModel: NewsViewModel;
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel;
    }
}