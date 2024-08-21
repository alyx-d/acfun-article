package com.qt.app.ui

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
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

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    navController: NavHostController,
    refreshState: MutableState<Boolean>,
    selectedPage: MutableIntState,
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
        startDestination = AcfunScreens.HomePage.route
    ) {
        composable(
            route = AcfunScreens.HomePage.route,
        ) { _ ->
            Box {
                val lazyGridState = rememberLazyGridState()
                AnimatedHomePage(visible = selectedPage.intValue == 0) {
                    VideoPage(
                        navController = navController,
                        lazyGridState = lazyGridState,
                        refreshState = refreshState,
                    )
                }
                val selectedIndex = rememberSaveable { mutableIntStateOf(0) }
                val articlePageState = rememberPagerState(pageCount = { 4 })
                val articleListStates = List(4) { rememberLazyListState() }
                AnimatedHomePage(visible = selectedPage.intValue == 1) {
                    ArticlePage(
                        navController,
                        refreshState,
                        selectedIndex = selectedIndex,
                        articlePageState = articlePageState,
                        articleListStates = articleListStates,
                    )
                }
                AnimatedHomePage(visible = selectedPage.intValue == 2) {
                    DynamicPage()
                }
                AnimatedHomePage(visible = selectedPage.intValue == 3) {
                    ProfilePage()
                }
            }
        }
        composable(
            route = AcfunScreens.ArticleDetail.route,
            arguments = AcfunScreens.ArticleDetail.arguments
        ) { backStackEntry -> ArticleDetail(navController, backStackEntry) }
        composable(
            route = AcfunScreens.VideoPlay.route,
            arguments = AcfunScreens.VideoPlay.arguments
        ) { entry ->
            VideoPlay(navController, entry)
        }
    }
}

val displayBottomBar =
    arrayOf(
        AcfunScreens.HomePage,
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
