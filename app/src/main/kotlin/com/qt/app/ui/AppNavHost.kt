package com.qt.app.ui

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.qt.app.core.navigation.AcfunScreens
import com.qt.app.core.network.utils.isOnline
import com.qt.app.core.network.utils.restartApp
import com.qt.app.core.utils.SnackBarHostStateHolder
import com.qt.app.feature.article.ui.ArticleDetail
import com.qt.app.feature.article.ui.ArticlePage
import com.qt.app.feature.dynamic.ui.DynamicPage
import com.qt.app.feature.profile.ui.ProfilePage
import com.qt.app.feature.video.ui.VideoPage
import com.qt.app.feature.video.ui.VideoPlay

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
            SnackBarHostStateHolder.showMessage("再次点击退出程序~")
            time = System.currentTimeMillis()
        } else {
            activity?.finish()
        }
    }
    if (isOnline().not()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "无网络, 请打开网络重试")
                Button(
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        containerColor = MaterialTheme.colorScheme.background
                    ),
                    onClick = { restartApp(context) }
                ) {
                    Text(text = "点击重启")
                }
            }
            return
        }
    }
    NavHost(
        navController = navController,
        startDestination = AcfunScreens.VideoPage.route
    ) {
        composable(
            route = AcfunScreens.VideoPage.route,
        ) {
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

fun NavHostController.singleTopTo(route: String) {
    this.navigate(route) {
        popUpTo(this@singleTopTo.graph.startDestinationId) {
            inclusive = true
            saveState = true
        }
        restoreState = true
        launchSingleTop = true
    }
}