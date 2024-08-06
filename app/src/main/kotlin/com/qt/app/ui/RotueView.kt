package com.qt.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.qt.app.ui.article.ArticleDetail
import com.qt.app.ui.article.ArticleList

@Composable
fun RouteView(navController: NavHostController, refreshState: MutableState<Boolean>) {
    NavHost(
        navController = navController,
        startDestination = Routers.ArticleList.route
    ) {
        composable(
            route = Routers.ArticleList.route,
            arguments = Routers.ArticleList.arguments
        ) { backStackEntry -> ArticleList(navController, backStackEntry, refreshState) }
        composable(
            route = Routers.ArticleDetail.route,
            arguments = Routers.ArticleDetail.arguments
        ) { backStackEntry -> ArticleDetail(navController, backStackEntry) }
    }
}

enum class Routers(
    val route: String,
    val arguments: List<NamedNavArgument> = listOf(),
    val path: (Map<String, Any>) -> String
) {
    ArticleList("article?tabId={tabId}", listOf(
        navArgument("tabId") {
            type = NavType.IntType
            defaultValue = 0
        }
    ), { map -> "article?tabId=${map["tabId"]}" }),
    ArticleDetail("article-detail/{articleId}", listOf(
        navArgument("articleId") {
            type = NavType.IntType
        }), { map -> "article-detail/${map["articleId"]}" }),
}

val displayBottomBar =
    arrayOf(Routers.ArticleList)