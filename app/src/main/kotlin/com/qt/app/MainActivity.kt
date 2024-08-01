package com.qt.app

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.qt.app.ui.home.ArticleDetail
import com.qt.app.ui.home.ArticleList
import com.qt.app.ui.theme.MyApplicationTheme
import kotlin.reflect.jvm.jvmName

class MainActivity() : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                var selected by remember { mutableIntStateOf(0) }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
//                        val itemArr = arrayOf("综合", "吐槽", "游戏", "动漫", "涂鸦", "漫文")
                        val itemArr = arrayOf("综合", "吐槽", "游戏", "涂鸦")
                        NavigationBar {
                            itemArr.forEachIndexed { index, s ->
                                NavigationBarItem(
                                    label = { Text(text = s) },
                                    selected = selected == index,
                                    onClick = {
                                        selected = index
                                        navController.navigate("${ArticleListNavHost::class.jvmName}/$selected",
                                            navOptions = NavOptions.Builder()
                                                .setLaunchSingleTop(true)
                                                .build())
                                    },
                                    icon = { /*TODO*/ })
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = "${ArticleListNavHost::class.jvmName}/0"
                        ) {
                            composable(
                                "${ArticleListNavHost::class.jvmName}/{tabId}",
                                arguments = listOf(navArgument("tabId") {
                                    type = NavType.StringType
                                })
                            ) { backStackEntry -> ArticleList(navController, backStackEntry) }
                            composable(
                                "${ArticleDetailNavHost::class.jvmName}/{articleId}",
                                arguments = listOf(navArgument("articleId") {
                                    type = NavType.StringType
                                })
                            ) { backStackEntry -> ArticleDetail(navController, backStackEntry) }
                        }
                    }
                }
            }
        }
    }
}

class ArticleListNavHost
class ArticleDetailNavHost
