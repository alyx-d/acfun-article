package com.qt.app.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.qt.app.ui.article.ArticleDetail
import com.qt.app.ui.article.ArticleList

@Composable
fun RouteView(navController: NavHostController, modifier : Modifier) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Route.ArticleList.name
    ) {
        composable(
            "${Route.ArticleList.name}?tabId={tabId}&refresh={refresh}",
            arguments = listOf(
                navArgument("tabId") {
                    type = NavType.IntType
                    defaultValue = 0
                },
                navArgument("refresh") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { backStackEntry -> ArticleList(navController, backStackEntry) }
        composable(
            "${Route.ArticleDetail.name}/{articleId}",
            arguments = listOf(navArgument("articleId") {
                type = NavType.StringType
            })
        ) { backStackEntry -> ArticleDetail(navController, backStackEntry) }
    }
}

enum class Route {
    ArticleList,
    ArticleDetail,
}