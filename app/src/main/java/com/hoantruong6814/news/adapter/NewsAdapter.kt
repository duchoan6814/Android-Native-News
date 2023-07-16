package com.hoantruong6814.news.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.hoantruong6814.news.R
import com.hoantruong6814.news.model.Article

class NewsAdapter : Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView);

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url;
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem;
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == VIEW_TYPE_ITEM) {
            ArticleViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_article_preview, parent,
                    false
                )
            )
        } else {
            LoadingViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_loading, parent,
                    false
                )
            )
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (differ.currentList[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ArticleViewHolder) {
            populateArticleItem(holder, position)
        } else if (holder is LoadingViewHolder) {
            populateLoadingItem(holder, position)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size;
    }

    private var onItemClickListener: ((Article) -> Unit)? = null

    private fun populateLoadingItem(holder: LoadingViewHolder, position: Int) {}

    private fun populateArticleItem(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position];

        val ivCover: ImageView = holder.itemView.findViewById(R.id.ivCover);
        val tvSource: TextView = holder.itemView.findViewById(R.id.tvSource);
        val tvTitle: TextView = holder.itemView.findViewById(R.id.tvTitle);
        val tvCreatedAt: TextView = holder.itemView.findViewById(R.id.tvCreatedAt);

        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(ivCover);

            tvSource.text = article.source.name;
            tvTitle.text = article.title;
            tvCreatedAt.text = article.publishedAt;

            setOnClickListener {
                onItemClickListener?.let { it(article) }
            }
        }

    }


    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }
}