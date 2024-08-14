package com.qt.app.ui.article

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import com.qt.app.App
import com.qt.app.R
import com.qt.app.util.Util

@Composable
fun PageLoading(context: Context) {
    Box(modifier = Modifier.fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {
        Image(
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.Center),
            painter = rememberAsyncImagePainter(R.drawable.loading_ac, Util.imageLoader(context)),
            contentDescription = "loading",
            contentScale = ContentScale.FillBounds,
        )
    }
}