package com.qt.app.feature.video.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.qt.app.core.ui.common.PageLoading
import com.qt.app.core.ui.state.UiState
import com.qt.app.core.video.VideoPlayer
import com.qt.app.feature.video.api.vo.KsPlayJson
import com.qt.app.feature.video.api.vo.VideoInfoVO
import com.qt.app.feature.video.vm.VideoPageViewModule

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
    val uiState by vm.videoPlayInfo.collectAsState()
    val context = LocalContext.current
    when(uiState) {
        is UiState.Error -> {}
        UiState.Loading -> PageLoading(context)
        is UiState.Success -> {
            val (videoInfo, ksPlayJson) = (uiState as UiState.Success).data as Pair<VideoInfoVO, KsPlayJson>
            VideoPlayer(videoUrl = ksPlayJson.adaptationSet[0].representation.last().url)
        }
    }
}