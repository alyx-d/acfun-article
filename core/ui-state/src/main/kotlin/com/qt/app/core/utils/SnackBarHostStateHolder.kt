package com.qt.app.core.utils

import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

object SnackBarHostStateHolder {
    private val hostState = MutableStateFlow<SnackbarHostState?>(null)
    private val stringMessage = MutableSharedFlow<String>(extraBufferCapacity = Int.MAX_VALUE)

    suspend fun handleHostState(hostState: SnackbarHostState) {
        this.stringMessage
            .collect {
                hostState.showSnackbar(message = it)
            }
    }

    fun showMessage(msg: String) {
        this.stringMessage.tryEmit(msg)
    }
}