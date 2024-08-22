package com.qt.app.feature.video.ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.qt.app.core.data.vo.HomeBananaListVO
import com.qt.app.core.navigation.AcfunScreens
import com.qt.app.core.ui.common.PageLoading
import com.qt.app.core.ui.state.UiState
import com.qt.app.core.utils.Util
import com.qt.app.feature.video.vm.VideoPageViewModule

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPage(
    navController: NavHostController,
    refreshState: MutableState<Boolean>,
    vm: VideoPageViewModule = hiltViewModel(),
) {
    val uiState by vm.videoUiState.collectAsState()
    val context = LocalContext.current
    val lazyGridState = rememberLazyGridState()
    when (uiState) {
        is UiState.Error -> Text(text = "Error")
        UiState.Loading -> PageLoading(context)
        is UiState.Success -> {
            val data = (uiState as UiState.Success).data as List<*>
            val videoData by remember { mutableStateOf(data.map { it as List<*> }) }
            var videos by remember { mutableStateOf(videoData[0]) }
            val isRefresh by vm.refreshState.collectAsState()
            val pullToRefreshState = rememberPullToRefreshState()
            if (pullToRefreshState.isRefreshing) {
                vm.refresh()
            }
            LaunchedEffect(isRefresh) {
                if (isRefresh) pullToRefreshState.startRefresh()
                else {
                    pullToRefreshState.endRefresh()
                    lazyGridState.animateScrollToItem(0)
                }
            }
            if (refreshState.value) {
                vm.refresh()
                refreshState.value = false
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(pullToRefreshState.nestedScrollConnection)
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(horizontal = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        var expended by remember { mutableStateOf(false) }
                        val menus = arrayOf("日榜", "三日榜", "周榜")
                        var menu by remember { mutableStateOf(menus[0]) }
                        val toggleMenu = { expended = !expended }
                        Text(
                            text = "香蕉榜", fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Column {
                            TextButton(
                                onClick = toggleMenu,
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    contentColor = Color.Black
                                )
                            ) {
                                Text(text = menu, fontSize = 14.sp)
                            }
                            DropdownMenu(
                                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                                expanded = expended,
                                onDismissRequest = { toggleMenu() }
                            ) {
                                menus.forEachIndexed { idx, it ->
                                    DropdownMenuItem(
                                        text = { Text(text = it) },
                                        onClick = {
                                            toggleMenu()
                                            menu = it
                                            videos = videoData[idx]
                                        }
                                    )
                                }
                            }
                        }
                    }
                    LazyVerticalGrid(
                        state = lazyGridState,
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(5.dp)
                    ) {
                        items(videos) {
                            val video = it as HomeBananaListVO.VideoInfo
                            VideoItem(navController, video)
                        }
                    }
                }
                PullToRefreshContainer(
                    modifier = Modifier.align(Alignment.TopCenter),
                    state = pullToRefreshState,
                )
            }
        }
    }

}

@Composable
fun VideoItem(
    navController: NavHostController,
    video: com.qt.app.core.data.vo.HomeBananaListVO.VideoInfo,
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
