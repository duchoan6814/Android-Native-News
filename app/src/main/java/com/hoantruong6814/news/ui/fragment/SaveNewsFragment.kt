package com.hoantruong6814.news.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
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

            findNavController().navigate(
                R.id.action_saveNewsFragment_to_detailNewsFragment,
                bundle
            );
        }

        val itemTouchHelperCallback =
            object :
                ItemTouchHelper.SimpleCallback(
                    ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return true;
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.layoutPosition;
                    val article = newsAdapter.differ.currentList[position];
                    viewModel.deleteArticle(article);
                    Snackbar.make(view, "Article is deleted.", Snackbar.LENGTH_SHORT).apply {
                        setAction("Undo") {
                            viewModel.saveArticle(article);
                        }
                        show()
                    }
                }

            }


        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvNewsSaved);
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