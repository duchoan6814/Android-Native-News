package com.hoantruong6814.news.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoantruong6814.news.R
import com.hoantruong6814.news.adapter.NewsAdapter
import com.hoantruong6814.news.databinding.FragmentSearchNewBinding
import com.hoantruong6814.news.ui.NewsActivity
import com.hoantruong6814.news.ui.NewsViewModel
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

        binding.ptSearchInput.addTextChangedListener {
            text: Editable? ->
            job?.cancel();

            job = MainScope().launch {
                delay(500L);
                text.let {
                    if(it.toString().isNotEmpty()) {
                        viewModel.searchNews(it.toString());
                    }
                }
            }

        }


        setupRecyclerView();




        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
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
    }

    private fun handleHideProgressBar() {
        binding.searchNewsProcessBar.visibility = View.INVISIBLE;
    }

    private fun handleShowProgressBar() {
        binding.searchNewsProcessBar.visibility = View.VISIBLE;
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
        }
    }
}