package com.qt.app.feature.video.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.qt.app.core.ui.common.PageLoading
import com.qt.app.core.ui.state.UiState
import com.qt.app.core.video.VideoPlayer
import com.qt.app.feature.video.api.vo.KsPlayJson
import com.qt.app.feature.video.api.vo.VideoInfoVO
import com.qt.app.feature.video.vm.VideoPageViewModule

@UnstableApi
@Composable
fun VideoPlay(
    navController: NavHostController,
    entry: NavBackStackEntry,
    vm: VideoPageViewModule = hiltViewModel()
) {
    val id = entry.arguments?.getString("videoId") ?: return
    LaunchedEffect(Unit) {
        vm.getVideoPlay(id)
    }
    val uiState by vm.videoPlayInfoUiState.collectAsState()
    val context = LocalContext.current
    when(uiState) {
        is UiState.Error -> {}
        UiState.Loading -> PageLoading(context)
        is UiState.Success -> {
            val (p1, p2) = (uiState as UiState.Success).data as Pair<*, *>
            val videoInfo = p1 as VideoInfoVO
            val ksPlayJson = p2 as KsPlayJson
            Box(modifier = Modifier.wrapContentSize()){
                val urls = listOf(ksPlayJson.adaptationSet.first().representation.last().url)
                VideoPlayer(videoUrls = urls)
            }
        }
    }
}