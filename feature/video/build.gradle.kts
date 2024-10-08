plugins {
    alias(libs.plugins.qt.app.android.library)
    alias(libs.plugins.qt.app.android.hilt)
    alias(libs.plugins.jetbrains.kotlin.plugin.serialization)
}

android {
    namespace = "com.qt.app.feature.video"
}

dependencies {
    api(project(":core:navigation"))
    api(project(":core:ui-state"))
    api(project(":core:video-player"))
    implementation(libs.jsoup)
}