package com.xxh.summary.download.model

sealed interface AppUiState {
    data object Loading : AppUiState
    data object Idle : AppUiState
    data class Success(val data: List<AppUIModel>) : AppUiState
    data class Error(val error: String) : AppUiState
}