package com.qt.app.feature.article.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.qt.app.feature.article.vm.ArticleViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArticlePage(
    navController: NavHostController,
    refreshState: MutableState<Boolean>,
    articlePageState: PagerState,
    selectedIndex: MutableIntState,
    articleListState: LazyListState,
    vm: ArticleViewModel = hiltViewModel(),
) {
    val tabs = arrayOf("综合", "吐槽", "游戏", "涂鸦")
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(articlePageState) {
        snapshotFlow { articlePageState.currentPage }.collect {
            selectedIndex.intValue = it
        }
    }
    Column {
        TabRow(selectedTabIndex = selectedIndex.intValue,
            containerColor = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .height(40.dp)
                .clip(RoundedCornerShape(10))
        ) {
            tabs.forEachIndexed { index, tab ->
                val selected = selectedIndex.intValue == index
                Tab(selected = selected, onClick = {
                    if (selected) {
                        refreshState.value = true
                    }
                    selectedIndex.intValue = index
                    coroutineScope.launch {
                        articlePageState.animateScrollToPage(selectedIndex.intValue)
                    }
                }) {
                    Text(text = tab,
                        fontSize = if(selected) 18.sp else TextUnit.Unspecified,
                        color = if (selected) Color.Red else Color.Black
                    )
                }
            }
        }
        HorizontalPager(state = articlePageState) { idx ->
            ArticleList(navController, idx, refreshState, vm, articleListState)
        }
    }
}