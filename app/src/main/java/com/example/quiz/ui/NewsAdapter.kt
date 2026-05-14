package com.example.quiz.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.quiz.R
import com.example.quiz.model.NewsArticle
import com.example.quiz.util.DateUtils

class NewsAdapter(
    private val onClick: (NewsArticle) -> Unit
) : ListAdapter<NewsArticle, NewsAdapter.NewsViewHolder>(NewsDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position), onClick)
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.ivNewsImage)
        private val title: TextView = itemView.findViewById(R.id.tvNewsTitle)
        private val source: TextView = itemView.findViewById(R.id.tvSource)
        private val date: TextView = itemView.findViewById(R.id.tvDate)

        fun bind(article: NewsArticle, onClick: (NewsArticle) -> Unit) {
            title.text = article.title
            source.text = article.sourceName
            date.text = DateUtils.format(article.publishedAt)

            image.load(article.image) {
                placeholder(R.drawable.bg_image_placeholder)
                error(R.drawable.bg_image_placeholder)
                crossfade(true)
            }

            itemView.setOnClickListener { onClick(article) }
        }
    }
}

private object NewsDiffCallback : DiffUtil.ItemCallback<NewsArticle>() {
    override fun areItemsTheSame(oldItem: NewsArticle, newItem: NewsArticle): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: NewsArticle, newItem: NewsArticle): Boolean {
        return oldItem == newItem
    }
}

