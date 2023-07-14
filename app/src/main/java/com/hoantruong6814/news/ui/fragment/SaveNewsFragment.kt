package com.hoantruong6814.news.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoantruong6814.news.R
import com.hoantruong6814.news.adapter.NewsAdapter
import com.hoantruong6814.news.databinding.FragmentSaveNewsBinding
import com.hoantruong6814.news.ui.NewsActivity
import com.hoantruong6814.news.ui.NewsViewModel

class SaveNewsFragment : Fragment(R.layout.fragment_save_news) {

    private lateinit var viewModel: NewsViewModel;
    private lateinit var newsAdapter: NewsAdapter;
    private lateinit var binding: FragmentSaveNewsBinding;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSaveNewsBinding.bind(view);

        viewModel = (activity as NewsActivity).viewModel;

        setUpRecycleView();

        viewModel.getAllNewsSaved().observe(viewLifecycleOwner, Observer { articles ->
            newsAdapter.differ.submitList(articles);
        })


        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it);
            }

            findNavController().navigate(R.id.action_saveNewsFragment_to_detailNewsFragment, bundle);
        }
    }

    private fun setUpRecycleView() {
        newsAdapter = NewsAdapter();

        binding.rvNewsSaved.apply {
            adapter = newsAdapter;
            layoutManager = LinearLayoutManager(activity)
        }
    }
}