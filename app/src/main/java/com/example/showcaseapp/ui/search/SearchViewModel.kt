package com.example.showcaseapp.ui.search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.showcaseapp.R
import com.example.showcaseapp.domain.model.AppInfo
import com.example.showcaseapp.domain.repository.AppCatalogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val query: String = "",
    val apps: List<AppInfo> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val isPopularFallback: Boolean = true,
    val errorMessage: String? = null
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: AppCatalogRepository,
    @param:ApplicationContext private val context: Context
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()
    private var searchJob: Job? = null

    init {
        search("", showLoading = false)
    }

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(220)
            search(query, showLoading = false)
        }
    }

    fun refresh() {
        search(_uiState.value.query, showLoading = true)
    }

    fun retry() {
        search(_uiState.value.query, showLoading = false)
    }

    private fun search(
        query: String,
        showLoading: Boolean
    ) {
        viewModelScope.launch {
            if (showLoading) {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                delay(650)
            }

            runCatching {
                val result = repository.searchApps(query)
                if (query.isNotBlank() && result.isEmpty()) {
                    repository.getPopularApps() to true
                } else {
                    result to query.isBlank()
                }
            }.onSuccess { (apps, isPopularFallback) ->
                _uiState.update {
                    it.copy(
                        apps = apps,
                        isLoading = false,
                        isRefreshing = false,
                        isPopularFallback = isPopularFallback,
                        errorMessage = null
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        errorMessage = error.message ?: context.getString(R.string.error_search)
                    )
                }
            }
        }
    }
}
