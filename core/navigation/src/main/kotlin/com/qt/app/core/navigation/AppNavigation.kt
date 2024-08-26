package com.qt.app.core.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class AcfunScreens(
    val name: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    val route = name.appendArguments(arguments)

    data object SplashPage : AcfunScreens(
        name = "splash-page"
    )
    data object VideoPage : AcfunScreens(
        name = "video-page"
    )

    data object ArticlePage : AcfunScreens(
        name = "article-page"
    )

    data object DynamicPage : AcfunScreens(
        name = "dynamic-page"
    )

    data object ProfilePage : AcfunScreens(
        name = "profile-page"
    )

    data object ArticleDetail : AcfunScreens(
        name = "article-detail",
        arguments = listOf(navArgument("articleId") {
            type = NavType.IntType
        })
    ) {
        fun createRoute(articleId: Int) =
            route.replace("{${arguments.first().name}}", articleId.toString())
    }

    data object VideoPlay : AcfunScreens(
        name = "video-play",
        arguments = listOf(
            navArgument("videoId") {
                type = NavType.StringType
            },
            navArgument("coverImage") {
                type = NavType.StringType
                defaultValue = ""
            },
            navArgument("commentCount") {
                type = NavType.StringType
                defaultValue = ""
            }
        )
    ) {
        fun createRoute(videoId: String, coverImage: String, commentCount: String) =
            route.replace("{${arguments.first().name}}", videoId)
                .replace("{${arguments[1].name}}", coverImage)
                .replace("{${arguments[2].name}}", commentCount)
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

val displayBottomBar =
    arrayOf(
        AcfunScreens.VideoPage,
        AcfunScreens.ArticlePage,
        AcfunScreens.DynamicPage,
        AcfunScreens.ProfilePage,
    )