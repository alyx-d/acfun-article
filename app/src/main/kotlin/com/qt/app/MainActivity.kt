package com.qt.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi
import androidx.navigation.compose.rememberNavController
import com.qt.app.core.utils.SnackBarHostStateHolder
import com.qt.app.ui.AppBottomNavBar
import com.qt.app.ui.AppNavHost
import com.qt.app.ui.AppSnackBarHost
import com.qt.app.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @UnstableApi
    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                val refreshState = remember { mutableStateOf(false) }
                val snackbarHostState = remember { SnackbarHostState() }
                LaunchedEffect(Unit) {
                    SnackBarHostStateHolder.handleHostState(snackbarHostState)
                }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { AppSnackBarHost(snackbarHostState) },
                    bottomBar = { AppBottomNavBar(navController, refreshState) },
                ) { innerPadding ->
                    Surface(modifier = Modifier.padding(innerPadding)) {
                        AppNavHost(navController, refreshState)
                    }
                }
            }
        }
    }
}


