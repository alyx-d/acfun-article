package com.qt.app.feature.video.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.qt.app.core.navigation.AcfunScreens
import com.qt.app.core.ui.common.PageLoading
import com.qt.app.core.ui.state.UiState
import com.qt.app.core.utils.Util
import com.qt.app.feature.video.api.vo.HomeBananaListVO
import com.qt.app.feature.video.vm.VideoPageViewModule

@Composable
fun VideoPage(
    navController: NavHostController,
    vm: VideoPageViewModule = hiltViewModel(),
    state: LazyGridState
) {
    val uiState by vm.videoUiState.collectAsState()
    val context = LocalContext.current

    when (uiState) {
        is UiState.Error -> Text(text = "Error")
        UiState.Loading -> PageLoading(context)
        is UiState.Success -> {
            val videos = ((uiState as UiState.Success).data as List<*>)[0] as List<*>
            LazyVerticalGrid(
                state = state,
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(5.dp)
            ) {
                items(videos) {
                    val video = it as HomeBananaListVO.VideoInfo
                    VideoItem(navController, video)
                }
            }
        }
    }

}

@Composable
fun VideoItem(
    navController: NavHostController,
    video: HomeBananaListVO.VideoInfo,
) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.background),
        onClick = { navController.navigate(AcfunScreens.VideoPlay.createRoute(video.id)) }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
            ) {
                AsyncImage(
                    modifier = Modifier
                        .height(120.dp)
                        .fillMaxWidth(),
                    model = video.coverImage, contentDescription = null,
                    imageLoader = Util.imageLoader(context),
                    contentScale = ContentScale.Crop
                )
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 7.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Rounded.PlayArrow, contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .size(10.dp)
                                .border(
                                    width = 1.dp,
                                    color = Color.White,
                                    shape = RoundedCornerShape(5f)
                                )
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            fontSize = 10.sp,
                            text = video.clickCount,
                            color = Color.White
                        )
                    }
                    Text(
                        fontSize = 10.sp,
                        text = video.videoTime,
                        color = Color.White,
                    )
                }
            }
            Spacer(modifier = Modifier.height(3.dp))
            Column(
                modifier = Modifier.padding(horizontal = 5.dp)
            ) {
                Text(
                    fontSize = 12.sp,
                    minLines = 2,
                    text = video.title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 14.sp
                )
                Text(
                    text = video.upName,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}
