package com.example.showcaseapp.ui.components

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Content<T>(
        val data: T,
        val isRefreshing: Boolean = false
    ) : UiState<T>

    data class Error(
        val message: String
    ) : UiState<Nothing>
}
