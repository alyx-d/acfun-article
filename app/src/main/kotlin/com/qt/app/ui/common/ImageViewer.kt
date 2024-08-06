package com.qt.app.ui.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.qt.app.util.Util.imageLoader

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageViewer(imageViewerState: MutableState<Boolean>, imageSet: MutableSet<String>) {
    val pagerState = rememberPagerState(pageCount = { imageSet.size })
    val imageList = imageSet.toList()
    val context = LocalContext.current
    HorizontalPager(
        state = pagerState,
    ) { idx ->
        Card(
            modifier = Modifier.fillMaxSize(),
            onClick = {
                imageViewerState.value = false
            },
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxWidth(),
                    model = imageList[idx],
                    contentDescription = null,
                    imageLoader = imageLoader(context),
                    contentScale = ContentScale.FillWidth
                )
            }
        }
    }
}

