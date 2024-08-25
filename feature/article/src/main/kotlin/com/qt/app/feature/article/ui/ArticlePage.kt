package com.qt.app.feature.article.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.qt.app.core.ui.common.pagerTabIndicatorOffset
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArticlePage(
    navController: NavHostController,
    refreshState: MutableState<Boolean>,
) {
    val tabs = arrayOf("综合", "吐槽", "游戏", "涂鸦")
    val coroutineScope = rememberCoroutineScope()
    val articlePagerState = rememberPagerState(pageCount = { tabs.size })
    Column {
        TabRow(
            selectedTabIndex = articlePagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.pagerTabIndicatorOffset(articlePagerState, tabPositions),
                    height = 2.dp,
                    color = Color.Red,
                )
            },
            containerColor = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .zIndex(10f)
                .padding(horizontal = 5.dp)
                .height(40.dp)
                .clip(RoundedCornerShape(10))
        ) {
            tabs.forEachIndexed { index, tab ->
                val selected = articlePagerState.currentPage == index
                Tab(
                    selectedContentColor = MaterialTheme.colorScheme.background,
                    selected = selected,
                    onClick = {
                        if (selected) {
                            refreshState.value = true
                        }
                        coroutineScope.launch {
                            articlePagerState.animateScrollToPage(index)
                        }
                    }) {
                    Text(
                        text = tab,
                        fontSize = if (selected) 18.sp else TextUnit.Unspecified,
                        color = if (selected) Color.Red else Color.Black
                    )
                }
            }
        }
        HorizontalPager(state = articlePagerState) { idx ->
            ArticleList(navController, idx, refreshState)
        }
    }
}