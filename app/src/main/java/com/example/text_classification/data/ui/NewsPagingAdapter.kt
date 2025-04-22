package com.example.text_classification.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.text_classification.R
import com.example.text_classification.data.model.Article
import com.example.text_classification.data.utils.SentimentAnalyzer
import com.example.text_classification.databinding.ItemNewsBinding

class NewsPagingAdapter(
    private val analyzer: SentimentAnalyzer
) : PagingDataAdapter<Article, NewsPagingAdapter.NewsViewHolder>(ArticleComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNewsBinding.inflate(inflater, parent, false)
        return NewsViewHolder(binding, analyzer)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = getItem(position)
        if (article != null) holder.bind(article)
    }

    class NewsViewHolder(
        private val binding: ItemNewsBinding,
        private val analyzer: SentimentAnalyzer
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article) = with(binding) {
            titleTextView.text = article.title
            descriptionTextView.text = article.description
            sentimentTextView.text = analyzer.analyze(article.title ?: "")

            Glide.with(imageView.context)
                .load(article.urlToImage)
                .placeholder(R.drawable.default_img)
                .error(R.drawable.default_img)
                .into(imageView)
        }
    }

    companion object {
        private val ArticleComparator = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem.url == newItem.url

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem == newItem
        }
    }
}
