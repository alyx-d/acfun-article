package com.qt.app.ui.article

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.qt.app.ui.Routers
import com.qt.app.ui.displayBottomBar

@Composable
fun BottomNavBar(navController: NavHostController) {
    var selected by remember { mutableIntStateOf(0) }
//    val itemArr = arrayOf("综合", "吐槽", "游戏", "动漫", "涂鸦", "漫文")
    val itemArr = arrayOf("综合", "吐槽", "游戏", "涂鸦")
    val entry by navController.currentBackStackEntryAsState()
    val bottomBarState = rememberSaveable {
        mutableStateOf(false)
    }
    bottomBarState.value = displayBottomBar.any { it.route == entry?.destination?.route }
    AnimatedVisibility(visible = bottomBarState.value) {
        NavigationBar {
            itemArr.forEachIndexed { index, s ->
                NavigationBarItem(
                    label = { Text(text = s) },
                    selected = selected == index,
                    onClick = {
                        val args = mapOf(
                            "tabId" to index,
                        )
                        selected = index
                        navController.navigate(Routers.ArticleList.path(args)) {
                            launchSingleTop = true
                        }
                    },
                    icon = {  },
                    colors = NavigationBarItemDefaults.colors(
                        selectedTextColor = Color.Red,
                        indicatorColor = Color.Red
                    )
                )
            }
        }
    }
}