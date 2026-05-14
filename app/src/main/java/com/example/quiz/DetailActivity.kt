package com.example.quiz

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.quiz.model.NewsArticle
import com.example.quiz.util.DateUtils

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val article = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(EXTRA_ARTICLE, NewsArticle::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra(EXTRA_ARTICLE) as? NewsArticle
        }

        if (article == null) {
            finish()
            return
        }

        bindArticle(article)
    }

    private fun bindArticle(article: NewsArticle) {
        val image = findViewById<ImageView>(R.id.ivDetailImage)
        val title = findViewById<TextView>(R.id.tvDetailTitle)
        val source = findViewById<TextView>(R.id.tvDetailSource)
        val published = findViewById<TextView>(R.id.tvDetailDate)
        val description = findViewById<TextView>(R.id.tvDetailDescription)
        val content = findViewById<TextView>(R.id.tvDetailContent)
        val readFullArticle = findViewById<Button>(R.id.btnReadFullArticle)

        image.load(article.image) {
            placeholder(R.drawable.bg_image_placeholder)
            error(R.drawable.bg_image_placeholder)
            crossfade(true)
        }

        title.text = article.title
        source.text = article.sourceName
        published.text = DateUtils.format(article.publishedAt)
        description.text = article.description ?: getString(R.string.no_description)
        content.text = article.content ?: getString(R.string.no_content_preview)

        readFullArticle.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
            startActivity(intent)
        }
    }

    companion object {
        const val EXTRA_ARTICLE = "extra_article"
    }
}
