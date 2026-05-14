package com.example.quiz

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quiz.model.NewsArticle
import com.example.quiz.ui.HomeViewModel
import com.example.quiz.ui.NewsAdapter
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var countryAutoComplete: AutoCompleteTextView
    private lateinit var searchEditText: TextInputEditText
    private lateinit var refreshButton: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: CircularProgressIndicator
    private lateinit var errorTextView: TextView

    private lateinit var adapter: NewsAdapter
    private var allArticles: List<NewsArticle> = emptyList()
    private var currentCountryCode: String = "us"

    // Build full country list from system Locale — sorted A-Z by display name
    private val countries: List<CountryOption> by lazy {
        Locale.getISOCountries()
            .map { code ->
                val locale = Locale("", code)
                CountryOption(
                    name = locale.getDisplayCountry(Locale.ENGLISH),
                    code = code.lowercase()
                )
            }
            .filter { it.name.isNotBlank() }
            .sortedBy { it.name }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews()
        setupRecycler()
        setupCountrySelector()
        setupHeadlineSearch()
        setupActions()
        observeUi()

        viewModel.loadNews(currentCountryCode)
    }

    private fun bindViews() {
        countryAutoComplete = findViewById(R.id.actCountry)
        searchEditText = findViewById(R.id.etHeadlineSearch)
        refreshButton = findViewById(R.id.btnRefresh)
        recyclerView = findViewById(R.id.rvNews)
        progressBar = findViewById(R.id.progressBar)
        errorTextView = findViewById(R.id.tvError)
    }

    private fun setupRecycler() {
        adapter = NewsAdapter { article ->
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_ARTICLE, article)
            }
            startActivity(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun setupCountrySelector() {
        val countryNames = countries.map { it.name }
        val dropdownAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, countryNames)
        countryAutoComplete.setAdapter(dropdownAdapter)

        // Set default selection to United States
        val default = countries.firstOrNull { it.code == currentCountryCode }
        countryAutoComplete.setText(default?.name ?: "United States", false)

        countryAutoComplete.setOnItemClickListener { _, _, position, _ ->
            // position is relative to the filtered list shown — use the adapter item
            val selectedName = dropdownAdapter.getItem(position) ?: return@setOnItemClickListener
            val selected = countries.firstOrNull { it.name == selectedName } ?: return@setOnItemClickListener
            if (selected.code != currentCountryCode) {
                currentCountryCode = selected.code
                viewModel.loadNews(currentCountryCode, force = true)
            }
        }
    }

    private fun setupHeadlineSearch() {
        searchEditText.doAfterTextChanged {
            applyArticleFilter(it?.toString().orEmpty())
        }
    }

    private fun setupActions() {
        refreshButton.setOnClickListener {
            viewModel.loadNews(currentCountryCode, force = true)
        }
    }

    private fun observeUi() {
        viewModel.uiState.observe(this) { state ->
            progressBar.visibility = if (state.isLoading) android.view.View.VISIBLE else android.view.View.GONE

            allArticles = state.articles
            applyArticleFilter(searchEditText.text?.toString().orEmpty())

            if (state.errorMessage.isNullOrBlank()) {
                errorTextView.visibility = android.view.View.GONE
            } else {
                errorTextView.visibility = android.view.View.VISIBLE
                errorTextView.text = state.errorMessage
            }
        }
    }

    private fun applyArticleFilter(query: String) {
        val filtered = if (query.isBlank()) {
            allArticles
        } else {
            allArticles.filter { article ->
                article.title.contains(query, ignoreCase = true) ||
                    article.sourceName.contains(query, ignoreCase = true)
            }
        }
        adapter.submitList(filtered)
    }
}

data class CountryOption(
    val name: String,
    val code: String
)