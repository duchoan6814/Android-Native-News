package com.hoantruong6814.news.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.hoantruong6814.news.R
import com.hoantruong6814.news.databinding.FragmentDetailNewsBinding
import com.hoantruong6814.news.model.Article
import com.hoantruong6814.news.ui.NewsActivity
import com.hoantruong6814.news.ui.NewsViewModel

class DetailNewsFragment : Fragment(R.layout.fragment_detail_news) {

    private lateinit var viewModel: NewsViewModel;
    private lateinit var binding: FragmentDetailNewsBinding;
    private val article: Article
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        get() = requireArguments().getSerializable("article", Article::class.java)
            ?: throw java.lang.IllegalArgumentException("Argument 'article' required");

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDetailNewsBinding.bind(view);

        binding.wvArticle.apply {
            webViewClient = WebViewClient();
            loadUrl(article.url);
        }

    }

}