package com.example.showcaseapp.ui.showcase

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.showcaseapp.R
import com.example.showcaseapp.domain.model.AppCategory
import com.example.showcaseapp.domain.model.AppInfo
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

data class ShowcaseContent(
    val apps: List<AppInfo>,
    val selectedCategory: AppCategory?
)

@HiltViewModel
class ShowcaseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: AppCatalogRepository,
    @param:ApplicationContext private val context: Context
) : ViewModel() {
    val selectedCategory: AppCategory? = savedStateHandle.get<String>("category")
        ?.takeIf { it.isNotBlank() }
        ?.let { AppCategory.fromTitle(it) }

    private val _uiState = MutableStateFlow<UiState<ShowcaseContent>>(UiState.Loading)
    val uiState: StateFlow<UiState<ShowcaseContent>> = _uiState.asStateFlow()

    init {
        loadApps(showLoading = false)
    }

    fun refresh() = loadApps(showLoading = true)

    fun retry() = loadApps(showLoading = false)

    private fun loadApps(showLoading: Boolean = true) {
        viewModelScope.launch {
            if (showLoading) {
                _uiState.value = UiState.Loading
                delay(650)
            }

            runCatching {
                repository.getApps(selectedCategory)
            }.onSuccess { apps ->
                _uiState.value = UiState.Content(
                    ShowcaseContent(
                        apps = apps,
                        selectedCategory = selectedCategory
                    )
                )
            }.onFailure { error ->
                _uiState.value = UiState.Error(
                    error.message ?: context.getString(R.string.error_check_connection)
                )
            }
        }
    }
}
