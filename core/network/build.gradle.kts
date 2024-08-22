plugins {
    alias(libs.plugins.qt.app.android.library)
    alias(libs.plugins.qt.app.android.hilt)
    alias(libs.plugins.jetbrains.kotlin.plugin.serialization)
}

android {
    namespace = "com.qt.app.core.network"
}

dependencies {
    api(libs.retrofit2)
    api(libs.retrofit2.converter.kotlinx.serialization)
    api(libs.retrofit2.converter.scalars)
    api(libs.jetbrains.kotlinx.serialization.json)
    api(libs.okhttp3.logging.interceptor)
    implementation(project(":core:ui-state"))
}