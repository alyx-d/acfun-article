package com.qt.app.core.utils

import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onSubscription

object SnackBarHostStateHolder {
    private val hostState = MutableStateFlow<SnackbarHostState?>(null)
    private val stringMessage = MutableSharedFlow<String>(extraBufferCapacity = Int.MAX_VALUE)

    suspend fun handleHostState(hostState: SnackbarHostState) {
        this.stringMessage
            .onSubscription { this@SnackBarHostStateHolder.hostState.value = hostState }
            .onCompletion { this@SnackBarHostStateHolder.hostState.value = null }
            .collect {
                hostState.showSnackbar(message = it)
            }
    }

    fun showMessage(msg: String) {
        this.stringMessage.tryEmit(msg)
    }
}