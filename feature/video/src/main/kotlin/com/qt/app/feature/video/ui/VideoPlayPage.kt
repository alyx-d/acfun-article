package com.qt.app.feature.video.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.qt.app.core.data.vo.KsPlayJson
import com.qt.app.core.data.vo.VideoInfoVO
import com.qt.app.core.ui.common.PageLoading
import com.qt.app.core.ui.common.usernameColor
import com.qt.app.core.ui.state.UiState
import com.qt.app.core.video.VideoPlayer
import com.qt.app.feature.video.vm.VideoPageViewModule

@Composable
fun VideoPlay(
    navController: NavHostController,
    entry: NavBackStackEntry,
    vm: VideoPageViewModule = hiltViewModel()
) {
    val args = entry.arguments ?: return
    val id = args.getString("videoId")!!
    val coverImage = args.getString("coverImage")!!
    LaunchedEffect(Unit) {
        vm.getVideoPlayInfo(id)
    }
    val uiState by vm.videoPlayInfoUiState.collectAsState()
    val context = LocalContext.current
    when (uiState) {
        is UiState.Error -> {}
        UiState.Loading -> PageLoading(context)
        is UiState.Success -> {
            val (p1, p2) = (uiState as UiState.Success).data as Pair<*, *>
            val videoInfo = p1 as VideoInfoVO
            val ksPlayJsons = (p2 as List<*>).map { it as KsPlayJson }
            val urls =
                ksPlayJsons.mapTo(mutableListOf()) { it.adaptationSet.first().representation.last().url }
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize()
            ) {
                VideoPlayer(
                    videoUrls = urls,
                    coverImage = coverImage,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .size(65.dp)
                            .padding(10.dp)
                            .clip(CircleShape),
                        model = videoInfo.user.userHeadImgInfo.thumbnailImageCdnUrl,
                        contentDescription = "headImage",
                        contentScale = ContentScale.Fit,
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = videoInfo.user.name,
                        color = usernameColor(videoInfo.user.nameColor)
                    )
                }
            }
        }
    }
}