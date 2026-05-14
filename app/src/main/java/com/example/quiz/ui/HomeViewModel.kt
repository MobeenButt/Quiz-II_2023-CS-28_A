package com.example.quiz.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiz.BuildConfig
import com.example.quiz.model.NewsArticle
import com.example.quiz.repository.NewsRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: NewsRepository = NewsRepository()
) : ViewModel() {

    private val _uiState = MutableLiveData(HomeUiState())
    val uiState: LiveData<HomeUiState> = _uiState

    fun loadNews(countryCode: String, force: Boolean = false) {
        val current = _uiState.value ?: HomeUiState()
        if (current.isLoading || (!force && current.countryCode == countryCode && current.articles.isNotEmpty())) {
            return
        }

        if (BuildConfig.GNEWS_API_KEY.isBlank()) {
            _uiState.value = current.copy(
                isLoading = false,
                errorMessage = "Missing GNEWS_API_KEY. Add it in local.properties.",
                countryCode = countryCode
            )
            return
        }

        _uiState.value = current.copy(isLoading = true, errorMessage = null, countryCode = countryCode)

        viewModelScope.launch {
            runCatching { repository.getTopHeadlines(countryCode) }
                .onSuccess { articles ->
                    _uiState.value = HomeUiState(
                        isLoading = false,
                        articles = articles,
                        countryCode = countryCode,
                        errorMessage = null
                    )
                }
                .onFailure { error ->
                    val fallback = if (current.countryCode == countryCode) current.articles else emptyList<NewsArticle>()
                    _uiState.value = HomeUiState(
                        isLoading = false,
                        articles = fallback,
                        countryCode = countryCode,
                        errorMessage = error.message ?: "Failed to load news."
                    )
                }
        }
    }
}

data class HomeUiState(
    val isLoading: Boolean = false,
    val articles: List<NewsArticle> = emptyList(),
    val countryCode: String = "us",
    val errorMessage: String? = null
)

