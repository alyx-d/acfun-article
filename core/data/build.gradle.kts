plugins {
    alias(libs.plugins.qt.app.android.library)
    alias(libs.plugins.qt.app.android.hilt)
    alias(libs.plugins.jetbrains.kotlin.plugin.serialization)
}

android {
    namespace = "com.qt.app.core.data"
}

dependencies {
    api(project(":core:network"))
    api(libs.androidx.paging.rumtime)
    api(libs.androidx.paging.compose)
}