package com.qt.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.qt.app.ui.theme.Black75

@Composable
fun AppSnackBarHost(snackbarHostState: SnackbarHostState) {
    SnackbarHost(hostState = snackbarHostState) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(13.dp))
                .background(Black75)
                .padding(horizontal = 15.dp, vertical = 5.dp)
        ) {
            Text(text = it.visuals.message, color = Color.White)
        }
    }
}