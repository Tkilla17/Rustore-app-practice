package com.example.showcaseapp.ui.screenshots

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.showcaseapp.R
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

data class ScreenshotsContent(
    val app: AppInfo,
    val startIndex: Int
)

@HiltViewModel
class ScreenshotsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: AppCatalogRepository,
    @param:ApplicationContext private val context: Context
) : ViewModel() {
    private val appId: String = checkNotNull(savedStateHandle["appId"])
    private val startIndex: Int = savedStateHandle["startIndex"] ?: 0

    private val _uiState = MutableStateFlow<UiState<ScreenshotsContent>>(UiState.Loading)
    val uiState: StateFlow<UiState<ScreenshotsContent>> = _uiState.asStateFlow()

    init {
        loadApp()
    }

    fun refresh() = loadApp(isRefresh = true)

    fun retry() = loadApp()

    private fun loadApp(isRefresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            if (isRefresh) delay(650)

            runCatching {
                ScreenshotsContent(
                    app = repository.getApp(appId),
                    startIndex = startIndex
                )
            }.onSuccess {
                _uiState.value = UiState.Content(it)
            }.onFailure {
                _uiState.value = UiState.Error(
                    it.message ?: context.getString(R.string.error_load_screenshots)
                )
            }
        }
    }
}
