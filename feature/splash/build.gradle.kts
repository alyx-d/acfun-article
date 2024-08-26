plugins {
    alias(libs.plugins.qt.app.android.library)
    alias(libs.plugins.qt.app.android.hilt)
}

android {
    namespace = "com.qt.app.feature.splash"
}

dependencies {
    api(project(":core:navigation"))
    api(project(":core:ui-state"))
}