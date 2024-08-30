package com.qt.app.core.ui.state

sealed class UiState {

    data class Success<T>(val data: T) : UiState()

    data class Error(val throwable: Throwable = Throwable()) : UiState()

    data object Loading : UiState()

    @Suppress("UNCHECKED_CAST")
    fun <T> success(): T {
        if (this is Success<*>) {
            return this.data as T
        }
        throw IllegalArgumentException("only success state has data")
    }

    fun error(): Throwable {
        if (this is Error) {
            return this.throwable
        }
        throw IllegalArgumentException("only error state has error")
    }
}