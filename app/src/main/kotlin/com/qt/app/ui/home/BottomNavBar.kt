package com.qt.app.ui.home

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.qt.app.ui.Route

@Composable
fun BottomNavBar(navController: NavHostController) {
    var selected by remember { mutableIntStateOf(0) }
//    val itemArr = arrayOf("综合", "吐槽", "游戏", "动漫", "涂鸦", "漫文")
    val itemArr = arrayOf("综合", "吐槽", "游戏", "涂鸦")
    NavigationBar {
        itemArr.forEachIndexed { index, s ->
            NavigationBarItem(
                label = { Text(text = s) },
                selected = selected == index,
                onClick = {
                    val args = mapOf(
                        "tabId" to index,
                        "refresh" to (index == selected),
                    ).mapTo(mutableListOf()) { (k, v) -> "$k=$v" }
                        .joinToString("&")
                    selected = index
                    navController.navigate(
                        "${Route.ArticleList.name}?$args",
                        navOptions = NavOptions.Builder()
                            .setLaunchSingleTop(true)
                            .build()
                    )
                },
                icon = { /*TODO*/ }
            )
        }
    }
}