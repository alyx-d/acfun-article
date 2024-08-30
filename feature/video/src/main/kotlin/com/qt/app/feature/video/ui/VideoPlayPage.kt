package com.qt.app.feature.video.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.qt.app.core.data.vo.KsPlayJson
import com.qt.app.core.data.vo.VideoInfoVO
import com.qt.app.core.ui.common.PageLoading
import com.qt.app.core.ui.common.pagerTabIndicatorOffset
import com.qt.app.core.ui.common.usernameColor
import com.qt.app.core.ui.components.CommentComponent
import com.qt.app.core.ui.components.NoCommentComponent
import com.qt.app.core.ui.components.PagingFooter
import com.qt.app.core.ui.state.UiState
import com.qt.app.core.video.VideoPlayer
import com.qt.app.feature.video.vm.VideoPageViewModule
import kotlinx.coroutines.launch

@Composable
fun VideoPlay(
    navController: NavHostController,
    entry: NavBackStackEntry,
    vm: VideoPageViewModule = hiltViewModel()
) {
    val args = entry.arguments ?: return
    val id = args.getString("videoId")!!
    val coverImage = args.getString("coverImage")!!
    val commentCount = args.getString("commentCount")!!
    LaunchedEffect(Unit) { initPageData(vm, id) }
    val uiState by vm.videoPlayInfoUiState.collectAsState()
    val context = LocalContext.current
    when (uiState) {
        is UiState.Error -> {}
        UiState.Loading -> PageLoading(context)
        is UiState.Success<*> -> {
            val (videoInfo, ksPlayJsons) = uiState.success<Pair<VideoInfoVO, List<KsPlayJson>>>()
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
                VideoAndCommentTab(navController, commentCount) { index ->
                    when (index) {
                        0 -> VideoTabContent(videoInfo)
                        1 -> CommentTabContent()
                    }
                }

            }
        }
    }
}

@Composable
fun CommentTabContent(
    vm: VideoPageViewModule = hiltViewModel()
) {
    val comments = vm.comments.collectAsLazyPagingItems()
    val subComments = vm.subComments.collectAsLazyPagingItems()
    val userEmotion by vm.userEmotion.collectAsState()
    val state = rememberLazyListState()
    val scope = rememberCoroutineScope()
    LazyColumn(
        state = state,
    ) {
        if (comments.itemCount == 0) {
            item {
                NoCommentComponent()
            }
        } else {
            items(comments.itemCount, key = { comments[it]?.commentId ?: it }) {
                val comment = comments[it] ?: return@items
                CommentComponent(
                    comment = comment,
                    subCommentList = subComments,
                    emotionMap = userEmotion
                ) { _ ->
                    scope.launch {
                        vm.getSubCommentList(comment.sourceId, comment.commentId)
                    }
                }
            }
            item {
                PagingFooter()
            }
        }
    }
}

@Composable
fun VideoTabContent(videoInfo: VideoInfoVO) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
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
        Text(
            modifier = Modifier
                .padding(horizontal = 10.dp),
            text = videoInfo.title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
        Row(
            modifier = Modifier
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(modifier = Modifier.alignByBaseline(), text = "${videoInfo.viewCountShow}播放")
            Spacer(modifier = Modifier.width(10.dp))
            Text(modifier = Modifier.alignByBaseline(), text = "AC${videoInfo.currentVideoId}")
            Spacer(modifier = Modifier.width(10.dp))
            Text(modifier = Modifier.alignByBaseline(), text = videoInfo.createTime)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VideoAndCommentTab(
    navController: NavHostController,
    commentCount: String,
    content: @Composable (Int) -> Unit,
) {
    val tabs = arrayOf("简介", "评论")
    val pagerState = rememberPagerState { tabs.size }
    val coroutineScope = rememberCoroutineScope()
    TabRow(
        modifier = Modifier
            .shadow(10.dp),
        containerColor = MaterialTheme.colorScheme.background,
        selectedTabIndex = pagerState.currentPage,
        indicator = { tabsOptions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.pagerTabIndicatorOffset(
                    pagerState,
                    tabsOptions,
                ),
                color = Color.Red,
            )
        }
    ) {
        tabs.forEachIndexed { idx, tab ->
            Tab(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(horizontal = 5.dp, vertical = 8.dp),
                selected = idx == pagerState.currentPage,
                selectedContentColor = MaterialTheme.colorScheme.background,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(idx)
                    }
                }
            ) {
                Text(
                    color = MaterialTheme.colorScheme.onBackground,
                    text = when (idx) {
                        1 -> "$tab $commentCount"
                        else -> tab
                    },
                )
            }
        }
    }
    HorizontalPager(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        state = pagerState,
    ) { idx ->
        content(idx)
    }
}

private fun initPageData(vm: VideoPageViewModule, id: String) {
    vm.getVideoPlayInfo(id)
    vm.getUserEmotion()
    vm.getComments(id)
}
