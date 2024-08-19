package com.qt.app.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavBar(
    navController: NavHostController,
    refreshState: MutableState<Boolean>,
    selectedPage: MutableIntState
) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    // val itemArr = arrayOf("综合", "吐槽", "游戏", "动漫", "涂鸦", "漫文")
    val itemArr = arrayOf("视频", "文章", "动态", "我的")
    val entry by navController.currentBackStackEntryAsState()
    val bottomBarState = remember {
        mutableStateOf(false)
    }
    bottomBarState.value = displayBottomBar.any { it.route == entry?.destination?.route }
    AnimatedVisibility(visible = bottomBarState.value) {
        NavigationBar {
            itemArr.forEachIndexed { index, s ->
                val selected = selectedIndex == index
                NavigationBarItem(
                    label = { Text(text = s) },
                    selected = selected,
                    onClick = {
                        if (!selected) {
                            selectedPage.intValue = index
                        } else {
                            refreshState.value = true
                        }
                        selectedIndex = index
                    },
                    icon = { /* TODO */ },
                    colors = NavigationBarItemDefaults.colors(
                        selectedTextColor = Color.Red,
                        indicatorColor = Color.Red
                    )
                )
            }
        }
    }
}