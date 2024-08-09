package com.qt.app.ui.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImage
import com.qt.app.util.Util.imageLoader
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ImageViewer(
    imageViewerState: MutableState<Boolean>,
    imageSet: MutableSet<String>,
    currImage: MutableState<String?>
) {
    val pagerState = rememberPagerState(pageCount = { imageSet.size })
    val imageList = imageSet.toList()
    val index = currImage.value?.let {
        imageList.indexOf(it)
    } ?: 0
    LaunchedEffect(Unit) {
        pagerState.scrollToPage(index)
    }
    val context = LocalContext.current
    Box {
        HorizontalPager(
            modifier = Modifier
                .background(Color.Black),
            state = pagerState,
        ) { idx ->
            Card(
                modifier = Modifier
                    .graphicsLayer {
                        val pageOffset = (
                                (pagerState.currentPage - idx) + pagerState
                                    .currentPageOffsetFraction
                                ).absoluteValue

                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    },
                colors = CardDefaults.cardColors(containerColor = Color.Black),
                onClick = {
                    imageViewerState.value = false
                },
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth(),
                        model = imageList[idx],
                        contentDescription = null,
                        imageLoader = imageLoader(context),
                        contentScale = ContentScale.FillWidth
                    )
                }
            }
        }
        Row(
            Modifier
                .height(40.dp)
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .padding(start = 20.dp, top = 20.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                text = "${pagerState.currentPage + 1} / ${pagerState.pageCount}"
            )
        }
    }
}

