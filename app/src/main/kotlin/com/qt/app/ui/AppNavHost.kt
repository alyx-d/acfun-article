package com.qt.app.ui

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.qt.app.core.navigation.AcfunScreens
import com.qt.app.feature.article.ui.ArticleDetail
import com.qt.app.feature.article.ui.ArticlePage
import com.qt.app.feature.dynmic.ui.DynamicPage
import com.qt.app.feature.profile.ui.ProfilePage
import com.qt.app.feature.video.ui.VideoPage

@Composable
fun AppNavHost(navController: NavHostController, refreshState: MutableState<Boolean>) {
    val context = LocalContext.current
    val activity = (context as? Activity)
    var time by remember {
        mutableLongStateOf(0L)
    }
    BackHandler {
        if (System.currentTimeMillis() - time > 2000) {
            Toast.makeText(context, "再次点击退出程序~", Toast.LENGTH_SHORT).show()
            time = System.currentTimeMillis()
        } else {
            activity?.finish()
        }
    }
    NavHost(
        navController = navController,
        startDestination = AcfunScreens.VideoPage.route
    ) {
        composable(route = AcfunScreens.VideoPage.route) {
            VideoPage()
        }
        composable(route = AcfunScreens.DynamicPage.route) {
            DynamicPage()
        }
        composable(route = AcfunScreens.ProfilePage.route) {
            ProfilePage()
        }
        composable(
            route = AcfunScreens.ArticlePage.route,
        ) { _ -> ArticlePage(navController, refreshState) }
        composable(
            route = AcfunScreens.ArticleDetail.route,
            arguments = AcfunScreens.ArticleDetail.arguments
        ) { backStackEntry -> ArticleDetail(navController, backStackEntry) }
    }
}

val displayBottomBar =
    arrayOf(
        AcfunScreens.VideoPage,
        AcfunScreens.ArticlePage,
        AcfunScreens.DynamicPage,
        AcfunScreens.ProfilePage,
    )




