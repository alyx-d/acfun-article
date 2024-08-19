plugins {
    alias(libs.plugins.qt.app.android.library)
}

android {
    namespace = "com.qt.app.feature.dynamic"
}

dependencies {
    api(project(":core:ui-state"))
}