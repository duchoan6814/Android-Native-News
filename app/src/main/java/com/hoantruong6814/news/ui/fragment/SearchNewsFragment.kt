package com.hoantruong6814.news.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hoantruong6814.news.R
import com.hoantruong6814.news.adapter.NewsAdapter
import com.hoantruong6814.news.databinding.FragmentSearchNewBinding
import com.hoantruong6814.news.ui.NewsActivity
import com.hoantruong6814.news.ui.NewsViewModel
import com.hoantruong6814.news.util.Constants.Companion.QUERY_PAGE_SIZE
import com.hoantruong6814.news.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment(R.layout.fragment_search_new) {

    private lateinit var viewModel: NewsViewModel;
    private lateinit var binding: FragmentSearchNewBinding;
    private lateinit var newsAdapter: NewsAdapter;

    val TAG = "Fragment Search News"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSearchNewBinding.bind(view);
        viewModel = (activity as NewsActivity).viewModel;

        var job: Job? = null;

        binding.ptSearchInput.addTextChangedListener { text: Editable? ->
            job?.cancel();

            job = MainScope().launch {
                delay(500L);
                text.let {
                    if (it.toString().isNotEmpty()) {
                        viewModel.searchNews(it.toString());
                    }
                }
            }

        }


        setupRecyclerView();




        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    isLoading = false;
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())

                        val totalPages = (newsResponse.totalResults / QUERY_PAGE_SIZE) + 2;

                        if(viewModel.searchNewsPage >= totalPages) {
                         isLastPage = true;
                        }
                    }

                }

                is Resource.Error -> {
                    isLoading = false;
                    response.message.let { message -> Log.e(TAG, "has an error: $message") }
                }

                is Resource.Loading -> {
                    handleLoading();
                }
            }
        })
    }


    private var isLoading = false;
    private var isLastPage = false;
    private var isScrolling = false;

    private fun handleLoading() {
        val articleList = newsAdapter.differ.currentList.toMutableList();
        articleList.add(null);
        newsAdapter.differ.submitList(articleList);
        isLoading = true;
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true;
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager;

            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            val visibleItemCount = layoutManager.childCount;
            val totalItemCount = layoutManager.itemCount;

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage;
            val isLastItem = visibleItemCount + firstVisibleItemPosition >= totalItemCount;
            val isNotAtBeginning = firstVisibleItemPosition >= 0;
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE;

            val shouldPaginate =
                isNotLoadingAndNotLastPage && isLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling;

            if (shouldPaginate) {
                viewModel.searchNews(binding.ptSearchInput.text.toString());
                isScrolling = false;
            }
        }
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter();

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it);
            }

            findNavController().navigate(
                R.id.action_searchNewsFragment_to_detailNewsFragment,
                bundle
            )
        }

        binding.rvSearchNews.apply {
            adapter = newsAdapter;
            layoutManager = LinearLayoutManager(activity);
            addOnScrollListener(this@SearchNewsFragment.scrollListener);
        }
    }
}