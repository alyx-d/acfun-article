package com.qt.app.feature.article.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ArticlePage(
    navController: NavHostController,
    refreshState: MutableState<Boolean>,
) {
    val tabs = arrayOf("综合", "吐槽", "游戏", "涂鸦")
    val coroutineScope = rememberCoroutineScope()
    val selectedIndex = rememberSaveable { mutableIntStateOf(0) }
    val articlePagerState = rememberPagerState(pageCount = { tabs.size })
    LaunchedEffect(articlePagerState) {
        snapshotFlow { articlePagerState.currentPage }.collect {
            selectedIndex.intValue = it
        }
    }
    Column {
        TabRow(selectedTabIndex = selectedIndex.intValue,
            containerColor = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .zIndex(10f)
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
                        articlePagerState.animateScrollToPage(selectedIndex.intValue)
                    }
                }) {
                    Text(text = tab,
                        fontSize = if(selected) 18.sp else TextUnit.Unspecified,
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