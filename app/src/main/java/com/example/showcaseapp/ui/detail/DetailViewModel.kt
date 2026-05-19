package com.example.showcaseapp.ui.detail

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

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: AppCatalogRepository,
    @param:ApplicationContext private val context: Context
) : ViewModel() {
    private val appId: String = checkNotNull(savedStateHandle["appId"])

    private val _uiState = MutableStateFlow<UiState<AppInfo>>(UiState.Loading)
    val uiState: StateFlow<UiState<AppInfo>> = _uiState.asStateFlow()

    init {
        loadApp()
    }

    fun refresh() = loadApp(isRefresh = true)

    fun retry() = loadApp()

    private fun loadApp(isRefresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            if (isRefresh) delay(650)

            runCatching { repository.getApp(appId) }
                .onSuccess { _uiState.value = UiState.Content(it) }
                .onFailure {
                    _uiState.value = UiState.Error(
                        it.message ?: context.getString(R.string.error_load_detail)
                    )
                }
        }
    }
}
