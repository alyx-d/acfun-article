package com.qt.app.ui.state

sealed class UiState {

    data class Success(val data: Any) : UiState()

    data class Error(val throwable: Throwable = Throwable()): UiState()

    data object Loading : UiState()
}