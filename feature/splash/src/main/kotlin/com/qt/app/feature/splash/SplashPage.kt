package com.qt.app.feature.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.qt.app.core.navigation.AcfunScreens
import kotlinx.coroutines.delay

@Composable
fun SplashPage(navHostController: NavHostController) {
    LaunchedEffect(Unit) {
        delay(10)
        navHostController.navigate(AcfunScreens.VideoPage.route) {
            popUpTo(0)
            launchSingleTop = true
        }
    }
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    )
}