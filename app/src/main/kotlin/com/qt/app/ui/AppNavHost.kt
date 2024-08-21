package com.qt.app.ui

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.qt.app.core.navigation.AcfunScreens
import com.qt.app.feature.article.ui.ArticleDetail
import com.qt.app.feature.article.ui.ArticlePage
import com.qt.app.feature.dynamic.ui.DynamicPage
import com.qt.app.feature.profile.ui.ProfilePage
import com.qt.app.feature.video.ui.VideoPage
import com.qt.app.feature.video.ui.VideoPlay

@UnstableApi
@ExperimentalMaterial3Api
@Composable
fun AppNavHost(
    navController: NavHostController,
    refreshState: MutableState<Boolean>,
) {
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
        composable(
            route = AcfunScreens.VideoPage.route,
        ) { _ ->
            VideoPage(
                navController = navController,
                refreshState = refreshState,
            )
        }
        composable(AcfunScreens.ArticlePage.route) {
            ArticlePage(
                navController,
                refreshState,
            )
        }
        composable(AcfunScreens.DynamicPage.route) {
            DynamicPage()
        }
        composable(AcfunScreens.ProfilePage.route) {
            ProfilePage()
        }
        composable(
            route = AcfunScreens.ArticleDetail.route,
            arguments = AcfunScreens.ArticleDetail.arguments
        ) { backStackEntry -> ArticleDetail(navController, backStackEntry) }
        composable(
            route = AcfunScreens.VideoPlay.route,
            arguments = AcfunScreens.VideoPlay.arguments
        ) { backStackEntry ->
            VideoPlay(navController, backStackEntry)
        }
    }
}

val displayBottomBar =
    arrayOf(
        AcfunScreens.VideoPage,
        AcfunScreens.ArticlePage,
        AcfunScreens.DynamicPage,
        AcfunScreens.ProfilePage,
    )

@Composable
fun AnimatedHomePage(visible: Boolean, content: @Composable () -> Unit) {
    AnimatedVisibility(
        visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        content()
    }
}
