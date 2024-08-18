package com.qt.app.feature.dynmic.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun DynamicPage() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "页面施工中...", modifier = Modifier.align(Alignment.Center))
    }
}