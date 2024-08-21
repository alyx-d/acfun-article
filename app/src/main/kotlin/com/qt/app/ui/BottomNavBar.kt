package com.qt.app.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavBar(
    navController: NavHostController,
    refreshState: MutableState<Boolean>,
    selectedPage: MutableIntState,
) {
    val itemArr = arrayOf("视频", "文章", "动态", "我的")
    val entry by navController.currentBackStackEntryAsState()
    val bottomBarState = remember {
        mutableStateOf(false)
    }
    bottomBarState.value = displayBottomBar.any { it.route == entry?.destination?.route }
    AnimatedVisibility(visible = bottomBarState.value) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = Color.Green,
        ) {
            itemArr.forEachIndexed { index, s ->
                val selected = selectedPage.intValue == index
                val opacity by remember {
                    derivedStateOf {
                        if (selectedPage.intValue == index) 1.3f else 1.0f
                    }
                }
                NavigationBarItem(
                    label = { Box(modifier = Modifier.scale(opacity)) { Text(text = s) } },
                    selected = selected,
                    onClick = {
                        if (index == selectedPage.intValue) {
                            refreshState.value = true
                        }
                        selectedPage.intValue = index
                        navController.navigate(displayBottomBar[index].route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
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