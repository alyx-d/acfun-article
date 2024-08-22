package com.qt.app

import android.app.Application
import com.qt.app.core.network.utils.initConnectivityManage
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initConnectivityManage(this.applicationContext)
    }
}