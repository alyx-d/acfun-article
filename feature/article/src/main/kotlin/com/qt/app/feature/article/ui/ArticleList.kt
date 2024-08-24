package com.qt.app.feature.article.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.qt.app.core.navigation.AcfunScreens
import com.qt.app.core.ui.common.PageLoading
import com.qt.app.core.utils.Util
import com.qt.app.feature.article.vm.ArticleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleList(
    navController: NavHostController,
    tabId: Int,
    refreshState: MutableState<Boolean>,
    vm: ArticleViewModel = hiltViewModel(),
) {
    val articleList = vm.articleListTab[tabId].collectAsLazyPagingItems()
    val context = LocalContext.current
    val state = rememberLazyListState()
    if (articleList.itemCount == 0) {
        PageLoading(context = context)
    }
    val pullToRefreshState = rememberPullToRefreshState()
    var isRefreshing by remember {
        mutableStateOf(false)
    }
    if (pullToRefreshState.isRefreshing && !isRefreshing) {
        articleList.refresh()
        isRefreshing = true
    }
    LaunchedEffect(articleList.loadState.refresh) {
        if (articleList.loadState.refresh is LoadState.NotLoading && isRefreshing) {
            pullToRefreshState.endRefresh()
            state.animateScrollToItem(0)
            isRefreshing = false
        }
    }
    if (refreshState.value) {
        pullToRefreshState.startRefresh()
        refreshState.value = false
    }

    Box(
        modifier = Modifier
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.surface),
            state = state,
            contentPadding = PaddingValues(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(articleList.itemCount) { idx ->
                val it = articleList[idx] ?: return@items
                Column(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(AcfunScreens.ArticleDetail.createRoute(it.articleId))
                        }
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(10.dp)

                ) {
                    Row {
                        Text(
                            text = it.title,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    AsyncImage(
                        model = it.coverImgInfo.thumbnailImageCdnUrl,
                        imageLoader = Util.imageLoader(context),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentScale = ContentScale.FillWidth
                    )
                    Row {
                        Text(
                            text = it.description,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(modifier = Modifier.alignByBaseline()) {
                            Text(
                                text = "UP:",
                                fontSize = 10.sp,
                            )
                            Text(
                                text = it.userName,
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .widthIn(max = 150.dp),
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                            )
                        }
                        Text(
                            text = Util.dateFormat(it.createTime),
                            fontSize = 12.sp,
                        )
                    }
                }
            }
        }
        PullToRefreshContainer(
            modifier = Modifier
                .align(Alignment.TopCenter),
            state = pullToRefreshState,
        )
    }
}
