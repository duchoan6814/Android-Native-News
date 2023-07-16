package com.hoantruong6814.news.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.hoantruong6814.news.ui.NewsActivity
import com.hoantruong6814.news.ui.NewsViewModel
import com.hoantruong6814.news.util.Resource

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


        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it);
            }


            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_detailNewsFragment,
                bundle
            )
        }

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    handleHideProgressBar()
                    response.data.let { newsResponse -> newsAdapter.differ.submitList(newsResponse?.articles) }

                }

                is Resource.Error -> {
                    handleHideProgressBar();
                    response.message.let { message -> Log.e(TAG, "has an error: $message") }
                }

                is Resource.Loading -> {
                    handleShowProgressBar();
                }
            }
        })

        setupScrollListener()

    }

    private fun handleLoading() {
        val articlesList = newsAdapter.differ.currentList.toMutableList();
        articlesList.add(null);
        newsAdapter.differ.submitList(articlesList);
    }


    private fun setupScrollListener() {
        binding.rvBreakingNews.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val linearLayoutManager = binding.rvBreakingNews.layoutManager as LinearLayoutManager;

                if(linearLayoutManager.findLastCompletelyVisibleItemPosition() == newsAdapter.differ.currentList.size - 1) {
                    handleLoading();
                }
            }
        })
    }

    private fun handleHideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE;
    }

    private fun handleShowProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE;
    }

    private fun setUpRecyclerView() {
        newsAdapter = NewsAdapter();

        binding.rvBreakingNews.apply {
            adapter = newsAdapter;
            layoutManager = LinearLayoutManager(activity);
        }
    }

}