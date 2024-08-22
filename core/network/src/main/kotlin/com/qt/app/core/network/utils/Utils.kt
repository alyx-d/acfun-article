package com.qt.app.core.network.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

var connectivityManager: ConnectivityManager? = null

fun initConnectivityManage(context: Context) {
    connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}

fun isOnline(): Boolean {
    val cm = connectivityManager!!
    val nw = cm.activeNetwork ?: return false
    val actNw = cm.getNetworkCapabilities(nw) ?: return false
    return when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
}

fun restartApp(context: Context) {
    val packageManager = context.packageManager
    val intent = packageManager.getLaunchIntentForPackage(context.packageName)
    val mainIntent = Intent.makeMainActivity(intent?.component)
    mainIntent.`package` = context.packageName
    context.startActivity(mainIntent)
    Runtime.getRuntime().exit(0)
}