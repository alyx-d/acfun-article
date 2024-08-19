plugins {
    alias(libs.plugins.qt.app.android.library)
}

android {
    namespace = "com.qt.app.feature.profile"
}

dependencies {
    api(project(":core:ui-state"))
}