package com.example.showcaseapp.ui.categories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.showcaseapp.R
import com.example.showcaseapp.domain.model.CategorySummary
import com.example.showcaseapp.domain.repository.AppCatalogRepository
import com.example.showcaseapp.ui.components.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val repository: AppCatalogRepository,
    @param:ApplicationContext private val context: Context
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<List<CategorySummary>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<CategorySummary>>> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    fun refresh() = loadCategories(isRefresh = true)

    fun retry() = loadCategories()

    private fun loadCategories(isRefresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            if (isRefresh) delay(650)

            runCatching { repository.getCategories() }
                .onSuccess { _uiState.value = UiState.Content(it) }
                .onFailure {
                    _uiState.value = UiState.Error(
                        it.message ?: context.getString(R.string.error_load_categories)
                    )
                }
        }
    }
}
