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
        startDestination = AcfunScreens.ArticleList.route
    ) {
        composable(
            route = AcfunScreens.ArticleList.route,
            arguments = AcfunScreens.ArticleList.arguments
        ) { backStackEntry -> ArticleList(navController, backStackEntry, refreshState) }
        composable(
            route = AcfunScreens.ArticleDetail.route,
            arguments = AcfunScreens.ArticleDetail.arguments
        ) { backStackEntry -> ArticleDetail(navController, backStackEntry) }
    }
}

val displayBottomBar =
    arrayOf(AcfunScreens.ArticleList)

sealed class AcfunScreens(
    val name: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    val route = name.appendArguments(arguments)

    data object ArticleList: AcfunScreens(
        name = "article-list",
        arguments = listOf(navArgument("tabId") {
            type = NavType.IntType
            defaultValue = 0
        })
    ) {
        fun createRoute(tabId: Int): String =
            route.replace("{${arguments.first().name}}", tabId.toString())
    }

    data object ArticleDetail: AcfunScreens(
        name = "article-detail",
        arguments = listOf(navArgument("articleId") {
            type = NavType.IntType
        })
    ) {
        fun createRoute(articleId: Int) =
            route.replace("{${arguments.first().name}}", articleId.toString())
    }

}

private fun String.appendArguments(arguments: List<NamedNavArgument>): String {
    val requiredArguments = arguments.filter { it.argument.defaultValue == null }
        .takeIf { it.isNotEmpty() }
        ?.joinToString(prefix = "/", separator = "/") { "{${it.name}}" }
        .orEmpty()
    val optionalArguments = arguments.filter { it.argument.defaultValue != null }
        .takeIf { it.isNotEmpty() }
        ?.joinToString(prefix = "?", separator = "&") { "${it.name}={${it.name}}" }
        .orEmpty()
    return "$this$requiredArguments$optionalArguments"
}


