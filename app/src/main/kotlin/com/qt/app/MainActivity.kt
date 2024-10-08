package com.qt.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.qt.app.core.utils.SnackBarHostStateHolder
import com.qt.app.ui.AppBottomNavBar
import com.qt.app.ui.AppNavHost
import com.qt.app.ui.AppSnackBarHost
import com.qt.app.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                val refreshState = remember { mutableStateOf(false) }
                val snackbarHostState = remember { SnackbarHostState() }
                val selectedPage = remember { mutableIntStateOf(0) }
                LaunchedEffect(Unit) {
                    SnackBarHostStateHolder.handleHostState(snackbarHostState)
                }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { AppSnackBarHost(snackbarHostState) },
                    bottomBar = { AppBottomNavBar(navController, selectedPage, refreshState) },
                ) { innerPadding ->
                    Surface(modifier = Modifier.padding(innerPadding)) {
                        AppNavHost(navController, selectedPage, refreshState)
                    }
                }
            }
        }
    }
}


