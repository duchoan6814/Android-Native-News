package com.hoantruong6814.news.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.hoantruong6814.news.R
import com.hoantruong6814.news.adapter.NewsAdapter
import com.hoantruong6814.news.databinding.FragmentBreakingNewsBinding
import com.hoantruong6814.news.model.Article
import com.hoantruong6814.news.ui.NewsActivity
import com.hoantruong6814.news.ui.NewsViewModel
import com.hoantruong6814.news.util.Constants.Companion.QUERY_PAGE_SIZE
import com.hoantruong6814.news.util.Resource
import kotlin.math.ceil

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    private lateinit var binding: FragmentBreakingNewsBinding;
    private lateinit var viewModel: NewsViewModel;
    private lateinit var newsAdapter: NewsAdapter;

    private val TAG = "Breaking News";

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentBreakingNewsBinding.bind(view);

        viewModel = (activity as NewsActivity).viewModel;

        setUpRecyclerView();

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {

                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList());

                        val totalPages = ceil(newsResponse.totalResults.toDouble() / QUERY_PAGE_SIZE.toDouble()) + 1;

                        isLastPage = viewModel.breakingNewsPage >= totalPages;
                        isLoading = false;
                    }


                }

                is Resource.Error -> {
                    isLoading = false;
                    response.message.let { message -> Toast.makeText(activity, message, Toast.LENGTH_SHORT).show() }
                }

                is Resource.Loading -> {
                    handleLoading();
                }
            }
        })
    }

    private fun handleLoading() {
        val articlesList = newsAdapter.differ.currentList.toMutableList();
        articlesList.add(null);
        newsAdapter.differ.submitList(articlesList);
        isLoading = true;
    }


    private var isLoading = false;
    private var isLastPage = false;
    private var isScrolling = false;


    private var scrollListener = object : RecyclerView.OnScrollListener() {
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
            val isLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount;
            val isNotAtBeginning = firstVisibleItemPosition >= 0;
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE;

            val shouldPaginate = isNotLoadingAndNotLastPage && isLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling;

            if(shouldPaginate) {
                viewModel.getBreakingNews("us");
                isScrolling = false;
            }

        }
    }


    private fun setUpRecyclerView() {
        newsAdapter = NewsAdapter();


        newsAdapter.apply {
            setOnItemClickListener {
                val bundle = Bundle().apply {
                    putSerializable("article", it);
                }


                findNavController().navigate(
                    R.id.action_breakingNewsFragment_to_detailNewsFragment,
                    bundle
                )
            }
        }

        binding.rvBreakingNews.apply {
            adapter = newsAdapter;
            layoutManager = LinearLayoutManager(activity);
            addOnScrollListener(this@BreakingNewsFragment.scrollListener);
        }
    }

}